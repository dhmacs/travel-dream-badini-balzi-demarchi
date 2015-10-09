package travelDream.entity.products;

import travelDream.dto.TransportDto;
import travelDream.entity.generics.City;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alessandrobalzi on 22/01/14.
 */
@Entity
@Table(name = "purchased_transport")
public abstract class PurchasedTransport implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes

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

    // Constructors

    public PurchasedTransport(){
        super();
    }

    public PurchasedTransport(Transport transport) {
        this.departure = transport.getDeparture();
        this.arrival = transport.getArrival();
        this.departureCity = transport.getDepartureCity();
        this.arrivalCity = transport.getArrivalCity();
    }

    // Getters

    public abstract TransportDto getDto();


    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public Date getDeparture() {
        return departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public City getDepartureCity() {
        return departureCity;
    }

    public City getArrivalCity() {
        return arrivalCity;
    }
}
