package travelDream.bean;

import travelDream.dto.*;
import travelDream.entity.generics.City;
import travelDream.entity.products.Excursion;
import travelDream.entity.products.Hotel;
import travelDream.entity.products.Transport;
import travelDream.entity.travelPackage.Package;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Bean implementing the logic to manage the package entity.
 */
@Stateless
public class PackageBean implements PackageInterface {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method create a package in the database
     *
     * @param packageDto the data of the package
     */
    @Override
    public List<PackageInputError> createPackage(PackageDto packageDto) {
        List<PackageInputError> packageInputErrors = this.validate(packageDto);

        if (packageInputErrors.size() == 0) {
            Package packageEntity = new Package(packageDto);
            this.entityManager.persist(packageEntity);
        }

        return packageInputErrors;
    }

    /**
     * This method updates the corresponding package record
     * in the database
     *
     * @param packageDto the new data of the package
     */
    @Override
    public List<PackageInputError> updatePackage(PackageDto packageDto) {
        List<PackageInputError> packageInputErrors = this.validate(packageDto);

        if (packageInputErrors.size() == 0) {
            Package packageEntity = new Package(packageDto);
            this.entityManager.merge(packageEntity);
        }

        return packageInputErrors;
    }

    @Override
    public List<PackageInputError> deletePackage(PackageDto packageDto) {
        Package travelPackage = new Package(packageDto);

        travelPackage = this.entityManager.merge(travelPackage);
        this.entityManager.remove(travelPackage);

        return new ArrayList<PackageInputError>();
    }

    /**
     * This method returns the list of the not deleted packages
     *
     * @return the list of all the not deleted packages
     */
    @Override
    public List<PackageDto> getUserPackages() {
        List<Package> entries = this.entityManager.createNamedQuery(Package.FIND_ALL).getResultList();
        List<PackageDto> packageDtos = new ArrayList<PackageDto>();
        for (Package packageEntity : entries) {
            if(this.checkPackageValidity(packageEntity)) {
                packageDtos.add(packageEntity.getDto());
            }
        }
        return packageDtos;
    }

    @Override
    public List<PackageDto> getAdminPackages() {
        List<Package> entries = this.entityManager.createNamedQuery(Package.FIND_ALL).getResultList();
        List<PackageDto> packageDtos = new ArrayList<PackageDto>();
        for (Package packageEntity : entries) {
            packageDtos.add(packageEntity.getDto());
        }
        return packageDtos;
    }

    @Override
    public List<CityDto> getCities() {
        List<City> entries = entityManager.createNamedQuery(City.FIND_ALL).getResultList();
        List<CityDto> cityDtos = new ArrayList<CityDto>();
        for (City city : entries) {
            cityDtos.add(city.getDto());
        }
        return cityDtos;
    }

    @Override
    public List<TransportDto> getTransportWithArrival(CityDto arrivalCityDto) {
        List<Transport> transports = entityManager.createNamedQuery(Transport.FIND_ALL).getResultList();
        List<TransportDto> filteredDtoList = new ArrayList<TransportDto>();

        for (Transport transport : transports) {
            if (transport.getArrivalCity().getId() == arrivalCityDto.getId()) {
                filteredDtoList.add(transport.getDto());
            }
        }

        return filteredDtoList;
    }

    @Override
    public List<TransportDto> getTransportWithDeparture(CityDto departureCityDto) {
        List<Transport> transports = entityManager.createNamedQuery(Transport.FIND_ALL).getResultList();
        List<TransportDto> filteredDtoList = new ArrayList<TransportDto>();

        for (Transport transport : transports) {
            if (transport.getDepartureCity().getId() == departureCityDto.getId()) {
                filteredDtoList.add(transport.getDto());
            }
        }

        return filteredDtoList;
    }

    @Override
    public List<ExcursionDto> getExcursionsByDestination(CityDto cityDto) {
        List<Excursion> excursions = entityManager.createNamedQuery(Excursion.FIND_ALL).getResultList();
        List<ExcursionDto> excursionDtos = new ArrayList<ExcursionDto>();

        for (Excursion excursion : excursions) {
            if (excursion.getCity().getId() == cityDto.getId()) {
                excursionDtos.add(excursion.getDto());
            }
        }
        return excursionDtos;
    }

    @Override
    public List<HotelDto> getHotelsByDestination(CityDto cityDto) {
        List<Hotel> hotels = entityManager.createNamedQuery(Hotel.FIND_ALL).getResultList();
        List<HotelDto> hotelDtos = new ArrayList<HotelDto>();

        for (Hotel hotel : hotels) {
            if (hotel.getCity().getId() == cityDto.getId()) {
                hotelDtos.add(hotel.getDto());
            }
        }
        return hotelDtos;
    }

    @Override
    public PackageDto getPackageByID(Integer id) {
        Package packageEntity = entityManager.find(Package.class, id);
        if(packageEntity == null || !packageEntity.isActive())
            return null;
        return packageEntity.getDto();
    }

    @Override
    public List<TransportDto> getFilteredLeavingTransports(PackageDto packageDto) {
        Calendar myStartingDate,myEndingDate;
        List<TransportDto> filteredLeavingTransports = new ArrayList<TransportDto>();
        for (TransportDto leavingTransport: packageDto.getLeavingTransports()) {
            if(leavingTransport.isActive()) {
                for (TransportDto returnTransport: packageDto.getReturningTransports()) {
                    if(returnTransport.isActive()) {
                        if (leavingTransport.getDepartureCity().equals(returnTransport.getArrivalCity())) {
                            if (leavingTransport.getArrival().before(returnTransport.getDeparture())) {
                                myStartingDate = adjustDate(leavingTransport.getDeparture());
                                myEndingDate = adjustDate(returnTransport.getArrival());
                                myStartingDate.add(Calendar.DAY_OF_MONTH,packageDto.getDuration());
                                if (myEndingDate.getTime().equals(myStartingDate.getTime())) {
                                    filteredLeavingTransports.add(leavingTransport);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return filteredLeavingTransports;
    }

    @Override
    public List<TransportDto> getFilteredReturnTransports(PackageDto packageDto, TransportDto leavingTransport) {
        Calendar myStartingDate,myEndingDate;
        List<TransportDto> filteredReturningTransports = new ArrayList<TransportDto>();
        for (TransportDto returnTransport: packageDto.getReturningTransports()) {
            if (returnTransport.isActive()) {
                if (leavingTransport.getDepartureCity().equals(returnTransport.getArrivalCity())) {
                    if (leavingTransport.getArrival().before(returnTransport.getDeparture())) {
                        myStartingDate = adjustDate(leavingTransport.getDeparture());
                        myEndingDate = adjustDate(returnTransport.getArrival());
                        myStartingDate.add(Calendar.DAY_OF_MONTH, packageDto.getDuration());
                        if (myEndingDate.getTime().equals(myStartingDate.getTime())) {
                            filteredReturningTransports.add(returnTransport);
                        }
                    }
                }
            }
        }
        return filteredReturningTransports;
    }

    @Override
    public List<HotelDto> getFilteredAccomodations(PackageDto packageDto) {
        List<HotelDto> filteredAccommodations = new ArrayList<HotelDto>();
        for (HotelDto accommodation: packageDto.getAccomodations()) {
            if (accommodation.isActive()) {
                filteredAccommodations.add(accommodation);
            }
        }
        return filteredAccommodations;
    }

    @Override
    public List<ExcursionDto> getFilteredExcursions(PackageDto packageDto) {
        List<ExcursionDto> filteredExcursions = new ArrayList<ExcursionDto>();
        for (ExcursionDto excursion: packageDto.getExcursions()) {
            if (excursion.isActive()) {
                filteredExcursions.add(excursion);
            }
        }
        return filteredExcursions;
    }

    private List<PackageInputError> validate(PackageDto packageDto) {
        List<PackageInputError> packageInputErrors = new ArrayList<PackageInputError>();

        String name = packageDto.getName();
        String namePattern = "([a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*)";
        String description = packageDto.getDescription();
        int duration = packageDto.getDuration();
        int numberOfParticipants = packageDto.getNumberOfParticipants();
        CityDto destination = packageDto.getCity();
        int i, j;
        boolean error;

        if (name == null || name.isEmpty() || !name.matches(namePattern)) {
            packageInputErrors.add(PackageInputError.WRONG_NAME);
        }

        if (description == null || description.isEmpty()) {
            packageInputErrors.add(PackageInputError.MISSING_DESCRIPTION);
        }

        if (duration <= 0) {
            packageInputErrors.add(PackageInputError.INVALID_DURATION);
        }

        if (numberOfParticipants <= 0) {
            packageInputErrors.add(PackageInputError.INVALID_NUMBER_OF_PARTICIPANTS);
        }

        if (destination == null || destination.getName() == null || destination.getNation() == null) {
            packageInputErrors.add(PackageInputError.MISSING_DESTINATION);
        }

        i = 0;
        error = false;
        while (i < packageDto.getLeavingTransports().size() && !error) {
            j = i + 1;
            while (j < packageDto.getLeavingTransports().size() && !error) {
                if (packageDto.getLeavingTransports().get(i).getId() == packageDto.getLeavingTransports().get(j).getId()) {
                    error = true;
                    packageInputErrors.add(PackageInputError.DUPLICATES_LEAVING);
                }
                j++;
            }
            i++;
        }

        i = 0;
        error = false;
        while (i < packageDto.getReturningTransports().size() && !error) {
            j = i + 1;
            while (j < packageDto.getReturningTransports().size() && !error) {
                if (packageDto.getReturningTransports().get(i).getId() == packageDto.getReturningTransports().get(j).getId()) {
                    error = true;
                    packageInputErrors.add(PackageInputError.DUPLICATES_RETURNING);
                }
                j++;
            }
            i++;
        }

        i = 0;
        error = false;
        while (i < packageDto.getAccomodations().size() && !error) {
            j = i + 1;
            while (j < packageDto.getAccomodations().size() && !error) {
                if (packageDto.getAccomodations().get(i).getId() == packageDto.getAccomodations().get(j).getId()) {
                    error = true;
                    packageInputErrors.add(PackageInputError.DUPLICATES_ACCOMODATIONS);
                }
                j++;
            }
            i++;
        }

        i = 0;
        error = false;
        while (i < packageDto.getExcursions().size() && !error) {
            j = i + 1;
            while (j < packageDto.getExcursions().size() && !error) {
                if (packageDto.getExcursions().get(i).getId() == packageDto.getExcursions().get(j).getId()) {
                    error = true;
                    packageInputErrors.add(PackageInputError.DUPLICATES_EXCURSIONS);
                }
                j++;
            }
            i++;
        }

        return packageInputErrors;
    }

    private boolean checkPackageValidity(Package travelPackage) {
        Calendar myStartingDate, myEndingDate;
        if (!travelPackage.isActive()){
            return false;
        }
        List<Hotel> activeAccommodations = new ArrayList<Hotel>();
        for (Hotel accommodation: travelPackage.getAccomodations()) {
            if (accommodation.isActive()) {
                activeAccommodations.add(accommodation);
            }
        }
        if (activeAccommodations.size() == 0) {
            return false;
        }
        for (Transport leavingTransport: travelPackage.getLeavingTrasports()) {
            if(leavingTransport.isActive()) {
                for (Transport returnTransport: travelPackage.getReturningTransports()) {
                    if (returnTransport.isActive()) {
                        if (leavingTransport.getDepartureCity().equals(returnTransport.getArrivalCity())) {
                            if (leavingTransport.getArrival().before(returnTransport.getDeparture())) {
                                myStartingDate = adjustDate(leavingTransport.getDeparture());
                                myEndingDate = adjustDate(returnTransport.getArrival());
                                myStartingDate.add(Calendar.DAY_OF_MONTH,travelPackage.getDuration());
                                if (myEndingDate.getTime().equals(myStartingDate.getTime())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private Calendar adjustDate(Date date) {
        Calendar adjustedDate = Calendar.getInstance();
        adjustedDate.setTime(date);
        adjustedDate.set(Calendar.HOUR,0);
        adjustedDate.set(Calendar.MINUTE,0);
        adjustedDate.set(Calendar.SECOND,0);
        adjustedDate.set(Calendar.MILLISECOND,0);
        return adjustedDate;
    }
}
