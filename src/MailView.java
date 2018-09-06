import com.sun.jna.Native;
import com.sun.jna.platform.win32.Crypt32Util;
import com.sun.mail.pop3.POP3Store;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    @FXML
    private MenuItem addAccount;
    @FXML
    private Menu accountMenu;
    private int accountNumber;
    private String logPath;
    private Login login;
    private String emails[],passwords[];
    private int accNumber[];

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public void setPasswords(String[] passwords) {
        this.passwords = passwords;
    }

    public void setAccNumber(int[] accNumber) {
        this.accNumber = accNumber;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

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
    private void addAccount(){
        main.modalLoginform();
    }

    @FXML
    private void logout()
    {
        Preferences userLogin = Preferences.userNodeForPackage(Main.class);
        byte nullData[] = new byte[1];
        int numOfAccounts = userLogin.getInt("accountNumber",0);
        for (int i = accountNumber; i < numOfAccounts; i++) {
            userLogin.put(String.format("username%d",i),userLogin.get(String.format("username%d",i+1),null));
            userLogin.putByteArray(String.format("password%d",i),userLogin.getByteArray(String.format("password%d",i+1),nullData));
        }
        userLogin.remove(String.format("username%d",numOfAccounts));
        userLogin.remove(String.format("password%d",numOfAccounts));
        userLogin.putInt("accountNumber",--numOfAccounts);
        updateEandP();
        if(numOfAccounts == 0)
            main.loginForm();
        else
        {
            login.loginSession(emails[1],passwords[1],accNumber[1]);
        }
    }

    private void updateEandP()
    {
        Preferences userLogin = Preferences.userNodeForPackage(Main.class);
        int numOfAccounts = userLogin.getInt("accountNumber",0);
        byte nullData[] = new byte[1];
        byte passByte[];
        String emails[] = new String[numOfAccounts+1];
        String passwords[] = new String[numOfAccounts+1];
        int accNumber[] = new int[numOfAccounts+1];
        for (int i = 1; i <= numOfAccounts; i++) {
            passByte = Crypt32Util.cryptUnprotectData(userLogin.getByteArray(String.format("password%d",i),nullData));
            emails[i] = userLogin.get(String.format("username%d",i),null);
            passwords[i] = Native.toString(passByte);
            accNumber[i] = i;
        }

        setEmails(emails);
        setPasswords(passwords);
        setAccNumber(accNumber);
    }

    public void fillAccountsMenu()
    {
        updateEandP();
        MenuItem emailMenuItem;
        for (int i : accNumber) {
            if(i == accountNumber || i == 0)
                continue;
            emailMenuItem = new MenuItem(emails[i]);
            emailMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    login.loginSession(emails[i],passwords[i],i);
                }
            });
            accountMenu.getItems().add(emailMenuItem);
        }
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
                return;
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
        } catch (ArrayIndexOutOfBoundsException e){
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

    @FXML
    private void aboutPage()
    {
        main.aboutPage();
    }
}
