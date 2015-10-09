package travelDream.entity.user;

import travelDream.dto.CustomPackageDto;
import travelDream.dto.RegistrationDto;
import travelDream.dto.UserDto;
import travelDream.entity.travelPackage.CustomPackage;
import travelDream.entity.travelPackage.PurchasedPackage;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity implementation class representing all the registered
 * subject of travelDream.
 */

@Entity
@Table(name = "users")
//Named queries for the entity User
@NamedQueries({
        @NamedQuery(
                name = User.FIND_ALL,
                query = "select u from User u order by u.registeredOn ASC"
        ),
        @NamedQuery(
                name = User.FIND_BY_EMAIL,
                query = "select u from User u where u.email like :userEmail"
        )
})
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    //Query names
    public static final String FIND_ALL = "User.findAll";
    public static final String FIND_BY_EMAIL = "User.findByEmail";


    //-----------------------------------------------------------//

    //Attributes

    @Id
    private String email;

    private String firstName;

    private String lastName;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredOn;

    @ElementCollection(targetClass = Group.class)
    @CollectionTable(name = "users_groups",
            joinColumns = @JoinColumn(name = "EMAIL"))
    @Enumerated(EnumType.STRING)
    @Column(name="GROUPNAME")
    private List<Group> groups;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="desired_package",
            joinColumns={@JoinColumn(name="user_mail", referencedColumnName="EMAIL")},
            inverseJoinColumns={@JoinColumn(name="custom_package_id", referencedColumnName="ID")})
    private List<CustomPackage> desiredPackages = new ArrayList<CustomPackage>();


    //-----------------------------------------------------------//

    //Constructors

    /**
     * Default constructor of the entity User
     */
    public User(){
        super();
    }

    /**
     * Constructor of the entity User
     * @param userDto dto that contains user data
     */
    public User(RegistrationDto userDto){
        email = userDto.getEmail();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(userDto.getPassword().getBytes());
            password = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        firstName = userDto.getFirstName();
        lastName = userDto.getLastName();
        registeredOn = new Date();
    }

    public User(UserDto userDto) {
        this.email = userDto.getEmail();
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.registeredOn = userDto.getRegisteredOn();
        for (CustomPackageDto customPackageDto: userDto.getDesiredPackages()) {
            this.getDesiredPackages().add(new CustomPackage(customPackageDto));
        }
    }


    //-----------------------------------------------------------//

    //Getter and setter


    /**
     * Method returning the User email
     * @return the user email
     */
    @Id
    public String getEmail() {
        return email;
    }

    /**
     * Method setting the User email
     * @param email the user email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method returning the User password
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<CustomPackage> getDesiredPackages() {
        return desiredPackages;
    }

    public void setDesiredPackages(List<CustomPackage> desiredPackages) {
        this.desiredPackages = desiredPackages;
    }

    public UserDto getDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail(this.email);
        userDto.setFirstName(this.firstName);
        userDto.setLastName(this.lastName);
        userDto.setRegisteredOn(this.registeredOn);
        return userDto;
    }

    //-----------------------------------------------------------//

    //toString, equals and hashCode

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", registeredOn=" + registeredOn +
                ", groups=" + groups +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (groups != null ? !groups.equals(user.groups) : user.groups != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (registeredOn != null ? !registeredOn.equals(user.registeredOn) : user.registeredOn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (registeredOn != null ? registeredOn.hashCode() : 0);
        result = 31 * result + (groups != null ? groups.hashCode() : 0);
        return result;
    }
}
