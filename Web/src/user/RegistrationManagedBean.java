package user;

import travelDream.bean.RegistrationInterface;
import travelDream.dto.RegistrationDto;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Managed bean responsible of the registration to
 * TravelDream
 */

@ManagedBean(name = "RegistrationManagedBean")
@RequestScoped
public class RegistrationManagedBean {

    @EJB
    private RegistrationInterface registration;

    private RegistrationDto registrationDto;

    /**
     * Constructor of the class
     */
    public RegistrationManagedBean() {
        registrationDto = new RegistrationDto();
    }

    /**
     * This method provides to the user the possibility to register
     *
     * @return the JSF reference for redirect
     */
    public String register() {
        List<RegistrationInterface.RegistrationInputError> registrationInputErrors;
        registrationInputErrors = registration.register(registrationDto);
        if (registrationInputErrors.size() != 0) {
            communicateErrors(registrationInputErrors);
            return "/registration?faces-redirect = true";
        }
        return "/index?faces-redirect = true";
    }

    //Private method that display the errors to the user

    private void communicateErrors(List<RegistrationInterface.RegistrationInputError> registrationInputErrors) {

        if (registrationInputErrors.contains(RegistrationInterface.RegistrationInputError.WRONG_EMAIL_PATTERN))
            FacesContext.getCurrentInstance().addMessage("registration:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid email",
                            "invalid email"));
        if (registrationInputErrors.contains(RegistrationInterface.RegistrationInputError.WRONG_FIRSTNAME))
            FacesContext.getCurrentInstance().addMessage("registration:firstName",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid firstname",
                            "invalid firstname"));
        if (registrationInputErrors.contains(RegistrationInterface.RegistrationInputError.WRONG_LASTNAME))
            FacesContext.getCurrentInstance().addMessage("registration:lastName",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid lastname",
                            "invalid lastname"));
        if (registrationInputErrors.contains(RegistrationInterface.RegistrationInputError.WRONG_PASSWORD))
            FacesContext.getCurrentInstance().addMessage("registration:password",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid password",
                            "letters and numbers, 6-20 char"));
        if (registrationInputErrors.contains(RegistrationInterface.RegistrationInputError.ALREADY_REGISTERED))
            FacesContext.getCurrentInstance().addMessage("registration:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "already used email",
                            "email already in use"));
        if(registrationInputErrors.contains(RegistrationInterface.RegistrationInputError.DIFFERENT_PASSWORDS))
            FacesContext.getCurrentInstance().addMessage("registration:password",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "password mismatch",
                            "confirm password doesn't match password"));
    }

    /*
  * GETTERS AND SETTERS
  * */

    public RegistrationDto getUserDto() {
        return registrationDto;
    }

    public void setUserDto(RegistrationDto registrationDto) {
        this.registrationDto = registrationDto;
    }

}
