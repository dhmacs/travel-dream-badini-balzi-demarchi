package travelDream.exception;

/**
 * Created by alessandrobalzi on 13/01/14.
 */
public class UserNotFoundException extends Exception {

    private String message;

    public UserNotFoundException(){
        super();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
