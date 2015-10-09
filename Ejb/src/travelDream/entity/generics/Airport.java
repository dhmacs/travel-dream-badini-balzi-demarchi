package travelDream.entity.generics;

import travelDream.dto.AirportDto;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Massimo De Marchi
 *         Date: 08/01/14
 *         Time: 19:04
 */

@Entity
@Table(name = "airport")
@NamedQuery(
        name = Airport.FIND_ALL,
        query = "select a from Airport a"
)
public class Airport implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIND_ALL = "Airport.findAll";

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @ManyToOne()
    private City city;


    public Airport() {
        super();
    }

    public Airport(AirportDto airportDto) {
        City city = new City();

        city.setId(airportDto.getCity().getId());
        city.setName(airportDto.getCity().getName());
        city.setNation(airportDto.getCity().getNation());

        this.setId(airportDto.getId());
        this.setName(airportDto.getName());
        this.setCity(city);
    }

    public AirportDto getDto() {
        AirportDto airportDto = new AirportDto();

        airportDto.setId(this.getId());
        airportDto.setName(this.getName());
        airportDto.setCity(this.getCity().getDto());

        return airportDto;
    }

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
