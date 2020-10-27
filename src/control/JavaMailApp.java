package control;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

public class JavaMailApp {
	
	 public static void main(String[] args) {
		   System.out.println("testou uma classe java");
	}
	 
	 public static boolean isValidEmailAddress(String email) {
	        boolean result = true;
	        try {
	            InternetAddress emailAddr = new InternetAddress(email);
	            emailAddr.validate();
	        } catch (AddressException ex) {
	            result = false;
	        }
	        return result;
	    }
	 
}
