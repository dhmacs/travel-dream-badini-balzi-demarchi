package travelDream.dto;

/**
 * Data transfer object of the hotel entity
 */
public class HotelDto {

    //Attributes

    private int id;
    private String name;
    private String description;
    private double price;
    private CityDto cityDto;
    private boolean isActive = true;

    //-----------------------------------------------------------//

    /*
    * GETTERS AND SETTERS
    * */

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

    public CityDto getCityDto() {
        return cityDto;
    }

    public void setCityDto(CityDto cityDto) {
        this.cityDto = cityDto;
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

        HotelDto hotelDto = (HotelDto) o;

        if (id != hotelDto.id) return false;
        if (Double.compare(hotelDto.price, price) != 0) return false;
        if (cityDto != null ? !cityDto.equals(hotelDto.cityDto) : hotelDto.cityDto != null) return false;
        if (description != null ? !description.equals(hotelDto.description) : hotelDto.description != null)
            return false;
        if (name != null ? !name.equals(hotelDto.name) : hotelDto.name != null) return false;

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
        result = 31 * result + (cityDto != null ? cityDto.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
