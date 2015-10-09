package travelDream.bean;

import travelDream.dto.CustomPackageDto;
import travelDream.dto.PurchasedPackageDto;
import travelDream.dto.UserDto;
import travelDream.dto.UserPackageDto;
import travelDream.entity.generics.City;
import travelDream.entity.products.*;
import travelDream.entity.travelPackage.CustomPackage;
import travelDream.entity.travelPackage.PurchasedPackage;
import travelDream.entity.user.User;
import travelDream.exception.user.travelPackage.InconsistentPackageException;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by alessandrobalzi on 21/01/14.
 */

@Stateless
public class CustomPackageBean implements CustomPackageInterface {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CustomPackageInputError> addToGiftList(CustomPackageDto customPackageDto, UserDto owner) {
        User userOwner = this.entityManager.find(User.class, owner.getEmail());
        userOwner.getDesiredPackages().add(new CustomPackage(customPackageDto));
        this.entityManager.merge(userOwner);
        return new ArrayList<CustomPackageInputError>();
    }

    @Override
    public List<CustomPackageInputError> removeFromGiftList(CustomPackageDto customPackageDto, UserDto owner) {
        User userOwner = this.entityManager.find(User.class, owner.getEmail());
        CustomPackage packageToBeRemoved = new CustomPackage(customPackageDto);

        if (userOwner.getDesiredPackages().contains(packageToBeRemoved)) {
            userOwner.getDesiredPackages().remove(packageToBeRemoved);
            this.entityManager.merge(userOwner);
        } else {
            // TODO: return the error
        }
        return new ArrayList<CustomPackageInputError>();
    }

    /**
     * Share the package, associating it with its sharer
     *
     * @param customPackageDto
     * @param sharer
     * @return the ID of the shared package
     */
    @Override
    public int share(CustomPackageDto customPackageDto, UserDto sharer) throws InconsistentPackageException {
        User userSharer = this.entityManager.find(User.class, sharer.getEmail());
        CustomPackage customPackage = new CustomPackage(customPackageDto);

        if (customPackage.getId() != 0) {
            customPackage = this.entityManager.merge(customPackage);
        } else {
            this.entityManager.persist(customPackage);
        }
        this.entityManager.flush();

        return customPackage.getId();

    }

    /**
     * Purchase a package associating it with its buyer
     *
     * @param customPackageDto
     * @param buyer
     * @return
     */
    @Override
    public List<CustomPackageInputError> purchase(CustomPackageDto customPackageDto, UserDto buyer, UserDto receiver) {
        User userBuyer = this.entityManager.find(User.class, buyer.getEmail());
        User userReceiver = this.entityManager.find(User.class, receiver.getEmail());
        PurchasedTransport leavingTransport = null;
        PurchasedTransport returningTransport = null;
        PurchasedExcursion excursion = null;
        PurchasedHotel accomodation = null;

        if (customPackageDto.getLeavingFlight() != null) {
            leavingTransport = new PurchasedFlight(new Flight(customPackageDto.getLeavingFlight()));
        }
        if (customPackageDto.getReturningFlight() != null) {
            returningTransport = new PurchasedFlight(new Flight(customPackageDto.getReturningFlight()));
        }
        if (customPackageDto.getAccomodation() != null) {
            accomodation = new PurchasedHotel(new Hotel(customPackageDto.getAccomodation()));
        }
        if (customPackageDto.getExcursion() != null) {
            excursion = new PurchasedExcursion(new Excursion(customPackageDto.getExcursion()));
        }


        PurchasedPackage purchasedPackage =
                new PurchasedPackage(
                        customPackageDto.getName(),
                        customPackageDto.getDescription(),
                        new City(customPackageDto.getCity()),
                        customPackageDto.getDuration(),
                        customPackageDto.getNumberOfParticipants(),
                        leavingTransport,
                        returningTransport,
                        accomodation,
                        excursion,
                        userBuyer,
                        userReceiver);

        this.entityManager.persist(purchasedPackage);

        if (!buyer.getEmail().equals(receiver.getEmail()))  {
            try {
                Object o = this.entityManager.createNamedQuery(CustomPackage.FIND_BY_ID)
                        .setParameter("id", customPackageDto.getId())
                        .getSingleResult();
                if (o instanceof CustomPackage) {
                    userReceiver.getDesiredPackages().remove(o);
                    this.entityManager.merge(userReceiver);
                }
            } catch (NoResultException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<CustomPackageInputError>();
    }

    @Override
    // I do not return non available package
    public List<UserPackageDto> getUserGiftList(UserDto user) {
        List<UserPackageDto> giftList = new ArrayList<UserPackageDto>();

        List receivedGifts =
                this.entityManager.createNamedQuery(PurchasedPackage.FIND_USER_GIFTS)
                        .setParameter("userEmail", user.getEmail()).getResultList();

        User selectedUser = this.entityManager.find(User.class, user.getEmail());

        for (CustomPackage customPackage : selectedUser.getDesiredPackages()) {
            if (this.isPackageAvailable(customPackage.getId())) {
                giftList.add(customPackage.getDto());
            }
        }

        for (Object o: receivedGifts) {
            if (o instanceof PurchasedPackage) {
                giftList.add(((PurchasedPackage) o).getDto());
            }
        }

        return giftList;
    }

    @Override
    public List<PurchasedPackageDto> getPurchased(UserDto user) {
        User selectedUser = this.entityManager.find(User.class, user.getEmail());
        List<PurchasedPackageDto> purchasedPackageDtos = new ArrayList<PurchasedPackageDto>();

        List results = this.entityManager.createNamedQuery(PurchasedPackage.FIND_USER_PURCHASES)
                .setParameter("userEmail", selectedUser.getEmail()).getResultList();


        for (Object o : results) {
            if (o instanceof PurchasedPackage) {
                purchasedPackageDtos.add(((PurchasedPackage) o).getDto());
            }
        }

        return purchasedPackageDtos;
    }

    @Override
    public CustomPackageDto getCustomPackageById(int id) throws IllegalArgumentException{
        CustomPackage customPackage = entityManager.find(CustomPackage.class, id);
        if(customPackage == null || !this.isPackageAvailable(customPackage.getId()))
            return null;
        return customPackage.getDto();
    }

    private boolean isPackageAvailable(int customPackageId) {
        CustomPackage customPackage = this.entityManager.find(CustomPackage.class, customPackageId);

        return customPackage.getParentPackage().isActive() &&
                customPackage.getLeavingTransport().isActive() &&
                customPackage.getReturningTransport().isActive() &&
                customPackage.getAccomodation().isActive() &&
                (customPackage.getExcursion() == null || customPackage.getExcursion().isActive());
    }
}
