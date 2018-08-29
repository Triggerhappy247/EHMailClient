import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLogin implements Initializable {

    @FXML
    private TextField password;
    @FXML
    private Button cancel;

    private MailView mailView;

    public void setMailView(MailView mailView) {
        this.mailView = mailView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void adminLogin()
    {
        ActiveXComponent application = new ActiveXComponent("hMailServer.Application");
        Dispatch.call(application,"Authenticate","Administrator",password.getText());
        Variant settings = Dispatch.get(application,"Settings");
        Dispatch setting = settings.getDispatch();
        Variant logging = Dispatch.get(setting,"Logging");
        Dispatch logs = logging.getDispatch();
        mailView.setLogPath(Dispatch.get(logs,"CurrentDefaultLog").getString());
        mailView.getOpenLogs().setVisible(true);
        mailView.getOpenLogs().setDisable(false);
        mailView.getCreateAccount().setVisible(true);
        mailView.getCreateAccount().setDisable(false);
        mailView.getAdminLogin().setDisable(true);
        closeWindow();
    }

    @FXML
    private void closeWindow()
    {
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
    }
}
