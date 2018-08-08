import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.Session;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception{
        setPrimaryStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Login login = loader.getController();
        login.setMain(this);
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void registrationForm()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration Form.fxml"));
        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegistrationForm registrationForm = loader.getController();
        primaryStage.setTitle("REGISTER");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void messageDialog(Session session,String email)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SendMessage.fxml"));
        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessage sendMsg = loader.getController();
        sendMsg.setSession(session);
        sendMsg.setEmail(email);
        primaryStage.setTitle("Send Message");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
