package travelDream.entity.products;

import travelDream.dto.FlightDto;
import travelDream.dto.TransportDto;
import travelDream.entity.generics.Airport;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 16:26
 *
 *         This class is the model for the flight entity
 */

@Entity
public class Flight extends Transport {
    private String flightNumber;
    private String airline;

    @ManyToOne()
    private Airport departureAirport;
    @ManyToOne()
    private Airport arrivalAirport;

    public Flight() {
        super();
    }

    public Flight(FlightDto flightDto) {
        super(flightDto);

        Airport airport;

        this.setFlightNumber(flightDto.getFlightNumber());
        this.setAirline(flightDto.getAirline());

        airport = new Airport(flightDto.getDepartureAirport());
        this.setDepartureAirport(airport);
        airport = new Airport(flightDto.getArrivalAirport());
        this.setArrivalAirport(airport);
    }

    @Override
    public TransportDto getDto() {
        FlightDto flightDto = new FlightDto();

        flightDto.setId(this.getId());
        flightDto.setPrice(this.getPrice());
        flightDto.setDeparture(this.getDeparture());
        flightDto.setArrival(this.getArrival());
        flightDto.setDepartureCity(this.getDepartureCity().getDto());
        flightDto.setArrivalCity(this.getArrivalCity().getDto());
        flightDto.setActive(this.isActive());

        flightDto.setFlightNumber(this.getFlightNumber());
        flightDto.setAirline(this.getAirline());
        flightDto.setDepartureAirport(this.getDepartureAirport().getDto());
        flightDto.setArrivalAirport(this.getArrivalAirport().getDto());

        return flightDto;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
}
