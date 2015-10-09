package user;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import travelDream.bean.CustomPackageInterface;
import travelDream.bean.PackageValidatorInterface;
import travelDream.bean.UserInterface;
import travelDream.dto.CustomPackageDto;
import travelDream.dto.UserDto;
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

/**
 * Created by federico on 26/01/14.
 */
@ManagedBean(name = "JoinManagedBean")
@ViewScoped
public class JoinManagedBean {

    @EJB
    private CustomPackageInterface customPackageInterface;
    @EJB
    private UserInterface userInterface;
    @EJB
    private PackageValidatorInterface packageValidatorInterface;
    @ManagedProperty(value = "#{MailDispatcher}")
    private MailDispatcher mailDispatcher;

    private CustomPackageDto customPackageDto;
    private boolean packageFound;
    private boolean joined;

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().
                getRequestParameterMap().get("customPackageID") != null) {
            Integer customPackageId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().
                    getRequestParameterMap().get("customPackageID"));

            this.customPackageDto = customPackageInterface.getCustomPackageById(customPackageId);
            if (this.customPackageDto == null) {

                this.packageFound = false;

            } else {
                if (!packageValidatorInterface.isValid(customPackageDto)) {
                    this.packageFound = false;
                } else {
                    this.packageFound = true;
                    if (FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() != null) {
                        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("USER")) {
                            if (!FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("user"))
                                try {
                                    FacesContext.getCurrentInstance().getExternalContext().redirect("/WebWeb/user/joinSharedPackage.xhtml?" +
                                            "customPackageID=" + customPackageId);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                }
            }
        } else {
            this.packageFound = false;
        }
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
        this.setJoined(true);
        UserDto currentUser = new UserDto();
        try {
            if (packageValidatorInterface.isValid(customPackageDto)) {
                currentUser = userInterface.getUserByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
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

    public CustomPackageDto getCustomPackageDto() {
        return customPackageDto;
    }

    public void setCustomPackageDto(CustomPackageDto customPackageDto) {
        this.customPackageDto = customPackageDto;
    }

    public boolean isPackageFound() {
        return packageFound;
    }

    public void setPackageFound(boolean packageFound) {
        this.packageFound = packageFound;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public void setMailDispatcher(MailDispatcher mailDispatcher) {
        this.mailDispatcher = mailDispatcher;
    }
}
