import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Crypt32Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    Button login,register;

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
        byte passwordByte[] = Native.toByteArray(password.getText());
        int numOfAccounts = userLogin.getInt("accountNumber",0);
        numOfAccounts++;
        userLogin.putInt("accountNumber",numOfAccounts);
        userLogin.put(String.format("username%d",numOfAccounts),String.format("%s@mail.localserver.com",email.getText()));
        userLogin.putByteArray(String.format("password%d",numOfAccounts),Crypt32Util.cryptProtectData(passwordByte));
        Stage stage = (Stage)login.getScene().getWindow();
        if(stage.getModality() == Modality.WINDOW_MODAL)
            stage.close();
        loginSession(String.format("%s@mail.localserver.com",email.getText()),password.getText(),numOfAccounts);
    }

    public void loginSession(String email,String password,int accountNumber)
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.localserver.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.starttls.required",true);
        props.put("mail.smtp.ssl.trust","*");

        Properties properties = new Properties();
        properties.put("mail.pop3.host", "mail.localserver.com");
        properties.put("mail.pop3.port", 995);
        properties.put("mail.pop3.starttls.required",true);
        properties.put("mail.pop3.ssl.trust","*");


        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        };

        Session receiveSession = Session.getInstance(properties, auth);
        Session sendSession = Session.getInstance(props, auth);
        setSendSession(sendSession);
        main.mailView(sendSession,receiveSession,email,password,accountNumber,this);
    }


}
