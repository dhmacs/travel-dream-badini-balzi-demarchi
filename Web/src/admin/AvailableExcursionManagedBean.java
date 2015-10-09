package admin;

import travelDream.bean.PackageInterface;
import travelDream.dto.ExcursionDto;
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
 * Managed bean responsible of the insertion of excursions into packages.
 */
@ManagedBean(name = "AvailableExcursionManagedBean")
@ViewScoped
public class AvailableExcursionManagedBean {

    @EJB
    private PackageInterface packageInterface;

    private Integer packageId;
    private PackageDto packageDto;
    private List<ExcursionDto> selectedExcursionsFiltered;
    private List<ExcursionDto> availableExcursions;
    private List<ExcursionDto> availableExcursionsFiltered;

    @PostConstruct
    public void init() {
        packageId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().
                getRequestParameterMap().get("packageID"));
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    /**
     * This method deletes an excursion from a package
     *
     * @param excursionDto the excursion to be deleted
     */
    public void delete(ExcursionDto excursionDto) {
        List<ExcursionDto> newList = packageDto.getExcursions();
        newList.remove(excursionDto);
        this.packageDto.setExcursions(newList);

        List<PackageInterface.PackageInputError> packageInputErrors = packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() > 0) {
            this.communicateErrors(packageInputErrors);
        }
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    /**
     * This method adds an excursion to the package
     *
     * @param excursionDto the excursion to be added
     */
    public void add(ExcursionDto excursionDto) {
        if (packageDto.getExcursions() == null) {
            packageDto.setExcursions(new ArrayList<ExcursionDto>());
        }
        List<ExcursionDto> newList = packageDto.getExcursions();
        newList.add(excursionDto);
        packageDto.setExcursions(newList);
        List<PackageInterface.PackageInputError> packageInputErrors = packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() > 0) {
            this.communicateErrors(packageInputErrors);
        }
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    private void checkNotTaken() {
        List<ExcursionDto> excursionDtos = packageInterface.getExcursionsByDestination(this.packageDto.getCity());
        this.availableExcursions = new ArrayList<ExcursionDto>();
        for (ExcursionDto excursionDto : excursionDtos) {
            availableExcursions.add(excursionDto);
        }
        for (int i = 0; availableExcursions != null && i < availableExcursions.size(); i++) {
            if (packageDto.getExcursions().contains(availableExcursions.get(i))) {
                availableExcursions.remove(i);
                i--;
            }
        }
        this.availableExcursionsFiltered = new ArrayList<ExcursionDto>();
        this.availableExcursionsFiltered.addAll(availableExcursions);
        this.selectedExcursionsFiltered = new ArrayList<ExcursionDto>();
        this.selectedExcursionsFiltered.addAll(packageDto.getExcursions());
    }

    private void communicateErrors(List<PackageInterface.PackageInputError> packageInputErrors) {
        if (packageInputErrors.contains(PackageInterface.PackageInputError.DUPLICATES_EXCURSIONS))
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "duplicated excursion",
                            "duplicated excursion"));
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

    public List<ExcursionDto> getSelectedExcursionsFiltered() {
        return selectedExcursionsFiltered;
    }

    public void setSelectedExcursionsFiltered(List<ExcursionDto> selectedExcursionsFiltered) {
        this.selectedExcursionsFiltered = selectedExcursionsFiltered;
    }

    public List<ExcursionDto> getAvailableExcursions() {
        return availableExcursions;
    }

    public void setAvailableExcursions(List<ExcursionDto> availableExcursions) {
        this.availableExcursions = availableExcursions;
    }

    public List<ExcursionDto> getAvailableExcursionsFiltered() {
        return availableExcursionsFiltered;
    }

    public void setAvailableExcursionsFiltered(List<ExcursionDto> availableExcursionsFiltered) {
        this.availableExcursionsFiltered = availableExcursionsFiltered;
    }
}
