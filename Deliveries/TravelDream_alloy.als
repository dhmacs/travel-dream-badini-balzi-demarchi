// Richiamo librerie

open util/integer as integer

// Creo signature utili per il modello

sig Luogo{}

/***********************
*DEFINIZIONE DEL MODELLO *
***********************/

sig Impiegato{}

sig UtenteNonRegistrato {
	pacchettiInCondivisione: set Pacchetto 		//Pacchetti che l'utente riceve in condivisione.
}

sig UtenteRegistrato {
	storicoAcquisti: set Pacchetto, 
	condivisioni: set Pacchetto,
	pacchettiInCondivisione: set Pacchetto, 		//Pacchetti che l'utente riceve in condivisione.
	giftList: set Pacchetto,									
	regaliRicevuti: set Pacchetto
}

abstract sig ProdottoBase{
	inseritoDa: one Impiegato
}

abstract sig Trasporto extends ProdottoBase{
	luogoPartenza: one Luogo,
	orarioPartenza: one Int,
	giornoPartenza: one Int,
	mesePartenza: one Int,
	annoPartenza: one Int,
	luogoArrivo: one Luogo,
	orarioArrivo: one Int,
	giornoArrivo: one Int,
	meseArrivo: one Int,
	annoArrivo: one Int
}{
	orarioPartenza > 0 and 
	giornoPartenza > 0 and
	mesePartenza > 0 and
	annoPartenza > 0 and
	orarioArrivo > 0 and
	giornoArrivo > 0 and
	meseArrivo > 0 and
	annoArrivo > 0	
}

sig Volo extends Trasporto{
}

sig Hotel extends ProdottoBase{
	luogo: one Luogo,
	giornoCheckIn: one Int,
	meseCheckIn: one Int,
	annoCheckIn: one Int,
	giornoCheckOut: one Int,
	meseCheckOut: one Int,
	annoCheckOut: one Int
}{
	giornoCheckIn > 0 and
	meseCheckIn > 0 and 
	annoCheckIn > 0 and 
	giornoCheckOut > 0 and 
	meseCheckOut > 0 and 
	annoCheckOut > 0
}

sig Escursione extends ProdottoBase{
	oraPartenza: one Int,
	giornoPartenza: one Int,
	mesePartenza: one Int,
	annoPartenza: one Int,
	oraRitorno: one Int,
	giornoRitorno: one Int,
	meseRitorno: one Int,
	annoRitorno: one Int
}{
	oraPartenza > 0 and 
	giornoPartenza > 0 and 
	mesePartenza > 0 and 
	annoPartenza > 0 and 
	oraRitorno > 0 and 
	giornoRitorno > 0 and 
	meseRitorno > 0 and
	annoRitorno > 0
}

sig Pacchetto{
	creatore: one Impiegato,
	trasportoAndata : one Volo,
	trasportiAndataAlternativi: set Volo,
	hotel: one Hotel,
	hotelAlternativi: set Hotel,
	escursione: lone Escursione,
	escursioniAlternative: set Escursione,
	trasportoRitorno: one Volo,
	trasportiRitornoAlternativi: set Volo,
	destinazione: one Luogo
}

//FATTI

//CONTROLLI SULLE SINGOLE SIGNATURE

fact orariHotelCoerenti {
	// La data di check-out deve essere successiva a quella di check-in
	all h:Hotel | (h.annoCheckOut > h.annoCheckIn or 
	(h.annoCheckOut = h.annoCheckIn and h.meseCheckOut > h.meseCheckIn) or
	( h.annoCheckOut = h.annoCheckIn and h.meseCheckOut = h.meseCheckIn and 
	h.giornoCheckOut > h.giornoCheckIn))
}

fact risorseAssegnateUnivocamente{
	// Non devo avere risorse che compaiono sia come selezione sia nel set delle alternative.
	// Per i voli devo evitare anche risorse replicate tra i voli di andata e quelli di ritorno.
	no p:Pacchetto| (some t:Trasporto | t = p.trasportoAndata and 
		(t in p.trasportiAndataAlternativi or t in p.trasportiRitornoAlternativi or t = p.trasportoRitorno)) 
	no p:Pacchetto | (some h:Hotel | h = p.hotel and h in p.hotelAlternativi) 
	no p:Pacchetto | (some e:Escursione | e = p.escursione and
	e in p.escursioniAlternative)
	no p:Pacchetto | (some t:Trasporto | t = p.trasportoRitorno and 
		(t in p.trasportiRitornoAlternativi or t in p.trasportiAndataAlternativi or t = p.trasportoAndata)) 
}



// Controlla la coerenza nei dati del singolo trasporto
fact trasportoCoerente{
	no t: Trasporto | (t.annoPartenza > t.annoArrivo or
	(t.annoPartenza = t.annoArrivo and t.mesePartenza > t.meseArrivo) or
	(t.annoPartenza = t.annoArrivo and t.mesePartenza = t.meseArrivo and t.giornoPartenza > t.giornoArrivo) or
	(t.annoPartenza = t.annoArrivo and t.mesePartenza = t.meseArrivo and t.giornoPartenza = t.giornoArrivo and t.orarioPartenza >= t.orarioArrivo))
	// Assumiamo che un trasporto abbia una durata minima e pertanto l'orario di partenza non può essere uguale a quello di arrivo
	no t: Trasporto | t.luogoPartenza = t.luogoArrivo
}


//CONTROLLI SUI PACCHETTI

fact voliInSequenzaCorretta {
	// Il volo di ritorno deve essere successivo al volo di andata.
	no p:Pacchetto | (p.trasportoAndata.annoArrivo > p.trasportoRitorno.annoPartenza or 
	(p.trasportoAndata.annoArrivo = p.trasportoRitorno.annoPartenza and p.trasportoAndata.meseArrivo > p.trasportoRitorno.mesePartenza ) or 
	(p.trasportoAndata.annoArrivo = p.trasportoRitorno.annoPartenza and p.trasportoAndata.meseArrivo = p.trasportoRitorno.mesePartenza 
		and p.trasportoAndata.giornoArrivo >= p.trasportoRitorno.giornoPartenza))
	// Assumiamo che siccome il pacchetto comprende l'hotel e l'escursione opzionale non ci siano due voli nello stesso giorno.
}

fact consistenzaLuoghi {
	no p: Pacchetto | p.trasportoAndata.luogoPartenza != p.trasportoRitorno.luogoArrivo or
								p.trasportoAndata.luogoArrivo != p.trasportoRitorno.luogoPartenza or
								p.hotel.luogo != p.trasportoAndata.luogoArrivo or
								p.hotel.luogo != p.destinazione							  
}

fact consistenzaDate{
	all p: Pacchetto | (p.hotel.annoCheckIn = p.trasportoAndata.annoArrivo and 
								p.hotel.meseCheckIn = p.trasportoAndata.meseArrivo and
								p.hotel.giornoCheckIn = p.trasportoAndata.giornoArrivo and
								p.hotel.annoCheckOut = p.trasportoRitorno.annoPartenza and
								p.hotel.meseCheckOut = p.trasportoRitorno.mesePartenza and
								p.hotel.giornoCheckOut = p.trasportoRitorno.giornoPartenza)
}

fact escursioneConsistente{
	// L'escursione deve essere programmata durante l'arco di tempo che intercorre tra il volo di andata e quello
	//di ritorno.
	all p:Pacchetto| (#p.escursione = 0 or (p.escursione.annoPartenza > p.trasportoAndata.annoArrivo or
	(p.escursione.annoPartenza = p.trasportoAndata.annoArrivo and p.escursione.mesePartenza > p.trasportoAndata.meseArrivo) or
	(p.escursione.annoPartenza = p.trasportoAndata.annoArrivo and p.escursione.mesePartenza = p.trasportoAndata.meseArrivo and 
		p.escursione.giornoPartenza > p.trasportoAndata.giornoArrivo) or 
	p.escursione.annoPartenza = p.trasportoAndata.annoArrivo and p.escursione.mesePartenza = p.trasportoAndata.meseArrivo and 
	p.escursione.giornoPartenza = p.trasportoAndata.giornoArrivo and p.escursione.oraPartenza > p.trasportoAndata.orarioArrivo)
	and
	(p.escursione.annoRitorno < p.trasportoRitorno.annoPartenza or
	(p.escursione.annoRitorno = p.trasportoRitorno.annoPartenza and p.escursione.meseRitorno < p.trasportoRitorno.mesePartenza) or
	(p.escursione.annoRitorno = p.trasportoRitorno.annoPartenza and p.escursione.meseRitorno = p.trasportoRitorno.mesePartenza and 
		p.escursione.giornoRitorno < p.trasportoRitorno.giornoPartenza) or (p.escursione.annoRitorno = p.trasportoRitorno.annoPartenza and 
		p.escursione.meseRitorno = p.trasportoRitorno.mesePartenza and	p.escursione.giornoRitorno = p.trasportoRitorno.giornoPartenza and
		p.escursione.oraRitorno < p.trasportoRitorno.orarioPartenza)))
}

//CONTROLLI SULLE AZIONI

fact pacchettiCondivisiAUtenti{
// Ogni pacchetto condiviso deve avere almeno un utente registrato che lo ha condiviso e almeno 
// un altro utente che riceve la condivisione
 all p: Pacchetto | (some ur: UtenteRegistrato | p in ur.condivisioni) =>
 	((some unr: UtenteNonRegistrato | p in unr.pacchettiInCondivisione) or 
 	(some ur2: UtenteRegistrato | p in ur2.pacchettiInCondivisione))
 all p:Pacchetto | (no ur: UtenteRegistrato | p in ur.condivisioni) =>
 	(no unr:UtenteNonRegistrato | p in unr.pacchettiInCondivisione) and 
 	(no ur:UtenteRegistrato | p in ur.pacchettiInCondivisione)
}



//ASSERZIONI

assert noVoliSfalsati {
	all p:Pacchetto | (p.trasportoAndata.annoArrivo < p.trasportoRitorno.annoPartenza or 
							(p.trasportoAndata.annoArrivo = p.trasportoRitorno.annoPartenza and p.trasportoAndata.meseArrivo < p.trasportoRitorno.mesePartenza ) or 
							(p.trasportoAndata.annoArrivo = p.trasportoRitorno.annoPartenza and p.trasportoAndata.meseArrivo = p.trasportoRitorno.mesePartenza and 
							p.trasportoAndata.giornoArrivo < p.trasportoRitorno.giornoPartenza))
}

assert noRisorseDuplicate{
	no pb: ProdottoBase | (some p: Pacchetto | (pb = p.trasportoAndata and 
	(pb in p.trasportiAndataAlternativi or pb in p.trasportiRitornoAlternativi or pb = p.trasportoRitorno)) or 
	(pb = p.hotel and pb in p.hotelAlternativi) or (pb = p.escursione and pb in p.escursioniAlternative) or
	(pb = p.trasportoRitorno and (pb in p.trasportiRitornoAlternativi or pb in p.trasportiAndataAlternativi or pb = p.trasportoAndata)))
}	

assert controlloDatePacchetto{
	no p: Pacchetto | p.trasportoAndata.annoArrivo != p.hotel.annoCheckIn or
								p.trasportoAndata.meseArrivo != p.hotel.meseCheckIn or
								p.trasportoAndata.giornoArrivo != p.hotel.giornoCheckIn or
								p.trasportoRitorno.annoPartenza != p.hotel.annoCheckOut or
								p.trasportoRitorno.mesePartenza != p.hotel.meseCheckOut or
								p.trasportoRitorno.giornoPartenza != p.hotel.giornoCheckOut
}

assert controlloEscursioneConsistente{
	no p: Pacchetto |(#p.escursione = 1 and (p.escursione.annoPartenza < p.trasportoAndata.annoArrivo or
	(p.escursione.annoPartenza = p.trasportoAndata.annoArrivo and p.escursione.mesePartenza < p.trasportoAndata.meseArrivo) or
	(p.escursione.annoPartenza = p.trasportoAndata.annoArrivo and p.escursione.mesePartenza = p.trasportoAndata.meseArrivo and 
		p.escursione.giornoPartenza < p.trasportoAndata.giornoArrivo) or 
	(p.escursione.annoPartenza = p.trasportoAndata.annoArrivo and p.escursione.mesePartenza = p.trasportoAndata.meseArrivo and 
	p.escursione.giornoPartenza = p.trasportoAndata.giornoArrivo and p.escursione.oraPartenza < p.trasportoAndata.orarioArrivo))
	or
	(p.escursione.annoRitorno > p.trasportoRitorno.annoPartenza or
	(p.escursione.annoRitorno = p.trasportoRitorno.annoPartenza and p.escursione.meseRitorno > p.trasportoRitorno.mesePartenza) or
	(p.escursione.annoRitorno = p.trasportoRitorno.annoPartenza and p.escursione.meseRitorno = p.trasportoRitorno.mesePartenza and 
		p.escursione.giornoRitorno > p.trasportoRitorno.giornoPartenza) or (p.escursione.annoRitorno = p.trasportoRitorno.annoPartenza and 
		p.escursione.meseRitorno = p.trasportoRitorno.mesePartenza and	p.escursione.giornoRitorno = p.trasportoRitorno.giornoPartenza and
		p.escursione.oraRitorno > p.trasportoRitorno.orarioPartenza)))
}

assert condivisioneConsistente {
	no p: Pacchetto | (some ur: UtenteRegistrato | p in ur.condivisioni) and
 		(no unr: UtenteNonRegistrato | p in unr.pacchettiInCondivisione) and 
 		(no ur2: UtenteRegistrato | p in ur2.pacchettiInCondivisione)
	no p:Pacchetto | (no ur: UtenteRegistrato | p in ur.condivisioni) and
 		((some unr:UtenteNonRegistrato | p in unr.pacchettiInCondivisione) or
 		(some ur:UtenteRegistrato | p in ur.pacchettiInCondivisione))
}

assert pacchettiSenzaEscursione{ 							//il modello prevede escursioni opzionali quindi l'asserzione dovrà trovare un controesempio
	no p: Pacchetto | #p.escursione = 0
}

//PREDICATI

pred acquisto[u_before,u_after: UtenteRegistrato, p:Pacchetto]{
	p not in u_before.storicoAcquisti //per mostrare l'acquisto
	u_after.storicoAcquisti = u_before.storicoAcquisti + p
	u_after.condivisioni = u_before.condivisioni
	u_after.pacchettiInCondivisione = u_before.pacchettiInCondivisione
	u_after.giftList = u_before.giftList
	u_after.regaliRicevuti =u_before.regaliRicevuti
}

pred condivisioneAUtenteNonRegistrato[ur_before,ur_after:UtenteRegistrato, 
	unr_before, unr_after:UtenteNonRegistrato, p:Pacchetto]{
	p not in ur_before.condivisioni //per mostrare la condivisione
	p not in unr_before.pacchettiInCondivisione //per mostrare la condivisione
	ur_after.condivisioni =  ur_before.condivisioni + p
	ur_after.pacchettiInCondivisione = ur_before.pacchettiInCondivisione
	ur_after.storicoAcquisti =  ur_before.storicoAcquisti
	ur_after.giftList = ur_before.giftList
	ur_after.regaliRicevuti = ur_before.regaliRicevuti 
	unr_after.pacchettiInCondivisione = unr_before.pacchettiInCondivisione + p
}

pred condivisioneAUtenteRegistrato[ur_before,ur_after,ur2_before,ur2_after:UtenteRegistrato,
	p:Pacchetto]{
	ur_before != ur2_before and ur_before != ur2_after and
	ur_after != ur2_before and ur_after != ur2_after
	p not in ur_before.condivisioni	  	//per mostrare la condivisione
	p not in ur2_before.pacchettiInCondivisione
	ur_after.condivisioni =  ur_before.condivisioni + p
	ur_after.pacchettiInCondivisione = ur_before.pacchettiInCondivisione
	ur_after.storicoAcquisti =  ur_before.storicoAcquisti
	ur_after.giftList = ur_before.giftList
	ur_after.regaliRicevuti = ur_before.regaliRicevuti 
	ur2_after.pacchettiInCondivisione = ur2_before.pacchettiInCondivisione + p
	ur2_after.condivisioni = ur2_before.condivisioni
	ur2_after.storicoAcquisti =  ur2_before.storicoAcquisti
	ur2_after.regaliRicevuti = ur2_before.regaliRicevuti
	ur2_after.giftList = ur2_before.giftList
}

pred giftList [u_before,u_after: UtenteRegistrato, p:Pacchetto]{
	p not in u_before.giftList		//per mostrare l'inserimento in giftList
	u_after.storicoAcquisti = u_before.storicoAcquisti
	u_after.condivisioni = u_before.condivisioni
	u_after.pacchettiInCondivisione = u_before.pacchettiInCondivisione
	u_after.giftList = u_before.giftList + p
	u_after.regaliRicevuti = u_before.regaliRicevuti
}

pred show() {
	#Pacchetto > 0
	#Escursione > 0
}

//COMANDI
//run show for 5
//check noVoliSfalsati 
//check noRisorseDuplicate
//check controlloDatePacchetto
//check controlloEscursioneConsistente
//check pacchettiSenzaEscursione
//check condivisioneConsistente
//run acquisto for 5
//run condivisioneAUtenteNonRegistrato for 5
//run condivisioneAUtenteRegistrato for 5
//run giftList for 5
