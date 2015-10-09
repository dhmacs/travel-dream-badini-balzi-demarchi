package travelDream.entity.products;

import travelDream.dto.TransportDto;
import travelDream.entity.generics.City;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 15:52
 */

@Entity
@Table(name = "transport")
@NamedQueries({
        @NamedQuery(
                name = Transport.FIND_ALL,
                query = "select t from Transport t"
        ),
        @NamedQuery(
                name = Transport.FIND_BY_ID,
                query = "select t from Transport t where t.id = :id"
        )
})
public abstract class Transport implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Transport.findAll";
    public static final String FIND_BY_ID = "Transport.findById";

    @Id
    @GeneratedValue
    private int id;

    private double price;
    @Temporal(TemporalType.TIMESTAMP)
    private Date departure;     // Contains both date and time
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrival;       // Contains both date and time
    @ManyToOne
    private City departureCity;
    @ManyToOne
    private City arrivalCity;
    private boolean isActive = true;
    public Transport(){
        super();
    }

    public Transport(TransportDto transportDto) {
        this.setId(transportDto.getId());
        this.setPrice(transportDto.getPrice());
        this.setDeparture(transportDto.getDeparture());
        this.setArrival(transportDto.getArrival());
        this.setDepartureCity(new City(transportDto.getDepartureCity()));
        this.setArrivalCity(new City(transportDto.getArrivalCity()));
        this.setActive(transportDto.isActive());
    }

    public abstract TransportDto getDto();

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

    public City getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(City departurePlace) {
        this.departureCity = departurePlace;
    }

    public City getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(City arrivalPlace) {
        this.arrivalCity = arrivalPlace;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
