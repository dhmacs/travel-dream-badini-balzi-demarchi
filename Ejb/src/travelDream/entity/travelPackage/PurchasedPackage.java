package travelDream.entity.travelPackage;

import travelDream.dto.PurchasedPackageDto;
import travelDream.entity.generics.City;
import travelDream.entity.products.PurchasedExcursion;
import travelDream.entity.products.PurchasedHotel;
import travelDream.entity.products.PurchasedTransport;
import travelDream.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Massimo De Marchi
 *         Date: 22/01/14
 *         Time: 14:40
 *
 *         Note: If buyer == owner the package is a normal purchase, else it is a gift package
 */
@Entity
@Table(name = "purchased_package")

@NamedQueries({
        @NamedQuery(
                name = PurchasedPackage.FIND_ALL,
                query = "select p from PurchasedPackage p"
        ),
        @NamedQuery(
                name = PurchasedPackage.FIND_USER_GIFTS,
                query = "select p from PurchasedPackage p " +
                        "where (p.owner.email like :userEmail) and (p.owner.email != p.buyer.email)"
        ),
        @NamedQuery(
        name = PurchasedPackage.FIND_USER_PURCHASES,
        query = "select p from PurchasedPackage p " +
                "where p.buyer.email like :userEmail"
        )
})
public class PurchasedPackage implements Serializable {
    private static final long serialVersionUID = 1L;

    // Queries names
    public static final String FIND_ALL = "PurchasedPackage.findAll";
    public static final String FIND_USER_GIFTS = "PurchasedPackage.findUserGifts";
    public static final String FIND_USER_PURCHASES = "PurchasedPackage.findUserPurchases";

    @Id
    @GeneratedValue
    private int id;

    private String name;
    @Lob
    @Column(length = 3000)
    private String description;
    @ManyToOne
    private City destination;
    private int duration;
    private int numberOfParticipants;

    @OneToOne(cascade = CascadeType.ALL)
    private PurchasedTransport leavingTransport;
    @OneToOne(cascade = CascadeType.ALL)
    private PurchasedTransport returningTransport;
    @OneToOne(cascade = CascadeType.ALL)
    private PurchasedHotel accomodation;
    @OneToOne(cascade = CascadeType.ALL)
    private PurchasedExcursion excursion;


    @ManyToOne
    private User buyer;
    @ManyToOne
    private User owner;

    public PurchasedPackage() {
        super();
    }

    public PurchasedPackage(String name,
                            String description,
                            City destination,
                            int duration,
                            int numberOfParticipants,
                            PurchasedTransport leavingTransport,
                            PurchasedTransport returningTransport,
                            PurchasedHotel accomodation,
                            PurchasedExcursion excursion,
                            User buyer,
                            User owner) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.duration = duration;
        this.numberOfParticipants = numberOfParticipants;
        this.leavingTransport = leavingTransport;
        this.returningTransport = returningTransport;
        this.accomodation = accomodation;
        this.excursion = excursion;
        this.buyer = buyer;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public City getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public PurchasedTransport getLeavingTransport() {
        return leavingTransport;
    }

    public PurchasedTransport getReturningTransport() {
        return returningTransport;
    }

    public PurchasedHotel getAccomodation() {
        return accomodation;
    }

    public PurchasedExcursion getExcursion() {
        return excursion;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getOwner() {
        return owner;
    }

    public PurchasedPackageDto getDto() {
        PurchasedPackageDto purchasedPackageDto = new PurchasedPackageDto();

        purchasedPackageDto.setId(this.getId());
        purchasedPackageDto.setName(this.getName());
        purchasedPackageDto.setDescription(this.getDescription());
        purchasedPackageDto.setDestination(this.getDestination().getDto());
        purchasedPackageDto.setDuration(this.getDuration());
        purchasedPackageDto.setNumberOfParticipants(this.getNumberOfParticipants());
        purchasedPackageDto.setLeavingTransport(this.getLeavingTransport().getDto());
        purchasedPackageDto.setReturningTransport(this.getReturningTransport().getDto());
        purchasedPackageDto.setAccomodation(this.getAccomodation().getDto());

        if (this.getExcursion() != null) {
            purchasedPackageDto.setExcursion(this.getExcursion().getDto());
        }

        purchasedPackageDto.setBuyer(this.getBuyer().getDto());
        purchasedPackageDto.setOwner(this.getOwner().getDto());

        return purchasedPackageDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchasedPackage)) return false;

        PurchasedPackage that = (PurchasedPackage) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
