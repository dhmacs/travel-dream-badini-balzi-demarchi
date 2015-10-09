package travelDream.dto;

import java.util.Date;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 16:14
 */
public abstract class TransportDto {
    private int id;
    private double price;
    private Date departure;     // Contains both date and time
    private Date arrival;       // Contains both date and time
    private CityDto departureCity;
    private CityDto arrivalCity;
    private boolean isActive = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public CityDto getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(CityDto departureCity) {
        this.departureCity = departureCity;
    }

    public CityDto getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(CityDto arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransportDto)) return false;

        TransportDto that = (TransportDto) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
