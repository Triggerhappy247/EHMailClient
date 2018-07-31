import java.util.Scanner;
import java.io.Console;

public class SendMail {
    public static void main(String[] args) {
        //from,password,to,subject,message
        String email,recipient,subject,message;
        String password;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Email: ");
        email = scanner.nextLine();
        System.out.print("Password: ");
        password = scanner.nextLine();
        System.out.print("Recipient: ");
        recipient = scanner.nextLine();
        System.out.print("Subject: ");
        subject = scanner.nextLine();
        System.out.print("Message: ");
        message = scanner.nextLine();
        EmailConnect.send(email,password,recipient,subject,message);
    }
}