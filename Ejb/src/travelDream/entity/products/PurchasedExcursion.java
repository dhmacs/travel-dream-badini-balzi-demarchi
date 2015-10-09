package travelDream.entity.products;

import travelDream.dto.ExcursionDto;
import travelDream.entity.generics.City;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alessandrobalzi on 22/01/14.
 */
@Entity
@Table(name = "purchased_excursion")
public class PurchasedExcursion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String description;
    private double price;
    @ManyToOne()
    private City city;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startingHour;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingHour;

    // Constructors

    public PurchasedExcursion() {
        super();
    }

    public PurchasedExcursion(Excursion excursion){
        this.name = excursion.getName();
        this.description = excursion.getDescription();
        this.price = excursion.getPrice();
        this.city = excursion.getCity();
        this.startingHour = excursion.getStartingHour();
        this.endingHour = excursion.getEndingHour();
    }

    // Getters

    public ExcursionDto getDto() {
        ExcursionDto excursionDto = new ExcursionDto();

        excursionDto.setId(this.getId());
        excursionDto.setName(this.getName());
        excursionDto.setDescription(this.getDescription());
        excursionDto.setPrice(this.getPrice());
        excursionDto.setCity(this.getCity().getDto());
        excursionDto.setStartingHour(this.getStartingHour());
        excursionDto.setEndingHour(this.getEndingHour());

        return excursionDto;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public City getCity() {
        return city;
    }

    public Date getStartingHour() {
        return startingHour;
    }

    public Date getEndingHour() {
        return endingHour;
    }
}
