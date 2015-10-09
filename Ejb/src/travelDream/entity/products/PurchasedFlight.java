package travelDream.entity.products;

import travelDream.dto.FlightDto;
import travelDream.dto.TransportDto;
import travelDream.entity.generics.Airport;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by alessandrobalzi on 22/01/14.
 */
@Entity
public class PurchasedFlight extends PurchasedTransport{

    private String flightNumber;
    private String airline;

    @ManyToOne()
    private Airport departureAirport;
    @ManyToOne()
    private Airport arrivalAirport;

    // Constructors

    public PurchasedFlight() {
        super();
    }

    @Override
    public TransportDto getDto() {
        FlightDto flightDto = new FlightDto();

        // Transport attributes
        flightDto.setId(this.getId());
        flightDto.setPrice(this.getPrice());
        flightDto.setDeparture(this.getDeparture());
        flightDto.setArrival(this.getArrival());
        flightDto.setDepartureCity(this.getDepartureCity().getDto());
        flightDto.setArrivalCity(this.getArrivalCity().getDto());

        // Flight attributes
        flightDto.setFlightNumber(this.getFlightNumber());
        flightDto.setAirline(this.getAirline());
        flightDto.setDepartureAirport(this.getDepartureAirport().getDto());
        flightDto.setArrivalAirport(this.getArrivalAirport().getDto());

        return flightDto;
    }

    public PurchasedFlight(Flight flight) {
        // Initialize transport attributes
        super(flight);

        // Initialize flight attributes
        this.flightNumber = flight.getFlightNumber();
        this.airline = flight.getAirline();
        this.arrivalAirport = flight.getArrivalAirport();
        this.departureAirport = flight.getDepartureAirport();
    }

    // Getters


    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }
}
