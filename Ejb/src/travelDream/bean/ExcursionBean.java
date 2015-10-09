package travelDream.bean;

import travelDream.dto.CityDto;
import travelDream.dto.ExcursionDto;
import travelDream.entity.generics.City;
import travelDream.entity.products.Excursion;
import travelDream.entity.travelPackage.*;
import travelDream.entity.travelPackage.Package;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Bean implementing the logic to manage the excursion entity.
 */
@Stateless
public class ExcursionBean implements ExcursionInterface {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method create an excursion in the database
     * @param excursionDto the data of the excursion
     */
    @Override
    public List<ExcursionInputError> createExcursion(ExcursionDto excursionDto) {
        //Validate dto
        List<ExcursionInputError> excursionInputErrors = this.checkExcursion(excursionDto);
        if (excursionInputErrors.size() == 0) {
            Excursion excursion = new Excursion(excursionDto);
            this.entityManager.persist(excursion);
        }
        return excursionInputErrors;
    }

    /**
     * This method updates the corresponding excursion record
     * in the database
     *
     * @param excursionDto the new data of the excursion
     */
    @Override
    public List<ExcursionInputError> updateExcursion(ExcursionDto excursionDto) {
        //Validate dto
        List<ExcursionInputError> excursionInputErrors = this.checkExcursion(excursionDto);
        if (excursionInputErrors.size() == 0) {
            Excursion excursion = new Excursion(excursionDto);
            excursion.setId(excursionDto.getId());
            entityManager.merge(excursion);
        }
        return excursionInputErrors;
    }

    /**
     * Delete existing excursion
     *
     * @param excursionDto
     * @return
     */
    @Override
    public List<ExcursionInputError> deleteExcursion(ExcursionDto excursionDto) {
        try {
            Object o = this.entityManager.find(Excursion.class, excursionDto.getId());

            if (o != null) {
                this.entityManager.remove(o);
            }
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        this.entityManager.flush();
        // Clear JPA cache to synchronize with DB
        this.entityManager.getEntityManagerFactory().getCache().evictAll();

        return new ArrayList<ExcursionInputError>();
    }

    /**
     * This method returns the list of the not deleted excursions
     *
     * @return the list of all the not deleted excursions
     */
    @Override
    public List<ExcursionDto> getExcursions() {
        List entries = entityManager.createNamedQuery(Excursion.FIND_ALL).getResultList();
        List<ExcursionDto> excursionDtos = new ArrayList<ExcursionDto>();

        for (Object o: entries) {
            if (o instanceof Excursion) {
                excursionDtos.add(((Excursion)o).getDto());
            }
        }
        return excursionDtos;
    }

    /**
     * This method returns the cities in the database
     *
     * @return the cities in the database
     */
    @Override
    public List<CityDto> getCities() {
        List<City> entries = entityManager.createNamedQuery(City.FIND_ALL).getResultList();
        List<CityDto> cityDtos = new ArrayList<CityDto>();
        for (City city : entries) {
            cityDtos.add(city.getDto());
        }
        return cityDtos;
    }

    private List<ExcursionInputError> checkExcursion(ExcursionDto excursionDto) {
        List<ExcursionInputError> excursionInputErrors = new ArrayList<ExcursionInputError>();

        String name = excursionDto.getName();
        String namePattern = "([a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*)";
        String description = excursionDto.getDescription();
        double price = excursionDto.getPrice();
        CityDto cityDto = excursionDto.getCity();
        Date startingHour = excursionDto.getStartingHour();
        Date endingHour = excursionDto.getEndingHour();

        if (name == null || name.isEmpty() || !name.matches(namePattern))
            excursionInputErrors.add(ExcursionInputError.WRONG_NAME);

        if (description == null || description.isEmpty())
            excursionInputErrors.add(ExcursionInputError.MISSING_DESCRIPTION);

        String text = Double.toString(price);
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        if (decimalPlaces > 2 || price < 0)
            excursionInputErrors.add(ExcursionInputError.INVALID_PRICE);

        if (cityDto == null || cityDto.getName() == null || cityDto.getNation() == null)
            excursionInputErrors.add(ExcursionInputError.MISSING_PLACE);

        if (startingHour == null)
            excursionInputErrors.add(ExcursionInputError.INVALID_STARTING_TIME);

        if (endingHour == null)
            excursionInputErrors.add(ExcursionInputError.INVALID_ENDING_TIME);

        if (startingHour != null && endingHour != null && startingHour.after(endingHour))
            excursionInputErrors.add(ExcursionInputError.ENDING_BEFORE_STARTING_TIME);

        return excursionInputErrors;
    }
}
