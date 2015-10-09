package user;

import travelDream.bean.CustomPackageInterface;
import travelDream.bean.PackageInterface;
import travelDream.bean.UserInterface;
import travelDream.dto.CustomPackageDto;
import travelDream.dto.PackageDto;
import travelDream.dto.UserDto;
import travelDream.dto.UserPackageDto;
import travelDream.exception.UserNotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by federico on 06/01/14.
 */
@ManagedBean(name = "UserManagedBean")
@ViewScoped
public class UserManagedBean {

    @EJB
    private UserInterface userInterface;
    @EJB
    private CustomPackageInterface customPackageInterface;
    @EJB
    private PackageInterface packageInterface;

    private List<PackageDto> packageList;
    private List<PackageDto> filteredPackageList;
    private List<UserPackageDto> giftList;
    private String currentUserEmail;
    private UserDto currentUser = new UserDto();

    @PostConstruct
    public void init(){
        this.setPackageList(packageInterface.getUserPackages());
        try {
            this.currentUser = userInterface.getUserByEmail(FacesContext.
                    getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        this.setGiftList(this.customPackageInterface.getUserGiftList(this.currentUser));
    }

    /**
     * delete a gift from user's personal gift list
     * @param giftToDelete the gift to remove
     */
    public void removeFromGiftList(UserPackageDto giftToDelete) {
        this.customPackageInterface.removeFromGiftList((CustomPackageDto)giftToDelete,currentUser);
        this.setGiftList(this.customPackageInterface.getUserGiftList(this.currentUser));
    }

    /**
     * This method check if a gift was already bought or not.
     * @param userPackageDto the gift
     * @return
     */
    public boolean isClassCustomPackage(UserPackageDto userPackageDto) {
        return userPackageDto instanceof CustomPackageDto;
    }

    // Getters and Setters


    public List<UserPackageDto> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<UserPackageDto> giftList) {
        this.giftList = giftList;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public List<PackageDto> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageDto> packageList) {
        this.packageList = packageList;
    }

    public List<PackageDto> getFilteredPackageList() {
        return filteredPackageList;
    }

    public void setFilteredPackageList(List<PackageDto> filteredPackageList) {
        this.filteredPackageList = filteredPackageList;
    }
}
