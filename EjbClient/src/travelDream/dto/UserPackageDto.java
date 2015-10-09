package travelDream.dto;

/**
 * @author Massimo De Marchi
 *         Date: 21/01/14
 *         Time: 14:35
 */
public interface UserPackageDto {
    public int getId();
    public String getName();
    public String getDescription();
    public CityDto getCity();
    public int getDuration();
    public int getNumberOfParticipants();
    public TransportDto getLeavingTransport();
    public TransportDto getReturningTransport();
    public HotelDto getAccomodation();
    public ExcursionDto getExcursion();
}
