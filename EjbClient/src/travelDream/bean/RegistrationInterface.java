package travelDream.bean;

import travelDream.dto.RegistrationDto;
import travelDream.dto.UserDto;

import javax.ejb.Local;
import java.util.List;

/**
 * Interface of the Registration EJB
 */

@Local
public interface RegistrationInterface {

    public enum RegistrationInputError{
        WRONG_EMAIL_PATTERN,ALREADY_REGISTERED,WRONG_FIRSTNAME,WRONG_LASTNAME,WRONG_PASSWORD,DIFFERENT_PASSWORDS
    }

    public List<RegistrationInputError> register(RegistrationDto registrationDto);

}
