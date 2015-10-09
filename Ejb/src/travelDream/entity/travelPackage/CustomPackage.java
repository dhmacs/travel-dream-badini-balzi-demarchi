package travelDream.entity.travelPackage;

import travelDream.dto.CustomPackageDto;
import travelDream.dto.FlightDto;
import travelDream.dto.PackageDto;
import travelDream.entity.generics.City;
import travelDream.entity.products.Excursion;
import travelDream.entity.products.Flight;
import travelDream.entity.products.Hotel;
import travelDream.entity.products.Transport;
import travelDream.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by federico on 19/01/14.
 */
@Entity
@Table(name = "custom_package")
@NamedQueries({
        @NamedQuery(
                name = CustomPackage.FIND_ALL,
                query = "select p from CustomPackage p"
        ),
        @NamedQuery(
                name = CustomPackage.FIND_BY_ID,
                query = "select p from CustomPackage p where p.id = :id"
        )
})
public class CustomPackage implements Serializable{

    private static final long serialVersionUID = 1L;

    //Query names

    public static final String FIND_ALL = "CustomPackage.findAll";
    public static final String FIND_BY_ID = "CustomPackage.findById";

    //-----------------------------------------------------------//

    //Attributes
    @GeneratedValue
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Package parentPackage;

    @ManyToOne
    @JoinColumn(name = "leaving_transport_id", nullable = true)
    private Transport leavingTransport;
    @ManyToOne
    @JoinColumn(name = "returning_transport_id", nullable = true)
    private Transport returningTransport;
    @ManyToOne
    @JoinColumn(name = "accomodation_id", nullable = true)
    private Hotel accomodation;
    @ManyToOne
    @JoinColumn(name = "excursion_id", nullable = true)
    private Excursion excursion;

    // I do not set this flag from dto
    private boolean isAvailable = true;



    public CustomPackage() {
        super();
    }

    public CustomPackage(CustomPackageDto customPackageDto) {
        this.setId(customPackageDto.getId());
        this.setParentPackage(new Package(customPackageDto.getParentPackage()));

        if (customPackageDto.getLeavingTransport() instanceof FlightDto) {
            this.setLeavingTransport(new Flight((FlightDto)customPackageDto.getLeavingTransport()));
        }

        if (customPackageDto.getReturningTransport() instanceof FlightDto) {
            this.setReturningTransport(new Flight((FlightDto) customPackageDto.getReturningTransport()));
        }

        this.setAccomodation(new Hotel(customPackageDto.getAccomodation()));
        if (customPackageDto.getExcursion()!=null) {
            this.setExcursion(new Excursion(customPackageDto.getExcursion()));
        }
    }

    // TODO: Check that selected items are in parent package either
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Package getParentPackage() {
        return parentPackage;
    }

    public void setParentPackage(Package parentPackage) {
        this.parentPackage = parentPackage;
    }

    public Transport getLeavingTransport() {
        return leavingTransport;
    }

    public void setLeavingTransport(Transport leavingTransport) {
        this.leavingTransport = leavingTransport;
    }

    public Transport getReturningTransport() {
        return returningTransport;
    }

    public void setReturningTransport(Transport returningTransport) {
        this.returningTransport = returningTransport;
    }

    public Hotel getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(Hotel accomodation) {
        this.accomodation = accomodation;
    }

    public Excursion getExcursion() {
        return excursion;
    }

    public void setExcursion(Excursion excursion) {
        this.excursion = excursion;
    }

    public CustomPackageDto getDto() {
        CustomPackageDto customPackageDto = new CustomPackageDto();
        customPackageDto.setId(this.id);
        customPackageDto.setParentPackage(this.parentPackage.getDto());
        customPackageDto.setLeavingTransport(this.leavingTransport.getDto());
        customPackageDto.setReturningTransport(this.returningTransport.getDto());
        customPackageDto.setAccomodation(this.accomodation.getDto());
        if (excursion != null) {
            customPackageDto.setExcursion(this.excursion.getDto());
        }
        return customPackageDto;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomPackage)) return false;

        CustomPackage that = (CustomPackage) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
