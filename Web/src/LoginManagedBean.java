import javafx.scene.Group;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Managed bean responsible of the authentication of the
 * registered user
 */
@ManagedBean(name = "LoginManagedBean")
@ViewScoped
public class LoginManagedBean {
    private String email;
    private String password;
    private String requestedURL;
    private boolean isAdmin = false;

    @PostConstruct
    public void init() {

        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        this.requestedURL = (String) externalContext.getRequestMap()
                .get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (this.requestedURL == null) {
            String baseUrl = "/user/home.xhtml";
            if (isAdmin) baseUrl = "/admin/home.xhtml";
            this.requestedURL = externalContext.getRequestContextPath() + baseUrl;
        } else {
            String originalQuery = (String) externalContext.getRequestMap()
                    .get(RequestDispatcher.FORWARD_QUERY_STRING);
            if (originalQuery != null) {
                this.requestedURL += "?" + originalQuery;
            }
        }
        if (requestedURL.contains("admin")) {
            setAdminLogin();
        }
    }

    /**
     * This method offers the possibility to login
     *
     * @throws IOException
     */
    public void login() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            if (externalContext.getRemoteUser() != null) {
                request.logout();
            }
            request.login(this.email, this.password);
            if (externalContext.isUserInRole("ADMIN") && requestedURL.contains("user")) {
                context.addMessage("login:email", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Not Authorized", "Not authorized"));
                request.logout();
            } else if (externalContext.isUserInRole("USER") && requestedURL.contains("admin")) {
                context.addMessage("login:email", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Not Authorized", "Not authorized"));
                request.logout();
            } else {
                externalContext.redirect(this.requestedURL);
            }

        } catch (ServletException e) {
            context.addMessage("login:email", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Wrong credentials", "Wrong email or password"));
        }
    }

    /**
     * This method offers the possibility to logout
     *
     * @return the JSF reference for redirect
     */
    public String logout() {
        String result = "/index.xhtml?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method offers the possibility to navigate to the admin area login
     *
     * @return the JSF reference for redirect
     */
    public String adminAccess() {
        String result = "/admin/home?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
   * GETTERS AND SETTERS
   * */

    public void logoutUnauthorized() {
        this.logout();
    }

    public void setAdminLogin() {
        isAdmin = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
