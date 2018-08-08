import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.mail.pop3.POP3Store;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.mail.*;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class Login implements Initializable {

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    TextField email;
    @FXML
    Label incorrect;
    @FXML
    PasswordField password;
    @FXML
    Button login,forgotPassword,register;

    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void register()
    {
        main.registrationForm();
    }

    @FXML
    private void login()
    {
        incorrect.setVisible(false);
        ActiveXComponent application = new ActiveXComponent("hMailServer.Application");
        Dispatch.call(application,"Authenticate","Administrator","123456");
        Variant variant = application.getProperty("Domains");
        Dispatch domains = variant.getDispatch();
        variant = Dispatch.call(domains,"ItemByName","mail.localserver.com");
        Dispatch domain = variant.getDispatch();
        variant = Dispatch.get(domain,"Accounts");
        Dispatch accounts = variant.getDispatch();
        variant = Dispatch.call(accounts,"ItemByAddress",String.format("%s@mail.localserver.com",email.getText()));
        Dispatch account = variant.getDispatch();
        if(!Dispatch.call(account,"ValidatePassword",password.getText()).getBoolean())
        {
            incorrect.setVisible(true);
            return;
        }
        else {
            printMessages();
        }

        String host = "mail.localserver.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        /*props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");*/

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(String.format("%s@mail.localserver.com",email.getText()), password.getText());
            }
        };

        Session session = Session.getInstance(props, auth);
        setSession(session);
        main.messageDialog(session,String.format("%s@mail.localserver.com",email.getText()));

    }

    private void printMessages()
    {
        try{
            Properties properties = new Properties();
            properties.put("mail.pop3.host", "mail.localserver.com");
            properties.put("mail.pop3.port", "110");

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(String.format("%s@mail.localserver.com",email.getText()), password.getText());
                }
            };

            Session session = Session.getInstance(properties, auth);
            POP3Store emailStore = (POP3Store) session.getStore("pop3");
            emailStore.connect(String.format("%s@mail.localserver.com",email.getText()), password.getText());
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            System.out.println(String.format("INBOX - %d Messages",messages.length));
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
            }
            emailFolder.close(false);
            emailStore.close();

        }
        catch(NoSuchProviderException e) {e.printStackTrace();}
        catch(MessagingException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }
}
