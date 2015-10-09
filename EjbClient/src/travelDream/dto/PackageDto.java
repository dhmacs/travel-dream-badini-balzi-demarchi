package travelDream.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object of the package entity
 */
public class PackageDto {

    //Attributes

    private int id;
    private String name;
    private String description;
    private int duration;
    private int numberOfParticipants;
    private CityDto city;

    private List<TransportDto> leavingTransports = new ArrayList<TransportDto>();
    private List<TransportDto> returningTransports = new ArrayList<TransportDto>();
    private List<HotelDto> accomodations = new ArrayList<HotelDto>();
    private List<ExcursionDto> excursions = new ArrayList<ExcursionDto>();

    private boolean isActive = true;

    //Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }

    public List<TransportDto> getLeavingTransports() {
        return leavingTransports;
    }

    public void setLeavingTransports(List<TransportDto> leavingTransports) {
        this.leavingTransports = leavingTransports;
    }

    public List<TransportDto> getReturningTransports() {
        return returningTransports;
    }

    public void setReturningTransports(List<TransportDto> returningTransports) {
        this.returningTransports = returningTransports;
    }

    public List<HotelDto> getAccomodations() {
        return accomodations;
    }

    public void setAccomodations(List<HotelDto> accomodations) {
        this.accomodations = accomodations;
    }

    public List<ExcursionDto> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<ExcursionDto> excursions) {
        this.excursions = excursions;
    }

    public List<FlightDto> getLeavingFlights(){
        List<FlightDto> flights = new ArrayList<FlightDto>();
        for(TransportDto transportDto: this.leavingTransports){
            if(transportDto instanceof FlightDto)
                flights.add((FlightDto) transportDto);
        }
        return flights;
    }

    public List<FlightDto> getReturningFlights(){
        List<FlightDto> flights = new ArrayList<FlightDto>();
        for(TransportDto transportDto: this.returningTransports){
            if(transportDto instanceof FlightDto)
                flights.add((FlightDto) transportDto);
        }
        return flights;
    }
}
