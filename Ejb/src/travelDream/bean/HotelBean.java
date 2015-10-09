package travelDream.bean;

import travelDream.dto.CityDto;
import travelDream.dto.HotelDto;
import travelDream.entity.generics.City;
import travelDream.entity.products.Hotel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean implementing the logic to manage the hotel entity.
 */
@Stateless
public class HotelBean implements HotelInterface {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method create a hotel in the database
     *
     * @param hotelDto the data of the hotel
     */
    @Override
    public List<HotelInputError> createHotel(HotelDto hotelDto) {
        //Validate
        List<HotelInputError> hotelInputErrors = this.checkHotel(hotelDto);
        if (hotelInputErrors.size() == 0) {
            Hotel hotel = new Hotel(hotelDto);
            entityManager.persist(hotel);
        }
        return hotelInputErrors;
    }

    /**
     * This method updates the corresponding hotel record
     * in the database
     *
     * @param hotelDto the new data off the hotel
     */
    @Override
    public List<HotelInputError> updateHotel(HotelDto hotelDto) {
        //Validate
        List<HotelInputError> hotelInputErrors = this.checkHotel(hotelDto);
        if (hotelInputErrors.size() == 0) {
            Hotel hotel = new Hotel(hotelDto);
            hotel.setId(hotelDto.getId());
            this.entityManager.merge(hotel);
        }
        return hotelInputErrors;
    }

    /**
     * This method deletes an hotel
     *
     * @param hotelDto
     * @return
     */
    @Override
    public List<HotelInputError> deleteHotel(HotelDto hotelDto) {
        try {
            Object o = this.entityManager.find(Hotel.class, hotelDto.getId());

            if (o != null) {
                this.entityManager.remove(o);
            }
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        this.entityManager.flush();
        // Clear JPA cache to synchronize with DB
        this.entityManager.getEntityManagerFactory().getCache().evictAll();

        return new ArrayList<HotelInputError>();
    }

    /**
     * This method returns the list of the not deleted hotels
     *
     * @return the list of all the not deleted hotels
     */
    @Override
    public List<HotelDto> getHotels() {
        List entries = entityManager.createNamedQuery(Hotel.FIND_ALL).getResultList();
        List<HotelDto> hotelDtos = new ArrayList<HotelDto>();
        for (Object o: entries) {
            if (o instanceof Hotel) {
                hotelDtos.add(((Hotel) o).getDto());
            }
        }
        return hotelDtos;
    }

    /**
     * This method returns the cities in the database
     *
     * @return the cities in the database
     */
    @Override
    public List<CityDto> getCities() {
        List entries = entityManager.createNamedQuery(City.FIND_ALL).getResultList();
        List<CityDto> cityDtos = new ArrayList<CityDto>(); 
        for (Object o: entries) {
            if (o instanceof City) {
                cityDtos.add(((City)o).getDto());
            }
        }
        return cityDtos;
    }

    //Validation of the hotel data
    private List<HotelInputError> checkHotel(HotelDto hotelDto) {
        List<HotelInputError> hotelInputErrors = new ArrayList<HotelInputError>();

        String name = hotelDto.getName();
        String namePattern = "([a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*)";
        String description = hotelDto.getDescription();
        double price = hotelDto.getPrice();
        CityDto cityDto = hotelDto.getCityDto();

        if (name == null || name.isEmpty() || !name.matches(namePattern))
            hotelInputErrors.add(HotelInputError.WRONG_NAME);

        if (description == null || description.isEmpty())
            hotelInputErrors.add(HotelInputError.MISSING_DESCRIPTION);

        String text = Double.toString(price);
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        if (decimalPlaces > 2 || price < 0)
            hotelInputErrors.add(HotelInputError.INVALID_PRICE);

        if (cityDto == null || cityDto.getName() == null || cityDto.getNation() == null)
            hotelInputErrors.add(HotelInputError.MISSING_CITY);

        return hotelInputErrors;
    }
}
