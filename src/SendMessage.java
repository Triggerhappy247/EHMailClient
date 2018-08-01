import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.ResourceBundle;

public class SendMessage implements Initializable {

    @FXML
    private TextField toField,subject;
    @FXML
    private TextArea messageField;
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void sendMessage()
    {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toField.getText()));
            message.setSubject(subject.getText());
            message.setText(messageField.getText());
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }
}
