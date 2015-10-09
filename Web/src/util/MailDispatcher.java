package util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.omg.CosNaming._NamingContextExtStub;
import travelDream.bean.UserInterface;
import travelDream.dto.CustomPackageDto;
import travelDream.dto.UserDto;
import travelDream.exception.UserNotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Created by federico on 28/01/14.
 */
@ManagedBean(name = "MailDispatcher")
@ViewScoped
public class MailDispatcher {

    @EJB
    private UserInterface userInterface;

    public void sendSharingEmail(String emailAddress, int packageID) {
        Email email = new SimpleEmail();
        UserDto currentUser = new UserDto();
        try {
            currentUser = userInterface.getUserByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("traveldreamse2@gmail.com", "Password91"));
        email.setSSLOnConnect(true);
        try {
            email.setFrom("traveldreamse2@gmail.com");
            email.setMsg("TravelDream greets. \n" + currentUser.getFirstName() + " " + currentUser.getLastName() +
                    " shared you a package: \nView the package at:\nhttp://localhost:8080/WebWeb/viewSharedPackage.xhtml?customPackageID=" +
                    packageID);
            email.addTo(emailAddress);
            email.setSubject("Travel package share!");
            email.send();
        } catch (EmailException e) {
            System.out.print("Failure while sending email");
        }
    }

    public void sendPurchaseEmail(String emailAddress, CustomPackageDto customPackageDto) {
        Email email = new SimpleEmail();
        String excursionMessage = null;
        UserDto currentUser = new UserDto();
        try {
            currentUser = userInterface.getUserByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("traveldreamse2@gmail.com", "Password91"));
        email.setSSLOnConnect(true);
        try {
            email.setFrom("traveldreamse2@gmail.com");
            String message ="Hello " + currentUser.getFirstName() + " " + currentUser.getLastName() + ". \n" +
                    "Thank you for buying from TravelDream; these are the details of the package you bought: \n" +
                    "Leaving flight:" + " " + customPackageDto.getLeavingFlight().getFlightNumber() + ", from " +
                    customPackageDto.getLeavingFlight().getDepartureAirport().getName() + " at " +
                    customPackageDto.getLeavingFlight().getDeparture() + " to " + customPackageDto.getLeavingFlight().
                    getArrivalAirport().getName() + " at " + customPackageDto.getLeavingFlight().getArrival() + ".\n" +
                    "Price: " + customPackageDto.getLeavingFlight().getPrice() + "$\n\n" +
                    "Returning flight:" + " " + customPackageDto.getReturningFlight().getFlightNumber() + ", from " +
                    customPackageDto.getReturningFlight().getDepartureAirport().getName() + " at " +
                    customPackageDto.getReturningFlight().getDeparture() + " to " + customPackageDto.getReturningFlight().
                    getArrivalAirport().getName() + " at " + customPackageDto.getReturningFlight().getArrival() + ".\n" +
                    "Price: " + customPackageDto.getReturningFlight().getPrice() + "$\n\n" +
                    "Accomodation:" + " " + customPackageDto.getAccomodation().getName() + "\nPrice: " +
                    customPackageDto.getAccomodation().getPrice() + "$\n\n";
            if(customPackageDto.getExcursion() != null){
                message.concat("Excursion: " + customPackageDto.getExcursion().getName() + "\nPrice: " +
                        customPackageDto.getExcursion().getPrice() + "$");
            }
            email.setMsg(message);

            email.addTo(emailAddress);
            email.setSubject("Travel package receipt");
            email.send();
        } catch (EmailException e) {
            System.out.print("Failure while sending email");
        }
    }
}
