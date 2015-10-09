package admin;

import travelDream.bean.HotelInterface;
import travelDream.dto.CityDto;
import travelDream.dto.HotelDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Managed bean controlling the management
 * of the hotels
 */

@ManagedBean(name = "HotelManagedBean")
@ViewScoped
public class HotelManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private HotelInterface hotelInterface;

    private List<HotelDto> hotelList;
    private HotelDto hotelDto = new HotelDto();
    private List<CityDto> cityList;
    private boolean adding;
    private boolean editing;

    @PostConstruct
    public void init() {
        this.setHotelList(this.hotelInterface.getHotels());
        this.setCityList(this.hotelInterface.getCities());
    }

    /**
     * Create a new hotel entity
     */
    public void create() {
        List<HotelInterface.HotelInputError> hotelInputErrors = this.hotelInterface.createHotel(this.hotelDto);
        if (hotelInputErrors.size() != 0) {
            this.communicateErrors(hotelInputErrors);
        } else {
            this.setHotelList(this.hotelInterface.getHotels());    // Update flight list
            this.hotelDto = new HotelDto(); // Reset placeholder.
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
     * @param hotelDto the selected item
     */
    public void edit(HotelDto hotelDto) {
        this.hotelDto = hotelDto;
        this.setEditing(true);
    }

    /**
     * Save changes to the item
     */
    public void save() {
        List<HotelInterface.HotelInputError> hotelInputErrors = this.hotelInterface.updateHotel(this.hotelDto);
        if (hotelInputErrors.size() != 0) {
            this.communicateErrors(hotelInputErrors);
        } else {
            this.hotelDto = new HotelDto(); // Reset placeholder.
            this.setEditing(false);
        }
    }

    /**
     * Remove the selected item
     *
     * @param hotelDto the selected item
     */
    public void delete(HotelDto hotelDto) {
        this.hotelInterface.deleteHotel(hotelDto);
        this.setHotelList(this.hotelInterface.getHotels());
    }

    /**
     * Reload the page with the list of hotels
     */
    public void cancel() {
        this.hotelDto = new HotelDto(); // Reset placeholder
        this.setEditing(false);
        this.setAdding(false);
        this.setHotelList(this.hotelInterface.getHotels());
    }

    /**
     * This method return the Dto of the selected city
     *
     * @param id the id used to return the cityDto
     * @return the Dto of the selected city
     */
    public CityDto getCity(int id) {
        for (CityDto dto : this.getCityList()) {
            if (dto.getId() == id)
                return dto;
        }
        return null;
    }


    private void communicateErrors(List<HotelInterface.HotelInputError> hotelInputErrors) {
        if (hotelInputErrors.contains(HotelInterface.HotelInputError.WRONG_NAME))
            FacesContext.getCurrentInstance().addMessage("customize:name",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid name",
                            "invalid name"));
        if (hotelInputErrors.contains(HotelInterface.HotelInputError.MISSING_DESCRIPTION))
            FacesContext.getCurrentInstance().addMessage("customize:description",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing description",
                            "missing description"));
        if (hotelInputErrors.contains(HotelInterface.HotelInputError.MISSING_CITY))
            FacesContext.getCurrentInstance().addMessage("customize:selectCity",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid city",
                            "invalid city"));
        if (hotelInputErrors.contains(HotelInterface.HotelInputError.INVALID_PRICE))
            FacesContext.getCurrentInstance().addMessage("customize:price",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid price",
                            "invalid price"));
    }

    /*
    * GETTERS AND SETTERS
    * */

    public List<HotelDto> getHotelList() {
        return hotelList;
    }

    public void setHotelList(List<HotelDto> hotelList) {
        this.hotelList = hotelList;
    }

    public HotelDto getHotelDto() {
        return hotelDto;
    }

    public void setHotelDto(HotelDto hotelDto) {
        this.hotelDto = hotelDto;
    }

    public boolean isAdding() {
        return adding;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public List<CityDto> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityDto> cityList) {
        this.cityList = cityList;
    }
}
