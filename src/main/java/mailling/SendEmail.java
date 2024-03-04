package mailling;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    public static void send(String toEmail , int code ) {
        String from = "oussama.sfaxi@esprit.tn"; // Change this to your sender email address
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("oussama.sfaxi@esprit.tn", "211JMT6879"); // Change this to your sender email password
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(from, "musemakers", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Forgot your password ");
            message.setText("This is the email body" + code );

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}