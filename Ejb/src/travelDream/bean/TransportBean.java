package travelDream.bean;

import travelDream.dto.AirportDto;
import travelDream.dto.FlightDto;
import travelDream.dto.TransportDto;
import travelDream.entity.generics.Airport;
import travelDream.entity.products.Flight;
import travelDream.entity.products.Transport;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 17:54
 */

@Stateless
public class TransportBean implements TransportInterface {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TrasportInputError> createTransport(TransportDto transportDto) {
        // Validation of the dto
        List<TrasportInputError> trasportInputErrors = this.validate(transportDto);

        // If there's no errors procede to create the transport
        if (trasportInputErrors.size() == 0) {
            if (transportDto instanceof FlightDto) {
                Flight flight = new Flight((FlightDto)transportDto);
                this.entityManager.persist(flight);
            }
        }

        return trasportInputErrors;
    }

    @Override
    public List<TrasportInputError> updateTransport(TransportDto transportDto) {
        List<TrasportInputError> trasportInputErrors = new ArrayList<TrasportInputError>();

        Transport transport = this.entityManager.find(Transport.class, transportDto.getId());

        if (transport instanceof Flight) {
            Flight flight = (Flight)transport;
            FlightDto dto = (FlightDto)transportDto;

            flight.setFlightNumber(dto.getFlightNumber());
            flight.setAirline(dto.getAirline());
            flight.setPrice(dto.getPrice());
            flight.setActive(dto.isActive());

            // Validation of the dto
            trasportInputErrors = this.validate(flight.getDto());

            if (trasportInputErrors.size() == 0) {
                this.entityManager.merge(flight);
            }
        }
        /*

        // Validation of the dto
        List<TrasportInputError> trasportInputErrors = this.validate(transportDto);

        // If there's no errors procede to update the transport
        if (trasportInputErrors.size() == 0) {
            if(transportDto instanceof FlightDto) {
                Flight flight = new Flight((FlightDto)transportDto);
                flight.setId(transportDto.getId());
                this.entityManager.merge(flight);
            }
        }   */
        return trasportInputErrors;
    }

    /**
     * Delete transport entity of the given dto
     *
     * @param transportDto
     * @return
     */
    @Override
    public List<TrasportInputError> deleteTransport(TransportDto transportDto) {
        Object transport = this.entityManager.createNamedQuery(Transport.FIND_BY_ID)
                .setParameter("id", transportDto.getId()).getSingleResult();

        if (transport instanceof Flight) {
            Flight flight = (Flight)transport;
            this.entityManager.remove(flight);
        }

        this.entityManager.flush();
        // Clear JPA cache to synchronize with DB
        this.entityManager.getEntityManagerFactory().getCache().evictAll();

        return new ArrayList<TrasportInputError>(); // TODO
    }

    @Override
    public List<TransportDto> getTransports() {
        List<TransportDto> transportDtos = new ArrayList<TransportDto>();
        List<Transport> transports = this.entityManager.createNamedQuery(Transport.FIND_ALL).getResultList();

        for (Transport transport: transports) {
            if (transport instanceof Flight) {
                Flight flight = (Flight)transport;

                transportDtos.add(flight.getDto());
            }
        }

        return transportDtos;
    }

    /**
     * Get list of airports
     *
     * @return list of AirportDtos
     */
    @Override
    public List<AirportDto> getAirports() {
        List<AirportDto> airportDtos = new ArrayList<AirportDto>();
        List<Airport> airports = this.entityManager.createNamedQuery(Airport.FIND_ALL).getResultList();

        for (Airport airport: airports) {
            airportDtos.add(airport.getDto());
        }

        return airportDtos;
    }

    /**
     * Check for input errors in the given dto
     * @param transportDto
     * @return list of errors
     */
    private List<TrasportInputError> validate(TransportDto transportDto) {
        List<TrasportInputError> trasportInputErrors = new ArrayList<TrasportInputError>();

        if (transportDto instanceof FlightDto) {
            FlightDto flightDto = (FlightDto)transportDto;

            AirportDto departureAirport = flightDto.getDepartureAirport();
            Date departureDate = flightDto.getDeparture();
            AirportDto arrivalAirport = flightDto.getArrivalAirport();
            Date arrivalDate = flightDto.getArrival();
            double price = flightDto.getPrice();
            String flightNumber = flightDto.getFlightNumber();
            String flightNumberPattern = "^([A-Z]{2}|[A-Z]\\d|\\d[A-Z])[1-9](\\d{1,3})?$";
            String airline = flightDto.getAirline();

            if(departureAirport == null || departureAirport.getName() == null ||
                    departureAirport.getCity() == null)
                trasportInputErrors.add(TrasportInputError.MISSING_DEPARTURE);

            if(departureDate == null)
                trasportInputErrors.add(TrasportInputError.MISSING_DEPARTURE_DATETIME);

            if(arrivalAirport == null || arrivalAirport.getName() == null ||
                    arrivalAirport.getCity() == null)
                trasportInputErrors.add(TrasportInputError.MISSING_ARRIVAL);

            if(arrivalDate == null)
                trasportInputErrors.add(TrasportInputError.MISSING_ARRIVAL_DATETIME);

            String text = Double.toString(price);
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            if (decimalPlaces > 2 || price < 0)
                trasportInputErrors.add(TrasportInputError.INVALID_PRICE);

            if(flightNumber == null || flightNumber.isEmpty() || !flightNumber.matches(flightNumberPattern))
                trasportInputErrors.add(TrasportInputError.WRONG_FLIGHT_NUMBER);

            if(airline == null || airline.isEmpty())
                trasportInputErrors.add(TrasportInputError.MISSING_AIRLINE);

            if (arrivalDate != null && departureDate != null && arrivalDate.before(departureDate))
                trasportInputErrors.add(TrasportInputError.DATE_ARRIVAL_BEFORE_DATE_DEPARTURE);
        }

        return trasportInputErrors;
    }
}


















