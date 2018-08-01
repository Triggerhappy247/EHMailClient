import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class RegistrationForm implements Initializable {

    @FXML
    private TextField name,email;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private void login()
    {
        System.out.println("Test");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email.getText(),password.getText());
                    }
                });
        main.messageDialog(session);
    }
}
