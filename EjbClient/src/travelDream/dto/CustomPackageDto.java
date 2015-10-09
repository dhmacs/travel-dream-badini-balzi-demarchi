package travelDream.dto;

import java.util.List;

/**
 * Created by alessandrobalzi on 19/01/14.
 */
public class CustomPackageDto implements UserPackageDto {

    private int id;
    private PackageDto parentPackage;
    private TransportDto leavingTransport;
    private TransportDto returningTransport;
    private HotelDto accomodation;
    private ExcursionDto excursion;


    public void setId(int id) {
        this.id = id;
    }

    public void setParentPackage(PackageDto parentPackage) {
        this.parentPackage = parentPackage;
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

    public PackageDto getParentPackage() {
        return parentPackage;
    }

    public FlightDto getLeavingFlight() {
        if (this.leavingTransport instanceof FlightDto) {
            return (FlightDto)this.leavingTransport;
        }
        return null;
    }

    public FlightDto getReturningFlight() {
        if (this.returningTransport instanceof FlightDto) {
            return (FlightDto)this.returningTransport;
        }
        return null;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.parentPackage.getName();
    }

    @Override
    public String getDescription() {
        return this.parentPackage.getDescription();
    }

    @Override
    public CityDto getCity() {
        return this.parentPackage.getCity();
    }

    @Override
    public int getDuration() {
        return this.parentPackage.getDuration();
    }

    @Override
    public int getNumberOfParticipants() {
        return this.parentPackage.getNumberOfParticipants();
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
        return this.excursion;
    }


}
