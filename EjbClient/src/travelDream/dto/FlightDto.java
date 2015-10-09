package travelDream.dto;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 16:49
 */
public class FlightDto extends TransportDto {
    private String flightNumber;
    private String airline;
    private AirportDto departureAirport;
    private AirportDto arrivalAirport;

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

    public AirportDto getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(AirportDto departureAirport) {
        this.departureAirport = departureAirport;
    }

    public AirportDto getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(AirportDto arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }


    @Override
    public boolean equals(Object o) {
        //return super.equals(o);
        return (o == this) || (o instanceof FlightDto && this.getId() == (((FlightDto) o).getId()));
    }

    @Override
    public int hashCode() {
        //return super.hashCode();
        return this.getClass().hashCode() + this.getId();
    }
}
