package travelDream.bean;

import org.junit.*;
import travelDream.dto.RegistrationDto;
import java.sql.*;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Test of the RegistrationBean
 */
public class RegistrationBeanTest {

    RegistrationBean registrationBean = new RegistrationBean();
    RegistrationDto wrongEmail = new RegistrationDto();
    RegistrationDto missingFirstname = new RegistrationDto();
    RegistrationDto missingLastname = new RegistrationDto();
    RegistrationDto missingPassword = new RegistrationDto();
    RegistrationDto passwordMismatch = new RegistrationDto();
    RegistrationDto alreadyRegistered = new RegistrationDto();
    Connection conn = null;

    @Before
    public void setUp(){
        wrongEmail.setEmail("wrongEmail");
        missingFirstname.setFirstName("");
        missingLastname.setLastName("");
        missingPassword.setPassword("");

        passwordMismatch.setPassword("firstPassword");
        passwordMismatch.setConfirmPassword("secondPassword");

        alreadyRegistered.setEmail("test@test.test");

        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "travelDream";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "user";
        String password = "password";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url+dbName,userName,password);
            System.out.println("Connesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrongEmail() throws Exception {
        List<RegistrationInterface.RegistrationInputError> wrongEmailError =
                registrationBean.register(wrongEmail);
        assertTrue(wrongEmailError.contains(RegistrationInterface.RegistrationInputError.WRONG_EMAIL_PATTERN));
        Statement stmt = conn.createStatement() ;
        String query = "select * from users where email = '" + wrongEmail.getEmail() + "';" ;
        ResultSet rs = stmt.executeQuery(query);
        assertTrue(rs.getRow() == 0);
    }

    @Test
    public void testMissingFirstname() throws Exception {
        List<RegistrationInterface.RegistrationInputError> missingFirstnameError =
                registrationBean.register(missingFirstname);
        assertTrue(missingFirstnameError.contains(RegistrationInterface.RegistrationInputError.WRONG_FIRSTNAME));
        Statement stmt = conn.createStatement() ;
        String query = "select * from users where firstname = '' ; " ;
        ResultSet rs = stmt.executeQuery(query);
        assertTrue(rs.getRow() == 0);
    }

    @Test
    public void testMissingLastname() throws Exception {
        List<RegistrationInterface.RegistrationInputError> missingLastnameError =
                registrationBean.register(missingLastname);
        assertTrue(missingLastnameError.contains(RegistrationInterface.RegistrationInputError.WRONG_LASTNAME));
        Statement stmt = conn.createStatement() ;
        String query = "select * from users where lastname = '' ; " ;
        ResultSet rs = stmt.executeQuery(query);
        assertTrue(rs.getRow() == 0);
    }

    @Test
    public void testMissingPassword() throws Exception {
        List<RegistrationInterface.RegistrationInputError> missingPasswordError =
                registrationBean.register(missingPassword);
        assertTrue(missingPasswordError.contains(RegistrationInterface.RegistrationInputError.WRONG_PASSWORD));
        Statement stmt = conn.createStatement() ;
        String query = "select * from users where password = '' ; " ;
        ResultSet rs = stmt.executeQuery(query);
        assertTrue(rs.getRow() == 0);
    }

    @Test
    public void testPasswordMismatch() throws Exception {
        Statement stmt1 = conn.createStatement() ;
        String query = "select * from users; " ;
        ResultSet rs1 = stmt1.executeQuery(query);
        List<RegistrationInterface.RegistrationInputError> passwordMismatchError =
                registrationBean.register(passwordMismatch);
        assertTrue(passwordMismatchError.contains(RegistrationInterface.RegistrationInputError.DIFFERENT_PASSWORDS));
        Statement stmt2 = conn.createStatement() ;
        ResultSet rs2 = stmt2.executeQuery(query);
        assertTrue(rs1.getRow() == rs2.getRow());
    }

    /*
    Ignored test - this test fails due to lack of application server injection
     */
    @Ignore
    @Test
    public void testAlreadyRegistered()throws Exception{
        Statement stmt1 = conn.createStatement() ;
        String query = "select * from users; " ;
        ResultSet rs1 = stmt1.executeQuery(query);

        Statement stmtInsert = conn.createStatement() ;
        String insert = "insert into users(EMAIL, FIRSTNAME, LASTNAME, PASSWORD, REGISTEREDON) " +
                "values ('test@test.test','test','test','test',null); " ;
        stmtInsert.executeUpdate(insert);

        List<RegistrationInterface.RegistrationInputError> alreadyRegisteredError =
                registrationBean.register(alreadyRegistered);
        assertTrue(alreadyRegisteredError.contains(RegistrationInterface.RegistrationInputError.ALREADY_REGISTERED));

        Statement stmt2 = conn.createStatement() ;
        ResultSet rs2 = stmt2.executeQuery(query);
        assertTrue(rs1.getRow() == rs2.getRow());

        Statement clean = conn.createStatement() ;
        String cleanQuery = "delete from users where email = 'test@test.test' ;" ;
        clean.executeQuery(cleanQuery);
    }

    @After
    public void tearDown(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Non connesso.");
    }
}
