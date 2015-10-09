package admin;

import travelDream.bean.ExcursionInterface;
import travelDream.dto.CityDto;
import travelDream.dto.ExcursionDto;

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
 * Created by alessandrobalzi on 07/01/14.
 */

@ManagedBean(name = "ExcursionManagedBean")
@ViewScoped
public class ExcursionManagedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private ExcursionInterface excursion;

    private List<ExcursionDto> excursionList;
    private List<CityDto> cityList;
    private ExcursionDto excursionDto = new ExcursionDto();
    private boolean adding;
    private boolean editing;

    @PostConstruct
    public void init() {
        this.setExcursionList(this.excursion.getExcursions());
        this.setCityList(this.excursion.getCities());
    }

    /**
     * Reload the page in creation mode
     */
    public void add() {
        this.adding = true;
    }

    /**
     * Create new excursion entity
     */
    public void create() {
        List<ExcursionInterface.ExcursionInputError> excursionInputErrors =
                this.excursion.createExcursion(excursionDto);
        if (excursionInputErrors.size() != 0) {
            this.communicateErrors(excursionInputErrors);
        } else {
            this.setExcursionList(this.excursion.getExcursions());  //Update the flight list
            this.excursionDto = new ExcursionDto();  //reset placeholder
            this.adding = false;
        }
    }

    /**
     * Reload the page in editing mode with the selected item
     *
     * @param excursionDto the selected item
     */
    public void edit(ExcursionDto excursionDto) {
        this.excursionDto = excursionDto;
        this.editing = true;
    }

    /**
     * Save changes to the item
     */
    public void save() {
        List<ExcursionInterface.ExcursionInputError> excursionInputErrors =
                this.excursion.updateExcursion(excursionDto);
        if (excursionInputErrors.size() != 0) {
            this.communicateErrors(excursionInputErrors);
        } else {
            this.excursionDto = new ExcursionDto();
            this.editing = false;
        }
    }

    /**
     * Come back to the page with the list of excursions
     */
    public void cancel() {
        this.excursionDto = new ExcursionDto();
        this.adding = false;
        this.editing = false;
        this.setExcursionList(this.excursion.getExcursions());
    }

    /**
     * Remove the selected item
     *
     * @param excursionDto the selected item
     */
    public void delete(ExcursionDto excursionDto) {
        this.excursion.deleteExcursion(excursionDto);
        this.setExcursionList(this.excursion.getExcursions());
    }

    private void communicateErrors(List<ExcursionInterface.ExcursionInputError> excursionInputErrors) {
        if(excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.WRONG_NAME))
            FacesContext.getCurrentInstance().addMessage("customize:name",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid name",
                            "invalid name"));
        if(excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.MISSING_DESCRIPTION))
            FacesContext.getCurrentInstance().addMessage("customize:description",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "missing description",
                            "missing description"));
        if(excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.MISSING_PLACE))
            FacesContext.getCurrentInstance().addMessage("customize:place",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid place",
                            "invalid place"));
        if(excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.INVALID_PRICE))
            FacesContext.getCurrentInstance().addMessage("customize:price",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid price",
                            "invalid price"));
        if(excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.INVALID_STARTING_TIME))
            FacesContext.getCurrentInstance().addMessage("customize:startingTime",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid starting time",
                            "invalid starting time"));
        if(excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.INVALID_ENDING_TIME))
            FacesContext.getCurrentInstance().addMessage("customize:endingTime",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid ending time",
                            "invalid ending time"));
        if (excursionInputErrors.contains(ExcursionInterface.ExcursionInputError.ENDING_BEFORE_STARTING_TIME))
            FacesContext.getCurrentInstance().addMessage("customize:startingTime",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "wrong time ordering",
                            "wrong time ordering"));


    }

    //GETTERS AND SETTERS

    public ExcursionDto getExcursionDto() {
        return excursionDto;
    }

    public List<ExcursionDto> getExcursionList() {
        return excursionList;
    }

    public void setExcursionList(List<ExcursionDto> excursionList) {
        this.excursionList = excursionList;
    }

    public List<CityDto> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityDto> cityList) {
        this.cityList = cityList;
    }

    public boolean isAdding() {
        return adding;
    }

    public boolean isEditing() {
        return editing;
    }

    public CityDto getCity(int id) {
        for (CityDto city : cityList) {
            if (city.getId() == id) {
                return city;
            }

        }
        return null;
    }
}
