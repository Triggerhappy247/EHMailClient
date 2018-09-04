import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

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

    private Session sendSession;

    public void setSendSession(Session sendSession) {
        this.sendSession = sendSession;
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
        Preferences userLogin = Preferences.userNodeForPackage(Main.class);
        userLogin.put("username",String.format("%s@mail.localserver.com",email.getText()));
        userLogin.put("password",password.getText());
        loginSession(String.format("%s@mail.localserver.com",email.getText()),password.getText());
    }

    public void loginSession(String email,String password)
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.localserver.com");

        Properties properties = new Properties();
        properties.put("mail.pop3.host", "mail.localserver.com");
        properties.put("mail.pop3.port", "110");

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        };

        Session receiveSession = Session.getInstance(properties, auth);
        Session sendSession = Session.getInstance(props, auth);
        setSendSession(sendSession);
        main.mailView(sendSession,receiveSession,email,password);
    }
    @FXML
    private void forgotPassword()
    {
        System.out.println("Forgot Password - Feature Under Development");
    }

}
