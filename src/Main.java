import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.Session;
import java.io.IOException;

public class Main extends Application {

    private Stage mainStage;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setMainStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistrationForm.fxml"));
        AnchorPane root = loader.load();
        RegistrationForm regForm = loader.getController();
        regForm.setMain(this);
        primaryStage.setTitle("Register/Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void messageDialog(Session session) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SendMessage.fxml"));
            AnchorPane messageDialog = loader.load();
            SendMessage sendMsg = loader.getController();
            sendMsg.print();
            sendMsg.setSession(session);

            mainStage.hide();
            Scene scene = new Scene(messageDialog);
            mainStage.setScene(scene);
            mainStage.setTitle("Send a Message");
            mainStage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
