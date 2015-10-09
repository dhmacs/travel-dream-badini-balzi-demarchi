package travelDream.entity.products;

import travelDream.dto.HotelDto;
import travelDream.entity.generics.City;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity implementation class representing all the hotels
 * registered in travelDream database
 */
@Entity
//Named queries for the entity Hotel
@Table(name = "hotel")
@NamedQueries({
        @NamedQuery(
                name = Hotel.FIND_ALL,
                query = "select h from Hotel h"
        ),
        @NamedQuery(
                name = Hotel.FIND_BY_ID,
                query = "select h from Hotel h where h.id = :id"
        )
})
public class Hotel implements Serializable{
    private static final long serialVersionUID = 1L;

    //Query names

    public static final String FIND_ALL = "Hotel.findAll";
    public static final String FIND_BY_ID = "Hotel.findById";

    //-----------------------------------------------------------//

    //Attributes

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private double price;
    @ManyToOne
    private City city;
    private boolean isActive = true;

    //-----------------------------------------------------------//

    //Constructors

    public Hotel(){
        super();
    }

    public Hotel(HotelDto hotelDto){
        this.id = hotelDto.getId();
        this.name = hotelDto.getName();
        this.description = hotelDto.getDescription();
        this.price = hotelDto.getPrice();
        this.city = new City(hotelDto.getCityDto());
        this.isActive = hotelDto.isActive();
    }

    //-----------------------------------------------------------//

    /**
     * Method returning the Dto representation of the entity
      * @return Dto representing the entity
     */
    public HotelDto getDto(){
        HotelDto hotelDto = new HotelDto();

        hotelDto.setId(this.getId());
        hotelDto.setName(this.getName());
        hotelDto.setDescription(this.getDescription());
        hotelDto.setPrice(this.getPrice());
        hotelDto.setCityDto(this.city.getDto());
        hotelDto.setActive(this.isActive());

        return hotelDto;
    }

    //Getter and setter

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
