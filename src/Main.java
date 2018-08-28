import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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
        ActiveXComponent application = new ActiveXComponent("hMailServer.Application");
        System.out.println("hMailServer Library Loaded");
        Dispatch.call(application,"Authenticate","Administrator","123456");
        Variant variant = application.getProperty("Settings");
        Dispatch settings = variant.getDispatch();
        variant = Dispatch.get(settings,"Logging");
        Dispatch logging = variant.getDispatch();
        boolean t = true;
        Dispatch.call(logging,"EnableLiveLogging",t);

        launch(args);
    }

    public void loginForm()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Login login = loader.getController();
            login.setMain(this);
            primaryStage.setResizable(false);
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
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.WINDOW_MODAL);
        secondaryStage.initOwner(primaryStage);
        secondaryStage.setResizable(false);
        secondaryStage.setTitle("Send Message " + email);
        secondaryStage.setScene(new Scene(root));
        secondaryStage.show();
    }

    public void mailView(Session sendSession,Session recieveSession,String email,String password)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MailView.fxml"));
        BorderPane root = null;
        try {
            root = (BorderPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailView mailView = loader.getController();
        mailView.setSendSession(sendSession);
        mailView.setReceiveSession(recieveSession);
        mailView.setEmail(email);
        mailView.setPassword(password);
        mailView.setMain(this);
        mailView.getMessages();
        primaryStage.setTitle("Inbox of " + email);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
