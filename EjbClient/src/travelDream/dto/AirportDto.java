package travelDream.dto;

/**
 * @author Massimo De Marchi
 *         Date: 08/01/14
 *         Time: 19:20
 */
public class AirportDto {
    private int id;
    private String name;
    private CityDto city;

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

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }
}
