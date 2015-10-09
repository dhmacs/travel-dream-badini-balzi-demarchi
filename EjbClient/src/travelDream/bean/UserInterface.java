package travelDream.bean;

import travelDream.dto.UserDto;
import travelDream.exception.UserNotFoundException;

import java.util.List;

/**
 * Created by federico on 06/01/14.
 */
public interface UserInterface {

    public List<UserDto> getRegisteredUsers();

    public UserDto getUserByEmail(String userEmail) throws UserNotFoundException;

    public void delete(UserDto userDto);

}
