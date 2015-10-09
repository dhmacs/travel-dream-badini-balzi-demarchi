package travelDream.entity.products;

import travelDream.dto.ExcursionDto;
import travelDream.entity.generics.City;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity implementation class representing all the excursions
 * registered in travelDream database
 */

@Entity
@Table(name = "excursion")
@NamedQueries({
        @NamedQuery(
                name = Excursion.FIND_ALL,
                query = "select e from Excursion e"
        ),
        @NamedQuery(
                name = Excursion.FIND_BY_ID,
                query = "select e from Excursion e where e.id = :id"
        )
})
public class Excursion implements Serializable {
    private static final long serialVersionUID = 1L;
    //Query name

    public static final String FIND_ALL = "Excursion.findAll";
    public static final String FIND_BY_ID = "Excursion.findById";

    //-----------------------------------------------------------//

    //Attributes

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
    private boolean isActive = true;
    //-----------------------------------------------------------//

    //Constructors
    public Excursion() {
        super();
    }

    public Excursion(ExcursionDto excursionDto) {
        this.id = excursionDto.getId();
        this.name = excursionDto.getName();
        this.description = excursionDto.getDescription();
        this.price = excursionDto.getPrice();
        this.startingHour = excursionDto.getStartingHour();
        this.endingHour = excursionDto.getEndingHour();
        this.isActive = excursionDto.isActive();
        this.city = new City();
        this.city.setId(excursionDto.getCity().getId());
        this.city.setName(excursionDto.getCity().getName());
        this.city.setNation(excursionDto.getCity().getNation());
    }

    //-----------------------------------------------------------//

    //Getter and Setter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(Date startingHour) {
        this.startingHour = startingHour;
    }

    public Date getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(Date endingHour) {
        this.endingHour = endingHour;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ExcursionDto getDto(){
        ExcursionDto excursionDto = new ExcursionDto();
        excursionDto.setId(this.id);
        excursionDto.setName(this.name);
        excursionDto.setCity(this.getCity().getDto());
        excursionDto.setPrice(this.price);
        excursionDto.setStartingHour(this.startingHour);
        excursionDto.setEndingHour(this.endingHour);
        excursionDto.setDescription(this.description);
        excursionDto.setActive(this.isActive);
        return excursionDto;
    }
}
