package admin;

import travelDream.bean.UserInterface;
import travelDream.dto.UserDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by alessandrobalzi on 20/01/14.
 */
@ManagedBean(name = "UsersManagedBean")
@ViewScoped
public class UsersManagedBean {

    @EJB
    private UserInterface userInterface;

    private List<UserDto> userDtos;

    @PostConstruct
    public void init(){
        this.userDtos = userInterface.getRegisteredUsers();
    }

    /**
     * Remove the selected user from the system
     * @param userDto the selected user
     */
    public void delete(UserDto userDto){;
        userInterface.delete(userDto);
        userDtos = userInterface.getRegisteredUsers();
    }

    // Getters and Setters

    public List<UserDto> getUserDtos() {
        return userDtos;
    }

    public void setUserDtos(List<UserDto> userDtos) {
        this.userDtos = userDtos;
    }
}
