import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationForm implements Initializable {

    @FXML
    private TextField fname,lname,email;
    @FXML
    private PasswordField password;
    @FXML
    private Button cancel;

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void register()
    {
        ActiveXComponent application = new ActiveXComponent("hMailServer.Application");
        System.out.println("hMailServer Library Loaded");
        Dispatch.call(application,"Authenticate","Administrator","123456");
        Variant variant = application.getProperty("Domains");
        Dispatch domains = variant.getDispatch();
        variant = Dispatch.call(domains,"ItemByName","mail.localserver.com");
        Dispatch domain = variant.getDispatch();
        variant = Dispatch.get(domain,"Accounts");
        Dispatch accounts = variant.getDispatch();
        variant = Dispatch.call(accounts,"Add");
        Dispatch account = variant.getDispatch();
        Dispatch.put(account,"Address",String.format("%s@mail.localserver.com",email.getText()));
        Dispatch.put(account,"Password",password.getText());
        Dispatch.put(account,"PersonFirstName",fname.getText());
        Dispatch.put(account,"PersonLastName",lname.getText());
        boolean t = true;
        Dispatch.put(account,"Active",t);
        Dispatch.put(account,"MaxSize",250);
        Dispatch.call(account,"Save");
        closeWindow();
    }

    @FXML
    private void closeWindow()
    {
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
    }
}
