package travelDream.bean;

import travelDream.dto.UserDto;
import travelDream.entity.user.Group;
import travelDream.entity.user.User;
import travelDream.exception.UserNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by federico on 06/01/14.
 */
@Stateless
public class UserBean implements UserInterface {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method return the list of all registered users
     * @return the list of all registered users
     */
    @Override
    public List<UserDto> getRegisteredUsers() {
        List entries = entityManager.createNamedQuery(User.FIND_ALL).getResultList();
        List<UserDto> userDtos = new ArrayList<UserDto>();
        while (!entries.isEmpty()) {
            UserDto userDto = new UserDto();
            User userEntry = (User) entries.remove(0);
            if (userEntry.getGroups().contains(Group.USER)) {
                userDto.setEmail(userEntry.getEmail());
                userDto.setFirstName(userEntry.getFirstName());
                userDto.setLastName(userEntry.getLastName());
                userDto.setRegisteredOn(userEntry.getRegisteredOn());
                userDtos.add(userDto);
            }
        }
        return userDtos;
    }

    /**
     * This method search a user by email and return him.
     * @param userEmail the email of the user.
     * @return the searched user if he is found, null otherwise.
     */
    @Override
    public UserDto getUserByEmail(String userEmail) throws UserNotFoundException{
        User user;
        try{
            user = (User) entityManager.createNamedQuery(User.FIND_BY_EMAIL).setParameter("userEmail", userEmail).getSingleResult();
            return user.getDto();
        }catch(NoResultException e){
            UserNotFoundException userNotFoundException = new UserNotFoundException();
            if (userEmail.isEmpty())
                userNotFoundException.setMessage("Please insert an email");
            else
                userNotFoundException.setMessage("User not found");
            throw userNotFoundException;
        }
    }

    /**
     * This method delete a user from the system.
     * @param userDto the user to delete.
     */
    @Override
    public void delete(UserDto userDto) {
        User userToDelete = find(userDto.getEmail());
        entityManager.remove(userToDelete);
    }

    private User find(String email) {
        return entityManager.find(User.class, email);
    }
}
