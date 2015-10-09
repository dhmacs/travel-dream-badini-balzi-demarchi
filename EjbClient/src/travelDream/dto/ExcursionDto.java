package travelDream.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.sql.Time;
import java.util.Date;

/**
 * Created by alessandrobalzi on 07/01/14.
 */
public class ExcursionDto {

    private int id;
    private String name;
    private String description;
    private double price;
    private CityDto city;
    private Date startingHour;
    private Date endingHour;
    private boolean isActive = true;

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

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcursionDto that = (ExcursionDto) o;

        if (id != that.id) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (endingHour != null ? !endingHour.equals(that.endingHour) : that.endingHour != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (startingHour != null ? !startingHour.equals(that.startingHour) : that.startingHour != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (startingHour != null ? startingHour.hashCode() : 0);
        result = 31 * result + (endingHour != null ? endingHour.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
