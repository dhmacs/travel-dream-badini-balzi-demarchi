package travelDream.entity.travelPackage;

import travelDream.dto.*;
import travelDream.entity.generics.City;
import travelDream.entity.products.Excursion;
import travelDream.entity.products.Flight;
import travelDream.entity.products.Hotel;
import travelDream.entity.products.Transport;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class representing all the packages
 * registered in travelDream database
 */
@Entity
//Named queries for the entity Package
@Table(name = "travel_package")
@NamedQuery(
        name = Package.FIND_ALL,
        query = "select p from Package p"
)

public class Package implements Serializable {

    private static final long serialVersionUID = 1L;

    //Query names

    public static final String FIND_ALL = "Package.findAll";


    //-----------------------------------------------------------//

    //Attributes

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="leaving_transports",
            joinColumns={@JoinColumn(name="package_id", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="transport_id", referencedColumnName="ID")})
    private List<Transport> leavingTrasports = new ArrayList<Transport>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="returning_transports",
            joinColumns={@JoinColumn(name="package_id", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="transport_id", referencedColumnName="ID")})
    private List<Transport> returningTransports = new ArrayList<Transport>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="accomodations",
            joinColumns={@JoinColumn(name="package_id", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="hotel_id", referencedColumnName="ID")})
    private List<Hotel> accomodations = new ArrayList<Hotel>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="trip",
            joinColumns={@JoinColumn(name="package_id", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="excursion_id", referencedColumnName="ID")})
    private List<Excursion> excursions = new ArrayList<Excursion>();

    private boolean isActive = true;


    //-----------------------------------------------------------//

    //Constructors

    public Package() {
        super();
    }

    /**
     * Initialize a package from a package dto
     * @param packageDto
     */
    public Package(PackageDto packageDto) {
        this.setId(packageDto.getId());     // TODO: Check if any issues could arise here (Possible duplicated keywords)
        this.setName(packageDto.getName());
        this.setDescription(packageDto.getDescription());
        this.setDestination(new City(packageDto.getCity()));
        this.setDuration(packageDto.getDuration());
        this.setNumberOfParticipants(packageDto.getNumberOfParticipants());
        this.setActive(packageDto.isActive());

        // Add leaving transports
        for (TransportDto transportDto: packageDto.getLeavingTransports()) {
            if (transportDto instanceof FlightDto) {
                this.getLeavingTrasports().add(new Flight((FlightDto)transportDto));
            }
        }

        // Add returning transports
        for (TransportDto transportDto: packageDto.getReturningTransports()) {
            if (transportDto instanceof FlightDto) {
                this.getReturningTransports().add(new Flight((FlightDto)transportDto));
            }
        }

        // Add accomodations
        for (HotelDto hotelDto: packageDto.getAccomodations()) {
            this.getAccomodations().add(new Hotel(hotelDto));
        }

        // Add excursions
        for (ExcursionDto excursionDto: packageDto.getExcursions()) {
            this.getExcursions().add(new Excursion(excursionDto));
        }
    }

    //-----------------------------------------------------------//

    //Getters and Setters

    /**
     * Method returning the Dto representation of the entity
     * @return Dto representing the entity
     */
    public PackageDto getDto() {
        PackageDto packageDto = new PackageDto();

        packageDto.setId(this.getId());
        packageDto.setName(this.getName());
        packageDto.setDescription(this.getDescription());
        packageDto.setCity(this.getDestination().getDto());
        packageDto.setDuration(this.getDuration());
        packageDto.setNumberOfParticipants(this.getNumberOfParticipants());
        packageDto.setActive(this.isActive());

        // Add leaving transports dtos
        for (Transport transport: this.getLeavingTrasports()) {
            packageDto.getLeavingTransports().add(transport.getDto());
        }

        // Add return transports dtos
        for (Transport transport: this.getReturningTransports()) {
            packageDto.getReturningTransports().add(transport.getDto());
        }

        // Add accomodations dtos
        for (Hotel hotel: this.getAccomodations()) {
            packageDto.getAccomodations().add(hotel.getDto());
        }

        // Add excursions dtos
        for (Excursion excursion: this.getExcursions()) {
            packageDto.getExcursions().add(excursion.getDto());
        }

        return packageDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public City getDestination() {
        return destination;
    }

    public void setDestination(City destination) {
        this.destination = destination;
    }

    public List<Transport> getLeavingTrasports() {
        return leavingTrasports;
    }

    public void setLeavingTrasports(List<Transport> leavingTrasports) {
        this.leavingTrasports = leavingTrasports;
    }

    public List<Transport> getReturningTransports() {
        return returningTransports;
    }

    public void setReturningTransports(List<Transport> returningTransports) {
        this.returningTransports = returningTransports;
    }

    public List<Hotel> getAccomodations() {
        return accomodations;
    }

    public void setAccomodations(List<Hotel> accomodations) {
        this.accomodations = accomodations;
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
    }
}
