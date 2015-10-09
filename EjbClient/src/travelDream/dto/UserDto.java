package travelDream.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fede on 26/12/13.
 */
public class UserDto {

    private String email;
    private String firstName;
    private String lastName;
    private Date registeredOn;
    private List<CustomPackageDto> desiredPackages = new ArrayList<CustomPackageDto>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    public List<CustomPackageDto> getDesiredPackages() {
        return desiredPackages;
    }

    public void setDesiredPackages(List<CustomPackageDto> desiredPackages) {
        this.desiredPackages = desiredPackages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        if (desiredPackages != null ? !desiredPackages.equals(userDto.desiredPackages) : userDto.desiredPackages != null)
            return false;
        if (email != null ? !email.equals(userDto.email) : userDto.email != null) return false;
        if (firstName != null ? !firstName.equals(userDto.firstName) : userDto.firstName != null) return false;
        if (lastName != null ? !lastName.equals(userDto.lastName) : userDto.lastName != null) return false;
        if (registeredOn != null ? !registeredOn.equals(userDto.registeredOn) : userDto.registeredOn != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (registeredOn != null ? registeredOn.hashCode() : 0);
        result = 31 * result + (desiredPackages != null ? desiredPackages.hashCode() : 0);
        return result;
    }
}
