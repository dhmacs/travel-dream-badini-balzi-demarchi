package admin;

import travelDream.bean.TransportInterface;
import travelDream.dto.AirportDto;
import travelDream.dto.FlightDto;
import travelDream.dto.TransportDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.SelectEvent;

/**
 * @author Massimo De Marchi
 *         Date: 07/01/14
 *         Time: 15:15
 */

@ManagedBean(name = "TransportManagedBean")
@ViewScoped
public class TransportManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private TransportInterface transportInterface;

    private List<FlightDto> flightList;
    private List<AirportDto> airportList;

    private FlightDto flightDto = new FlightDto();
    private boolean editing;
    private boolean adding;

    @PostConstruct
    public void init() {
        this.setFlightList(this.transportInterface.getTransports());
        this.setAirportList(this.transportInterface.getAirports());
    }

    /**
     * Create new transport entity
     */
    public void create() {
        List<TransportInterface.TrasportInputError> trasportInputErrors;

        // Set the departure city based on the chosen airport
        if (flightDto.getDepartureAirport() != null && flightDto.getArrivalAirport() != null) {
            this.flightDto.setDepartureCity(this.flightDto.getDepartureAirport().getCity());
            this.flightDto.setArrivalCity(this.flightDto.getArrivalAirport().getCity());
        }

        // Persist changes
        trasportInputErrors = this.transportInterface.createTransport(this.flightDto);

        // Validation : if the list of errors is not empty then displays error messages
        if (trasportInputErrors.size() > 0) {
            this.communicateError(trasportInputErrors);
        } else {
            // Reset the status
            this.setFlightList(this.transportInterface.getTransports());    // Update flight list
            this.flightDto = new FlightDto(); // Reset placeholder.
            this.setAdding(false);
        }
    }

    /**
     * Reload the page in adding mode
     */
    public void add() {
        this.setAdding(true);
    }

    /**
     * Reload the page in editing mode with the selected item
     *
     * @param flightDto the selected item
     */
    public void edit(FlightDto flightDto) {
        this.flightDto = flightDto;
        this.setEditing(true);
    }

    /**
     * Save changes to the item
     */
    public void save() {
        List<TransportInterface.TrasportInputError> trasportInputErrors;

        // Set the departure city based on the chosen airport
        if (flightDto.getDepartureAirport() != null && flightDto.getArrivalAirport() != null) {
            this.flightDto.setDepartureCity(this.flightDto.getDepartureAirport().getCity());
            this.flightDto.setArrivalCity(this.flightDto.getArrivalAirport().getCity());
        }

        // get errors
        trasportInputErrors = this.transportInterface.updateTransport(this.flightDto);

        // Validation : if the list of errors is not empty then displays error messages
        if (trasportInputErrors.size() > 0) {
            this.communicateError(trasportInputErrors);
        } else {
            // Reset the status
            this.flightDto = new FlightDto(); // Reset placeholder.
            this.setEditing(false);
            this.setFlightList(this.transportInterface.getTransports());    // Update flight list
        }
    }

    /**
     * Remove the selected item
     *
     * @param flightDto the selected item
     */
    public void delete(FlightDto flightDto) {
        // Register deletion
        //this.transportInterface.updateTransport(flightDto);
        this.transportInterface.deleteTransport(flightDto);
        // Reset status
        this.flightDto = new FlightDto(); // Reset placeholder.
        this.setFlightList(this.transportInterface.getTransports());
    }

    /**
     * Reload the page with the list of transports
     */
    public void cancel() {
        this.flightDto = new FlightDto(); // Reset placeholder
        this.setEditing(false);
        this.setAdding(false);
        this.setFlightList(this.transportInterface.getTransports());
    }


    public AirportDto getAirport(int id) {
        for (AirportDto dto : this.getAirportList()) {
            if (dto.getId() == id)
                return dto;
        }

        return null;
    }

    /*
    * GETTERS AND SETTERS
    * */

    public List<FlightDto> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<TransportDto> transportDtos) {
        this.flightList = new ArrayList<FlightDto>();

        for (TransportDto transportDto : transportDtos) {
            if (transportDto instanceof FlightDto) {
                this.flightList.add((FlightDto) transportDto);
            }
        }
    }


    private void communicateError(List<TransportInterface.TrasportInputError> trasportInputErrors) {

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.MISSING_DEPARTURE))
            FacesContext.getCurrentInstance().addMessage("transport:departureAirport",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing departure",
                            "missing departure"));

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.MISSING_DEPARTURE_DATETIME))
            FacesContext.getCurrentInstance().addMessage("transport:departure",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing departure date/time",
                            "missing departure date/time"));

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.MISSING_ARRIVAL))
            FacesContext.getCurrentInstance().addMessage("transport:arrivalAirport",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing arrival",
                            "missing arrival"));

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.MISSING_ARRIVAL_DATETIME))
            FacesContext.getCurrentInstance().addMessage("transport:arrival",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing arrival date/time",
                            "missing arrival date/time"));

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.INVALID_PRICE))
            FacesContext.getCurrentInstance().addMessage("transport:price",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid price",
                            "invalid price"));

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.WRONG_FLIGHT_NUMBER))
            FacesContext.getCurrentInstance().addMessage("transport:flightNumber",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid flight number",
                            "invalid flight number"));

        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.MISSING_AIRLINE))
            FacesContext.getCurrentInstance().addMessage("transport:airline",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing airline",
                            "missing airline"));
        if (trasportInputErrors.contains(TransportInterface.TrasportInputError.DATE_ARRIVAL_BEFORE_DATE_DEPARTURE))
            // Note: "transport:arrival" means component with id="arrival" contained in
            // form with id="transport"
            FacesContext.getCurrentInstance().addMessage("transport:arrival",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "arrival before departure",
                            "invalid time ordering"));


    }

    public List<AirportDto> getAirportList() {
        return airportList;
    }

    public void setAirportList(List<AirportDto> airportList) {
        this.airportList = airportList;
    }

    public FlightDto getFlightDto() {
        return flightDto;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isAdding() {
        return adding;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }
}
