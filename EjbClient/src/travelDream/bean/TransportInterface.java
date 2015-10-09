package travelDream.bean;

import travelDream.dto.AirportDto;
import travelDream.dto.FlightDto;
import travelDream.dto.TransportDto;

import java.util.List;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 17:07
 *
 *         Interface class which handles transport transaction between front end and back end
 */
public interface TransportInterface {
    // Declaring an enum which contains all types of error which can occur on input data
    public enum TrasportInputError {
        INVALID_PRICE, MISSING_DEPARTURE, MISSING_DEPARTURE_DATETIME,MISSING_ARRIVAL,
        MISSING_ARRIVAL_DATETIME,WRONG_FLIGHT_NUMBER,MISSING_AIRLINE,DATE_ARRIVAL_BEFORE_DATE_DEPARTURE
    }

    public List<TrasportInputError> createTransport(TransportDto transportDto);

    /**
     * Update transport entity with the given dto.
     * !! NOTE: this is used also to delete the transport, since delete action just update a boolean flag that memorize
     * the state of the trasport.
     * @param transportDto dto containing transportation info
     */
    public List<TrasportInputError> updateTransport(TransportDto transportDto);

    /**
     * Delete transport entity of the given dto
     * @param transportDto
     * @return
     */
    public List<TrasportInputError> deleteTransport(TransportDto transportDto);

    /**
     * Get list of trasport
     * @return list of TransportDtos
     */
    public List<TransportDto> getTransports();

    /**
     * Get list of airports
     * @return list of AirportDtos
     */
    public List<AirportDto> getAirports();
}
