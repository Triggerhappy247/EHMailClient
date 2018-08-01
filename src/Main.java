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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration Form.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        RegistrationForm regForm = loader.getController();
        regForm.setMain(this);
        stage.setTitle("Register/Login");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void messageDialog(Session session)
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
        primaryStage.setTitle("Send Message");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
