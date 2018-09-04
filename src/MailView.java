import com.sun.mail.pop3.POP3Store;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MailView implements Initializable {

    private Main main;
    private Session sendSession,receiveSession;
    private String email,password;
    private ArrayList<String> messageContentList;

    public void setMessageContentList(ArrayList<String> messageContentList) {
        this.messageContentList = messageContentList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSendSession(Session sendSession) {
        this.sendSession = sendSession;
    }

    public void setReceiveSession(Session receiveSession) {
        this.receiveSession = receiveSession;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Button adminLogin,openLogs,createAccount;
    @FXML
    private ListView messageList;
    @FXML
    private TextArea messageArea;
    private String logPath;

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Button getOpenLogs() {
        return openLogs;
    }

    public Button getCreateAccount() {
        return createAccount;
    }

    public Button getAdminLogin() {
        return adminLogin;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void composeMessage()
    {
        main.messageDialog(sendSession,email);
    }

    @FXML
    private void logout()
    {
        Preferences userLogin = Preferences.userNodeForPackage(Main.class);
        byte nullData[] = new byte[1];
        userLogin.putByteArray("username",nullData);
        userLogin.putByteArray("password",nullData);

        main.loginForm();
    }

    public void getMessages()
    {
        try {
            ArrayList<String> messageContentList = new ArrayList<>();
            ArrayList<String> messageInfoList = new ArrayList<>();
            POP3Store emailStore = (POP3Store) receiveSession.getStore("pop3");
            emailStore.connect(email,password);
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            if (messages.length == 0)
            {
                messageList.getItems().clear();
                messageList.getItems().add("No Messages :(");
            }
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                String messageInfo = String.format("%s - %s",message.getFrom()[0],message.getSubject());
                messageInfoList.add(i,messageInfo);
                messageContentList.add(i,message.getContent().toString());
            }
            messageList.getItems().clear();
            messageList.getItems().addAll(messageInfoList);
            setMessageContentList(messageContentList);
            emailFolder.close(false);
            emailStore.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showMessage()
    {
        int selectedMessageIndex = 0;
        String selectedMessage = null;
        try {
            selectedMessageIndex = messageList.getSelectionModel().getSelectedIndex();
            selectedMessage = (String)messageList.getSelectionModel().getSelectedItem();
            String[] info = selectedMessage.split(" - ");
            String message = String.format("From: %s" +
                    "\nTo: %s" +
                    "\nSubject: %s" +
                    "\n%s\n",info[0],email,info[1],messageContentList.get(selectedMessageIndex));
            messageArea.setText(message);
        } catch (NullPointerException e) {
            return;
        }

    }
    @FXML
    private void openLogs()
    {
        try {
            Runtime.getRuntime().exec(String.format("explorer.exe \"%s\"",logPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void adminAuthenticate()
    {
        main.adminPassword(this);
    }

    @FXML
    private void createAccount()
    {
        main.registrationForm();
    }
}
