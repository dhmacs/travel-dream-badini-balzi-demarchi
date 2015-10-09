package travelDream.dto;

/**
 * @author Massimo De Marchi
 *         Date: 08/01/14
 *         Time: 18:58
 */
public class CityDto {
    private int id;

    private String name;
    private String nation;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityDto cityDto = (CityDto) o;

        if (id != cityDto.id) return false;
        if (name != null ? !name.equals(cityDto.name) : cityDto.name != null) return false;
        if (nation != null ? !nation.equals(cityDto.nation) : cityDto.nation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nation != null ? nation.hashCode() : 0);
        return result;
    }
}
