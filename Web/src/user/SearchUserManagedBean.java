package user;

import travelDream.bean.CustomPackageInterface;
import travelDream.bean.PackageValidatorInterface;
import travelDream.bean.UserInterface;
import travelDream.dto.CustomPackageDto;
import travelDream.dto.UserDto;
import travelDream.dto.UserPackageDto;
import travelDream.exception.UserNotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alessandrobalzi on 12/01/14.
 */

@ManagedBean(name = "SearchUserManagedBean")
@ViewScoped
public class SearchUserManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private UserInterface userInterface;
    @EJB
    private CustomPackageInterface customPackageInterface;
    @EJB
    private PackageValidatorInterface packageValidatorInterface;

    private String searchedUserEmail;
    private UserDto searchedUser;
    private List<UserPackageDto> giftList;
    private boolean searchingUser;

    @PostConstruct
    public void init() {
        this.searchedUser = new UserDto();
    }

    /**
     * Search an user by email.
     */
    public void searchUser() {
        try {
            this.searchedUser = userInterface.getUserByEmail(searchedUserEmail);
            this.setGiftList(this.customPackageInterface.getUserGiftList(searchedUser));
            UserDto searcher = userInterface.getUserByEmail(FacesContext.getCurrentInstance()
                    .getExternalContext().getRemoteUser());
            if (!this.searchedUser.equals(searcher)) {
                this.searchingUser = true;
            }
        } catch (UserNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR: ",
                            e.getMessage()));
        }
    }

    /**
     * Purchase a gift from a friend's gift list
     *
     * @param giftToPurchase the gift to purchase
     */
    public void giveGift(UserPackageDto giftToPurchase) {
        UserDto currentUser = new UserDto();
        try {
            currentUser = userInterface.getUserByEmail(FacesContext.
                    getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        if (packageValidatorInterface.isValid((CustomPackageDto) giftToPurchase)) {
            this.customPackageInterface.purchase((CustomPackageDto) giftToPurchase, currentUser, this.searchedUser);
            this.setGiftList(this.customPackageInterface.getUserGiftList(searchedUser));
        } else {
            try {
                customPackageInterface.removeFromGiftList((CustomPackageDto)giftToPurchase,searchedUser);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/WebWeb/user/packageNotAvailable.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method check if a gift was already bought or not.
     *
     * @param userPackageDto the gift
     * @return
     */
    public boolean isClassCustomPackage(UserPackageDto userPackageDto) {
        return userPackageDto instanceof CustomPackageDto;
    }

    // Getters and Setters

    public String getSearchedUserEmail() {
        return searchedUserEmail;
    }

    public void setSearchedUserEmail(String searchedUserEmail) {
        this.searchedUserEmail = searchedUserEmail;
    }

    public boolean isSearchingUser() {
        return searchingUser;
    }

    public void setSearchingUser(boolean searchingUser) {
        this.searchingUser = searchingUser;
    }

    public UserDto getSearchedUser() {
        return searchedUser;
    }

    public void setSearchedUser(UserDto searchedUser) {
        this.searchedUser = searchedUser;
    }

    public List<UserPackageDto> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<UserPackageDto> giftList) {
        this.giftList = giftList;
    }
}

