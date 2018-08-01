import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class RegistrationForm implements Initializable {

    @FXML
    TextField name,email;
    @FXML
    PasswordField password;
    @FXML
    Button login;
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
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getText()));
            message.setSubject("Test");
            message.setText("Test");
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }
}
