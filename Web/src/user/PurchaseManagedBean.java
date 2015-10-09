package user;

import travelDream.bean.CustomPackageInterface;
import travelDream.bean.PackageInterface;
import travelDream.bean.PackageValidatorInterface;
import travelDream.bean.UserInterface;
import travelDream.dto.*;
import travelDream.exception.UserNotFoundException;
import travelDream.exception.user.travelPackage.InconsistentPackageException;
import util.MailDispatcher;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by federico on 19/01/14.
 */
@ManagedBean(name = "PurchaseManagedBean")
@ViewScoped
public class PurchaseManagedBean {

    @EJB
    private PackageInterface packageInterface;
    @EJB
    private UserInterface userInterface;
    @EJB
    private CustomPackageInterface customPackageInterface;
    @EJB
    private PackageValidatorInterface packageValidatorInterface;
    @ManagedProperty(value = "#{MailDispatcher}")
    private MailDispatcher mailDispatcher;

    private PackageDto packageDto = new PackageDto();
    private CustomPackageDto customPackageDto = new CustomPackageDto();
    private List<FlightDto> filteredLeavingTransports = new ArrayList<FlightDto>();
    private List<FlightDto> filteredReturningTransports = new ArrayList<FlightDto>();
    private List<HotelDto> filteredAccommodations = new ArrayList<HotelDto>();
    private List<ExcursionDto> filteredExcursions = new ArrayList<ExcursionDto>();
    private boolean summary;
    private boolean purchased;
    private boolean shared;

    @PostConstruct
    public void init() {
        Integer id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("packageID"));
        this.packageDto = packageInterface.getPackageByID(id);
        if (this.packageDto == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/WebWeb/user/packageNotAvailable.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            this.customPackageDto.setParentPackage(this.packageDto);
            for (TransportDto leavingTransport : this.packageInterface.getFilteredLeavingTransports(this.packageDto)) {
                if (leavingTransport instanceof FlightDto) {
                    this.filteredLeavingTransports.add((FlightDto) leavingTransport);
                }
            }
            setFilteredAccommodations(this.packageInterface.getFilteredAccomodations(this.packageDto));
            setFilteredExcursions(this.packageInterface.getFilteredExcursions(this.packageDto));
        }
    }

    public void addLeavingTransport(TransportDto transportDto) {
        customPackageDto.setLeavingTransport(transportDto);
        for (TransportDto returnTransport : this.packageInterface.getFilteredReturnTransports(this.packageDto, transportDto)) {
            if (returnTransport instanceof FlightDto) {
                if (this.filteredReturningTransports.size() > 0) {
                    this.filteredReturningTransports.clear();
                }
                this.filteredReturningTransports.add((FlightDto) returnTransport);
            }
        }
        this.customPackageDto.setReturningTransport(null);
        this.customPackageDto.setAccomodation(null);
        this.customPackageDto.setExcursion(null);
    }

    public void addReturningTransport(TransportDto transportDto) {
        customPackageDto.setReturningTransport(transportDto);
        this.customPackageDto.setAccomodation(null);
        this.customPackageDto.setExcursion(null);
    }

    public void addAccommodation(HotelDto hotelDto) {
        customPackageDto.setAccomodation(hotelDto);
        this.customPackageDto.setExcursion(null);
        this.setSummary(false);
    }

    public void addExcursion(ExcursionDto excursionDto) {
        customPackageDto.setExcursion(excursionDto);
        showSummary();
    }

    public double calculatePrice() {
        if (this.customPackageDto.getExcursion() != null) {
            return this.customPackageDto.getLeavingTransport().getPrice() +
                    this.customPackageDto.getReturningTransport().getPrice() +
                    this.customPackageDto.getAccomodation().getPrice() +
                    this.customPackageDto.getExcursion().getPrice();
        }
        return this.customPackageDto.getLeavingTransport().getPrice() +
                this.customPackageDto.getReturningTransport().getPrice() +
                this.customPackageDto.getAccomodation().getPrice();
    }

    public void showSummary() {
        if (this.customPackageDto.getLeavingFlight() != null && this.customPackageDto.getReturningFlight() != null
                && this.customPackageDto.getAccomodation() != null) {
            this.setSummary(true);
            this.setShared(false);
            this.setPurchased(false);
        }
    }

    /**
     * add a custom package to the gift list of the user
     *
     * @return redirect to the personal page of the user
     */
    public String addToGiftList() {
        UserDto currentUser = new UserDto();
        try {
            currentUser = userInterface.getUserByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        if (packageValidatorInterface.isValid(customPackageDto)) {
            this.customPackageInterface.addToGiftList(this.customPackageDto, currentUser);
        } else {
            return "packageNotFound?faces-redirect=true";
        }
        return "personalPage?faces-redirect=true";
    }

    public void share(String emailAddress) {
        this.setSummary(false);
        this.setShared(true);
        this.setPurchased(false);
        UserDto currentUser = new UserDto();
        try {
            currentUser = userInterface.getUserByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (packageValidatorInterface.isValid(customPackageDto)) {
                customPackageDto.setId(this.customPackageInterface.share(this.customPackageDto, currentUser));
                mailDispatcher.sendSharingEmail(emailAddress, customPackageDto.getId());
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/WebWeb/user/packageNotAvailable.xhtml");
            }
        } catch (InconsistentPackageException e) {
            // TODO: manage exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void purchase() {
        this.setSummary(false);
        this.setShared(false);
        this.setPurchased(true);
        UserDto currentUser = new UserDto();
        try {
            currentUser = userInterface.getUserByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
            if (packageValidatorInterface.isValid(customPackageDto)) {
                this.customPackageInterface.purchase(this.customPackageDto, currentUser, currentUser);
                mailDispatcher.sendPurchaseEmail(currentUser.getEmail(), customPackageDto);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/WebWeb/user/packageNotAvailable.xhtml");
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters

    public PackageDto getPackageDto() {
        return packageDto;
    }

    public void setPackageDto(PackageDto packageDto) {
        this.packageDto = packageDto;
    }

    public CustomPackageDto getCustomPackageDto() {
        return customPackageDto;
    }

    public void setCustomPackageDto(CustomPackageDto customPackageDto) {
        this.customPackageDto = customPackageDto;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public List<FlightDto> getFilteredReturningTransports() {
        return filteredReturningTransports;
    }

    public void setFilteredReturningTransports(List<FlightDto> filteredReturningTransports) {
        this.filteredReturningTransports = filteredReturningTransports;
    }

    public List<FlightDto> getFilteredLeavingTransports() {
        return filteredLeavingTransports;
    }

    public void setFilteredLeavingTransports(List<FlightDto> filteredLeavingTransports) {
        this.filteredLeavingTransports = filteredLeavingTransports;
    }

    public List<HotelDto> getFilteredAccommodations() {
        return filteredAccommodations;
    }

    public void setFilteredAccommodations(List<HotelDto> filteredAccommodations) {
        this.filteredAccommodations = filteredAccommodations;
    }

    public List<ExcursionDto> getFilteredExcursions() {
        return filteredExcursions;
    }

    public void setFilteredExcursions(List<ExcursionDto> filteredExcursions) {
        this.filteredExcursions = filteredExcursions;
    }

    public void setMailDispatcher(MailDispatcher mailDispatcher) {
        this.mailDispatcher = mailDispatcher;
    }
}
