package travelDream.entity.generics;

import travelDream.dto.CityDto;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Massimo De Marchi
 *         Date: 08/01/14
 *         Time: 18:54
 */

@Entity
@Table(name = "city")
@NamedQuery(
        name = City.FIND_ALL,
        query = "select c from City c"
)
public class City implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIND_ALL = "City.findAll";

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String nation;


    public City() {
        super();
    }

    public City(CityDto cityDto) {
        this.setId(cityDto.getId());
        this.setName(cityDto.getName());
        this.setNation(cityDto.getNation());
    }


    public CityDto getDto() {
        CityDto cityDto = new CityDto();

        cityDto.setId(this.getId());
        cityDto.setName(this.getName());
        cityDto.setNation(this.getNation());

        return cityDto;
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

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
