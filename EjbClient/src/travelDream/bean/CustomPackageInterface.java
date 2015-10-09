package travelDream.bean;

import travelDream.dto.CustomPackageDto;
import travelDream.dto.PurchasedPackageDto;
import travelDream.dto.UserDto;
import travelDream.dto.UserPackageDto;
import travelDream.exception.user.travelPackage.InconsistentPackageException;

import java.util.List;

/**
 * @author Massimo De Marchi
 *         Date: 21/01/14
 *         Time: 14:02
 */
public interface CustomPackageInterface {

    public enum CustomPackageInputError {

    }

    /**
     * Add the package to the "owner" gift list
     * @param customPackageDto
     * @param owner
     * @return
     */
    public List<CustomPackageInputError> addToGiftList(CustomPackageDto customPackageDto, UserDto owner);

    /**
     * Add the package to the "owner" gift list
     * @param customPackageDto
     * @param owner
     * @return
     */
    public List<CustomPackageInputError> removeFromGiftList(CustomPackageDto customPackageDto, UserDto owner);

    /**
     * Share the package, associating it with its sharer
     * @param customPackageDto
     * @param sharer
     * @return the ID of the shared package
     */
    public int share(CustomPackageDto customPackageDto, UserDto sharer) throws InconsistentPackageException;

    /**
     * Purchase a package associating it with its buyer
     * @param customPackageDto
     * @param buyer
     * @return
     */
    public List<CustomPackageInputError> purchase(CustomPackageDto customPackageDto, UserDto buyer, UserDto receiver);

    /**
     * Get "user" gift list
     * @param user
     * @return
     */
    public List<UserPackageDto> getUserGiftList(UserDto user);

    /**
     * Get "user" purchased packages
     * @param user
     * @return
     */
    public List<PurchasedPackageDto> getPurchased(UserDto user);

    /**
     * Get a custom package by ID
     * @param id
     * @return
     */
    public CustomPackageDto getCustomPackageById(int id);

}
