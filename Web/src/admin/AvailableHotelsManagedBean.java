package admin;

import travelDream.bean.PackageInterface;
import travelDream.dto.HotelDto;
import travelDream.dto.PackageDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed bean responsible of the insertion of hotels into packages.
 */
@ManagedBean(name = "AvailableHotelsManagedBean")
@ViewScoped
public class AvailableHotelsManagedBean {

    @EJB
    private PackageInterface packageInterface;

    private Integer packageId;
    private PackageDto packageDto;
    private List<HotelDto> selectedHotelsFiltered;
    private List<HotelDto> availableHotels;
    private List<HotelDto> availableHotelsFiltered;

    @PostConstruct
    public void init() {
        packageId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().
                getRequestParameterMap().get("packageID"));
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    /**
     * This method deletes an hotel from a package
     *
     * @param hotelDto the hotel to be deleted
     */
    public void delete(HotelDto hotelDto) {
        List<HotelDto> newList = packageDto.getAccomodations();
        newList.remove(hotelDto);
        this.packageDto.setAccomodations(newList);
        List<PackageInterface.PackageInputError> packageInputErrors = packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() > 0) {
            this.communicateErrors(packageInputErrors);
        }
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    /**
     * This method adds an hotel to the package
     *
     * @param hotelDto the hotel to be added
     */
    public void add(HotelDto hotelDto) {
        if (packageDto.getAccomodations() == null) {
            packageDto.setAccomodations(new ArrayList<HotelDto>());
        }
        List<HotelDto> newList = packageDto.getAccomodations();
        newList.add(hotelDto);
        packageDto.setAccomodations(newList);
        List<PackageInterface.PackageInputError> packageInputErrors = packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() > 0) {
            this.communicateErrors(packageInputErrors);
        }
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    private void checkNotTaken() {
        List<HotelDto> hotelDtos = packageInterface.getHotelsByDestination((this.packageDto.getCity()));
        this.availableHotels = new ArrayList<HotelDto>();
        for (HotelDto hotelDto : hotelDtos) {
            availableHotels.add(hotelDto);
        }
        for (int i = 0; availableHotels != null && i < availableHotels.size(); i++) {
            if (packageDto.getAccomodations().contains(availableHotels.get(i))) {
                availableHotels.remove(i);
                i--;
            }
        }
        this.availableHotelsFiltered = new ArrayList<HotelDto>();
        this.availableHotelsFiltered.addAll(availableHotels);
        this.selectedHotelsFiltered = new ArrayList<HotelDto>();
        this.selectedHotelsFiltered.addAll(packageDto.getAccomodations());
    }

    private void communicateErrors(List<PackageInterface.PackageInputError> packageInputErrors) {
        if (packageInputErrors.contains(PackageInterface.PackageInputError.DUPLICATES_ACCOMODATIONS))
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "duplicated accomodation",
                            "duplicated accomodation"));
    }

    /*
    * GETTER AND SETTER
    * */

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public PackageDto getPackageDto() {
        return packageDto;
    }

    public void setPackageDto(PackageDto packageDto) {
        this.packageDto = packageDto;
    }

    public List<HotelDto> getSelectedHotelsFiltered() {
        return selectedHotelsFiltered;
    }

    public void setSelectedHotelsFiltered(List<HotelDto> selectedHotelsFiltered) {
        this.selectedHotelsFiltered = selectedHotelsFiltered;
    }

    public List<HotelDto> getAvailableHotels() {
        return availableHotels;
    }

    public void setAvailableHotels(List<HotelDto> availableHotels) {
        this.availableHotels = availableHotels;
    }

    public List<HotelDto> getAvailableHotelsFiltered() {
        return availableHotelsFiltered;
    }

    public void setAvailableHotelsFiltered(List<HotelDto> availableHotelsFiltered) {
        this.availableHotelsFiltered = availableHotelsFiltered;
    }
}
