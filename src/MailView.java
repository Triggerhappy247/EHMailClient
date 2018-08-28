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
    Button composeButton,logout;
    @FXML
    ListView messageList;
    @FXML
    TextArea messageArea;

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
        } catch (NullPointerException e) {
            return;
        }
        String[] info = selectedMessage.split(" - ");
        String message = String.format("From: %s" +
                "\nTo: %s" +
                "\nSubject: %s" +
                "\n%s\n",info[0],email,info[1],messageContentList.get(selectedMessageIndex));
        messageArea.setText(message);
    }
}
