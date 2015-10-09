package travelDream.bean;

import travelDream.dto.CityDto;
import travelDream.dto.ExcursionDto;

import java.util.List;

/**
 * Created by alessandrobalzi on 07/01/14.
 */
public interface ExcursionInterface {

    public enum ExcursionInputError{
        WRONG_NAME,MISSING_DESCRIPTION,INVALID_PRICE,MISSING_PLACE,
                INVALID_STARTING_TIME,INVALID_ENDING_TIME, ENDING_BEFORE_STARTING_TIME
    }

    /**
     * Create a new excursion
     * @param excursionDto
     * @return
     */
    public List<ExcursionInputError> createExcursion(ExcursionDto excursionDto);

    /**
     * Update existing excursion
     * @param excursionDto
     * @return
     */
    public List<ExcursionInputError> updateExcursion(ExcursionDto excursionDto);

    /**
     * Delete existing excursion
     * @param excursionDto
     * @return
     */
    public List<ExcursionInputError> deleteExcursion(ExcursionDto excursionDto);

    public List<ExcursionDto> getExcursions();

    public List<CityDto> getCities();
}
