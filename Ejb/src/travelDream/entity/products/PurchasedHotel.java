package travelDream.entity.products;

import travelDream.dto.HotelDto;
import travelDream.entity.generics.City;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by alessandrobalzi on 22/01/14.
 */

@Entity
@Table(name = "purchased_hotel")
public class PurchasedHotel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private double price;
    @ManyToOne
    private City city;

    // Constructors

    public PurchasedHotel() {
        super();
    }

    public PurchasedHotel(Hotel hotel) {
        this.name = hotel.getName();
        this.description = hotel.getDescription();
        this.city = hotel.getCity();
        this.price = hotel.getPrice();
    }

    // Getters

    public HotelDto getDto() {
        HotelDto hotelDto = new HotelDto();

        hotelDto.setId(this.getId());
        hotelDto.setName(this.getName());
        hotelDto.setDescription(this.getDescription());
        hotelDto.setPrice(this.getPrice());
        hotelDto.setCityDto(this.getCity().getDto());

        return hotelDto;
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
}
