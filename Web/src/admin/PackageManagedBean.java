package admin;

import travelDream.bean.PackageInterface;
import travelDream.dto.CityDto;
import travelDream.dto.PackageDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 * Managed bean controlling the management
 * of the packages
 */

@ManagedBean(name ="PackageManagedBean")
@ViewScoped
public class PackageManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private PackageInterface packageInterface;

    private List<PackageDto> packageList;
    private List<CityDto> cityList;

    private PackageDto packageDto = new PackageDto();

    // Page display
    private boolean adding = false;
    private boolean editing = false;

    @PostConstruct
    public void init() {
        this.setPackageList(packageInterface.getAdminPackages());
        this.setCityList(this.packageInterface.getCities());
        this.setAdding(false);
        this.setEditing(false);
    }

    /**
     * Reload the page in creation mode
     */
    public void add() {
        this.setAdding(true);
    }

    /**
     * Create new package entity
     */
    public void create() {
        List<PackageInterface.PackageInputError> packageInputError = this.packageInterface.createPackage(this.getPackageDto());
        if (packageInputError.size() != 0){
            this.communicateErrors(packageInputError);
        }else{
            this.setPackageList(packageInterface.getAdminPackages());
            this.packageDto = new PackageDto();
            this.setAdding(false);
        }
    }


    /**
     * Reload the page in editing mode with the selected item
     * @param packageDto the selected item
     */
    public void edit(PackageDto packageDto) {
        this.packageDto = packageDto;
        this.setEditing(true);
    }

    /**
     * Save changes to the item
     */
    public void save() {
        List<PackageInterface.PackageInputError> packageInputErrors = this.packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() != 0) {
            communicateErrors(packageInputErrors);
        } else {
            this.packageDto = new PackageDto();
            this.setEditing(false);
        }
    }



    /**
     * Come back to the page with the list of excursions
     */
    public void cancel() {
        this.packageDto = new PackageDto();
        this.setAdding(false);
        this.setEditing(false);
        this.setPackageList(packageInterface.getAdminPackages());
    }

    /**
     * Remove the selected item
     * @param packageDto the selected item
     */
    public void delete(PackageDto packageDto) {
        this.packageInterface.deletePackage(packageDto);
        this.setPackageList(packageInterface.getAdminPackages());
    }


    //Getters and Setters


    public List<PackageDto> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageDto> packageList) {
        this.packageList = packageList;
    }

    public PackageDto getPackageDto() {
        return packageDto;
    }

    public void setPackageDto(PackageDto packageDto) {
        this.packageDto = packageDto;
    }

    public List<CityDto> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityDto> cityList) {
        this.cityList = cityList;
    }

    public CityDto getCity(int id) {
        for (CityDto city : cityList) {
            if (city.getId() == id) {
                return city;
            }

        }
        return null;
    }

    public boolean isAdding() {
        return adding;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    private void communicateErrors(List<PackageInterface.PackageInputError> packageInputError) {
        if(packageInputError.contains(PackageInterface.PackageInputError.WRONG_NAME))
            FacesContext.getCurrentInstance().addMessage("createPackage:name",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid name",
                            "invalid name"));

        if(packageInputError.contains(PackageInterface.PackageInputError.MISSING_DESCRIPTION))
            FacesContext.getCurrentInstance().addMessage("createPackage:description",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing description",
                            "missing description"));

        if(packageInputError.contains(PackageInterface.PackageInputError.INVALID_DURATION))
            FacesContext.getCurrentInstance().addMessage("createPackage:duration",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "duration must be positive",
                            "duration must be positive"));

        if(packageInputError.contains(PackageInterface.PackageInputError.INVALID_NUMBER_OF_PARTICIPANTS))
            FacesContext.getCurrentInstance().addMessage("createPackage:numberOfParticipants",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "number of participants must be positive",
                            "number of participants must be positive"));

        if(packageInputError.contains(PackageInterface.PackageInputError.MISSING_DESTINATION))
            FacesContext.getCurrentInstance().addMessage("createPackage:destination",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing destination",
                            "missing destination"));
    }
}
