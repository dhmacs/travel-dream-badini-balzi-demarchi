package travelDream.bean;

import travelDream.dto.*;
import travelDream.entity.products.Flight;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by federico on 29/01/14.
 */
@Stateless
public class PackageValidator implements PackageValidatorInterface {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PackageInterface packageInterface;
    @EJB
    private TransportInterface transportInterface;

    public boolean isValid(CustomPackageDto customPackageDto) {
        this.entityManager.getEntityManagerFactory().getCache().evictAll();
        if (customPackageDto == null)
            return false;
        int parentPackageID = customPackageDto.getParentPackage().getId();
        PackageDto parentPackageDto = packageInterface.getPackageByID(parentPackageID);
        if (parentPackageDto == null)
            return false;
        if(!parentPackageDto.isActive())
            return false;
        if(parentPackageDto.getLeavingFlights() == null || !parentPackageDto.getLeavingFlights().contains(
                customPackageDto.getLeavingFlight()) || !checkActiveLeavingFlight(parentPackageDto,customPackageDto))
            return false;
        if (parentPackageDto.getReturningFlights() == null || !parentPackageDto.getReturningFlights().contains(
                customPackageDto.getReturningFlight()) || !checkActiveReturnFlight(parentPackageDto,customPackageDto))
            return false;
        if (parentPackageDto.getAccomodations() == null || !parentPackageDto.getAccomodations().contains(
                customPackageDto.getAccomodation()) || !checkActiveHotel(parentPackageDto,customPackageDto))
            return false;
        if(customPackageDto.getExcursion() != null && (parentPackageDto.getExcursions() == null || !parentPackageDto
                .getExcursions().contains(customPackageDto.getExcursion())|| !checkActiveExcursions(parentPackageDto,customPackageDto)))
            return false;

        return true;
    }

    private boolean checkActiveLeavingFlight(PackageDto parentPackage, CustomPackageDto customPackage){
        for(FlightDto flightDto: parentPackage.getLeavingFlights()){
            if(flightDto.equals(customPackage.getLeavingFlight()))
                if (!flightDto.isActive())
                    return false;
        }
        if(parentPackage.getLeavingFlights().contains(customPackage.getLeavingFlight()))
            return true;
        return false;
    }

    private boolean checkActiveReturnFlight(PackageDto parentPackage, CustomPackageDto customPackage){
        for(FlightDto flightDto: parentPackage.getReturningFlights()){
            if(flightDto.equals(customPackage.getReturningFlight()))
                if (!flightDto.isActive())
                    return false;
        }
        if(parentPackage.getReturningFlights().contains(customPackage.getReturningFlight()))
            return true;
        return false;
    }

    private boolean checkActiveHotel(PackageDto parentPackage, CustomPackageDto customPackage){
        for(HotelDto hotelDto: parentPackage.getAccomodations()){
            if(hotelDto.equals(customPackage.getAccomodation()))
                if (!hotelDto.isActive())
                    return false;
        }
        if(parentPackage.getAccomodations().contains(customPackage.getAccomodation()))
            return true;
        return false;
    }

    private boolean checkActiveExcursions(PackageDto parentPackage, CustomPackageDto customPackage){
        for(ExcursionDto excursionDto: parentPackage.getExcursions()){
            if(excursionDto.equals(customPackage.getExcursion()))
                if (!excursionDto.isActive())
                    return false;
        }
        if(parentPackage.getExcursions().contains(customPackage.getExcursion()))
            return true;
        return false;
    }
}
