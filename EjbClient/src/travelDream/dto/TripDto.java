package travelDream.dto;

/**
 * Created by alessandrobalzi on 10/01/14.
 */
public class TripDto {

    //Attributes

    private PackageDto packageDto;
    private ExcursionDto excursion;
    private boolean isActive = true;

    //Getters and Setters


    public PackageDto getPackageDto() {
        return packageDto;
    }

    public void setPackageDto(PackageDto packageDto) {
        this.packageDto = packageDto;
    }

    public ExcursionDto getExcursion() {
        return excursion;
    }

    public void setExcursion(ExcursionDto excursion) {
        this.excursion = excursion;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
