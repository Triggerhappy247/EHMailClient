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

    public void loginForm()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Login login = loader.getController();
            login.setMain(this);
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void registrationForm()
    {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration Form.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        RegistrationForm registrationForm = loader.getController();
        registrationForm.setMain(this);
        primaryStage.setTitle("REGISTER");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        sendMsg.setMain(this);
        primaryStage.setTitle("Send Message");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
