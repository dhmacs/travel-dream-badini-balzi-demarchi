package travelDream.bean;

import travelDream.dto.RegistrationDto;
import travelDream.entity.user.Group;
import travelDream.entity.user.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is the implementation of the ejb responsible of the
 * registration of the user
 */

@Stateless
public class RegistrationBean implements RegistrationInterface {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method creates a new registered user
     *
     * @param registrationDto the data of the user to register
     * @return the list of possible errors
     */
    @Override
    public List<RegistrationInputError> register(RegistrationDto registrationDto) {
        //Validation of the dto
        List<RegistrationInputError> registrationInputErrors = this.checkRegistration(registrationDto);

        if (registrationInputErrors.size() == 0) {
            User newUser = new User(registrationDto);
            List<Group> groups = new ArrayList<Group>();
            groups.add(Group.USER);
            newUser.setGroups(groups);
            entityManager.persist(newUser);
        }
        return registrationInputErrors;
    }

    /**
     * This method validates a registration dto against wrong parameters value
     *
     * @param registrationDto the dto to be checked
     * @return the list of errors discovered during the validation
     */
    private List<RegistrationInputError> checkRegistration(RegistrationDto registrationDto) {
        List<RegistrationInputError> registrationInputErrors = new ArrayList<RegistrationInputError>();

        //Parameters and patterns

        String email = registrationDto.getEmail();
        String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        String firstName = registrationDto.getFirstName();
        String firstNamePattern = "(^[a-zA-Z]+$)";
        String lastName = registrationDto.getLastName();
        String lastNamePattern = "([a-zA-Z]+([\\s][a-zA-Z]+)*)";
        String password = registrationDto.getPassword();
        String passwordPattern = "((?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{6,20})$)";
        String confirmPassword = registrationDto.getConfirmPassword();

        //Validation

        if (email == null || email.isEmpty() || !email.matches(emailPattern))
            registrationInputErrors.add(RegistrationInputError.WRONG_EMAIL_PATTERN);

        if (firstName == null || firstName.isEmpty() || !firstName.matches(firstNamePattern))
            registrationInputErrors.add(RegistrationInputError.WRONG_FIRSTNAME);

        if (lastName == null || lastName.isEmpty() || !lastName.matches(lastNamePattern))
            registrationInputErrors.add(RegistrationInputError.WRONG_LASTNAME);

        if (password == null || password.isEmpty() || !password.matches(passwordPattern))
            registrationInputErrors.add(RegistrationInputError.WRONG_PASSWORD);

        if (password != null && confirmPassword != null && !password.equals(confirmPassword))
            registrationInputErrors.add(RegistrationInputError.DIFFERENT_PASSWORDS);

        if (email != null && !email.isEmpty() && email.matches(emailPattern)) {
            User user = entityManager.find(User.class, email);
            if (user != null)
                registrationInputErrors.add(RegistrationInputError.ALREADY_REGISTERED);
        }

        return registrationInputErrors;
    }
}
