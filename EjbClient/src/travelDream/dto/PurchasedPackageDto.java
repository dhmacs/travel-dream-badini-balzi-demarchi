package travelDream.dto;

/**
 * @author Massimo De Marchi
 *         Date: 21/01/14
 *         Time: 14:16
 */
public class PurchasedPackageDto implements UserPackageDto {
    private int id;
    private String name;
    private String description;
    private CityDto destination;
    private int duration;
    private int numberOfParticipants;
    private TransportDto leavingTransport;
    private TransportDto returningTransport;
    private HotelDto accomodation;
    private ExcursionDto excursion;

    // Purchasing info
    private UserDto buyer;
    private UserDto owner;


    ///////////////////////////////////////////
    //                METHODS                //
    ///////////////////////////////////////////

    public UserDto getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDto buyer) {
        this.buyer = buyer;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDestination(CityDto destination) {
        this.destination = destination;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public void setLeavingTransport(TransportDto leavingTransport) {
        this.leavingTransport = leavingTransport;
    }

    public void setReturningTransport(TransportDto returningTransport) {
        this.returningTransport = returningTransport;
    }

    public void setAccomodation(HotelDto accomodation) {
        this.accomodation = accomodation;
    }

    public void setExcursion(ExcursionDto excursion) {
        this.excursion = excursion;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public CityDto getCity() {
        return this.destination;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public int getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    @Override
    public TransportDto getLeavingTransport() {
        return this.leavingTransport;
    }

    @Override
    public TransportDto getReturningTransport() {
        return this.returningTransport;
    }

    @Override
    public HotelDto getAccomodation() {
        return this.accomodation;
    }

    @Override
    public ExcursionDto getExcursion() {
        return excursion;
    }
}
