import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
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

}
