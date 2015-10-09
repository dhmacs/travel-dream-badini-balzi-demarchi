package admin;

import travelDream.bean.PackageInterface;
import travelDream.dto.FlightDto;
import travelDream.dto.PackageDto;
import travelDream.dto.TransportDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed bean responsible of the insertion of leaving transports into packages.
 */
@ManagedBean(name = "LeavingTransportManagedBean")
@ViewScoped
public class LeavingTransportManagedBean {

    @EJB
    private PackageInterface packageInterface;

    private Integer packageId;
    private PackageDto packageDto;
    private List<FlightDto> selectedFlightsFiltered;
    private List<FlightDto> availableFlights;
    private List<FlightDto> availableFlightsFiltered;

    @PostConstruct
    public void init() {
        packageId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().
                getRequestParameterMap().get("packageID"));
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    /**
     * This method deletes a transport from a package
     *
     * @param transportDto the transport to be deleted
     */
    public void delete(TransportDto transportDto) {
        List<TransportDto> newList = packageDto.getLeavingTransports();
        newList.remove(transportDto);
        this.packageDto.setLeavingTransports(newList);
        List<PackageInterface.PackageInputError> packageInputErrors = packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() > 0) {
            this.communicateErrors(packageInputErrors);
        }
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    /**
     * This method adds a transport to the package
     *
     * @param transportDto the transport to be added
     */
    public void add(TransportDto transportDto) {
        if (packageDto.getLeavingTransports() == null) {
            packageDto.setLeavingTransports(new ArrayList<TransportDto>());
        }
        List<TransportDto> newList = packageDto.getLeavingTransports();
        newList.add(transportDto);
        packageDto.setLeavingTransports(newList);
        List<PackageInterface.PackageInputError> packageInputErrors = packageInterface.updatePackage(packageDto);
        if (packageInputErrors.size() > 0) {
            this.communicateErrors(packageInputErrors);
        }
        this.packageDto = packageInterface.getPackageByID(packageId);
        this.checkNotTaken();
    }

    private void checkNotTaken() {
        List<TransportDto> transportDtos = packageInterface.getTransportWithArrival((this.packageDto.getCity()));
        this.availableFlights = new ArrayList<FlightDto>();
        for (TransportDto transportDto : transportDtos) {
            if (transportDto instanceof FlightDto) {
                availableFlights.add((FlightDto) transportDto);
            }
        }
        for (int i = 0; availableFlights != null && i < availableFlights.size(); i++) {
            if (packageDto.getLeavingTransports().contains(availableFlights.get(i))) {
                availableFlights.remove(i);
                i--;
            }
        }
        this.availableFlightsFiltered = new ArrayList<FlightDto>();
        this.availableFlightsFiltered.addAll(availableFlights);
        this.selectedFlightsFiltered = new ArrayList<FlightDto>();
        this.selectedFlightsFiltered.addAll(packageDto.getLeavingFlights());
    }

    private void communicateErrors(List<PackageInterface.PackageInputError> packageInputErrors) {
        if (packageInputErrors.contains(PackageInterface.PackageInputError.DUPLICATES_LEAVING))
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "duplicated leaving",
                            "duplicated leaving transports"));
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

    public List<FlightDto> getAvailableFlights() {
        return availableFlights;
    }

    public void setAvailableFlights(List<FlightDto> availableFlights) {
        this.availableFlights = availableFlights;
    }

    public List<FlightDto> getAvailableFlightsFiltered() {
        return availableFlightsFiltered;
    }

    public void setAvailableFlightsFiltered(List<FlightDto> availableFlightsFiltered) {
        this.availableFlightsFiltered = availableFlightsFiltered;
    }

    public List<FlightDto> getSelectedFlightsFiltered() {
        return selectedFlightsFiltered;
    }

    public void setSelectedFlightsFiltered(List<FlightDto> selectedFlightsFiltered) {
        this.selectedFlightsFiltered = selectedFlightsFiltered;
    }
}
