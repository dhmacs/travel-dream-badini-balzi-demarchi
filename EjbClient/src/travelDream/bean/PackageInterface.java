package travelDream.bean;

import travelDream.dto.*;

import java.util.List;

/**
 * Created by alessandrobalzi on 10/01/14.
 */
public interface PackageInterface {
    public enum PackageInputError {
        LEAVING_DESTINATION_DIFFERENT_FROM_PACKAGE_DESTINATION,
        WRONG_NAME,
        MISSING_DESCRIPTION,
        INVALID_DURATION,
        INVALID_NUMBER_OF_PARTICIPANTS,
        MISSING_DESTINATION,
        DUPLICATES_LEAVING,
        DUPLICATES_RETURNING,
        DUPLICATES_ACCOMODATIONS,
        DUPLICATES_EXCURSIONS
    }
    public List<PackageInputError> createPackage(PackageDto packageDto);
    public List<PackageInputError> updatePackage(PackageDto packageDto);
    public List<PackageInputError> deletePackage(PackageDto packageDto);

    public List<PackageDto> getUserPackages();
    public List<PackageDto> getAdminPackages();
    public List<CityDto> getCities();
    public List<TransportDto> getTransportWithArrival(CityDto arrivalCityDto);
    public List<TransportDto> getTransportWithDeparture(CityDto departureCityDto);
    public List<ExcursionDto> getExcursionsByDestination(CityDto destinationCityDto);
    public List<HotelDto> getHotelsByDestination(CityDto cityDto);
    public PackageDto getPackageByID(Integer id);
    public List<TransportDto> getFilteredLeavingTransports(PackageDto packageDto);
    public List<TransportDto> getFilteredReturnTransports(PackageDto packageDto, TransportDto transportDto);
    public List<HotelDto> getFilteredAccomodations(PackageDto packageDto);
    public List<ExcursionDto> getFilteredExcursions(PackageDto packageDto);
}
