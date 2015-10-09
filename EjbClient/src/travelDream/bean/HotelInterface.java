package travelDream.bean;

import travelDream.dto.CityDto;
import travelDream.dto.HotelDto;

import java.util.List;

/**
 * Interface class which handles transport transaction between front end and back end
 */
public interface HotelInterface {

    public enum HotelInputError {
        WRONG_NAME, MISSING_DESCRIPTION, MISSING_CITY, INVALID_PRICE
    }

    /**
     * This method create a hotel entity
     *
     * @param hotelDto the data of the hotel
     */
    public List<HotelInputError> createHotel(HotelDto hotelDto);

    /**
     * This method updates the values of an existing hotel
     * <p/>
     * !! NOTE: this is used also to delete the hotel, since delete action just update a boolean flag that memorize
     * the state of the hotel.
     *
     * @param hotelDto the new data of the hotel
     */
    public List<HotelInputError> updateHotel(HotelDto hotelDto);

    /**
     * This method deletes an hotel
     * @param hotelDto
     * @return
     */
    public List<HotelInputError> deleteHotel(HotelDto hotelDto);

    /**
     * This method returns the list of all the hotels
     *
     * @return the list of the hotels
     */
    public List<HotelDto> getHotels();

    /**
     * This method returns the list of cities
     *
     * @return the list of cities
     */
    public List<CityDto> getCities();

}
