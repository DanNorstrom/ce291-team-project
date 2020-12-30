package main;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
    private String recipient;
    private String fileChosenDir;
    private String sender = "smat.noreply@gmail.com";
    private String password = "Smatd123";
    public SendEmail(String to, String fileChosenDir){
        this.recipient = to;
        this.fileChosenDir = fileChosenDir;
        
        // Assuming you are sending email through gmail
        String hostAPI = "smtp.gmail.com";
        
        Properties props = new Properties();
        props.put("mail.smtp.host", hostAPI);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        //Login to email: Authentication to host server
        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(sender, password);
            }
        });
        
        try {
             Message message = new MimeMessage(session);
             
             //Set the sender
             message.setFrom(new InternetAddress(sender));
             
             //Set recipient
             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
             
             //Set Subject
             message.setSubject("University Statistics Report");
             
             // Create a message body 
             Multipart body = new MimeMultipart();
             BodyPart content = new MimeBodyPart();
             
             //Set text context
             content.setText("Sent from the SMAT –CE291T26");
             body.addBodyPart(content);
             
             //Add the pdf/image attachment
             content = new MimeBodyPart();
             content.setDataHandler(new DataHandler(new FileDataSource(fileChosenDir)));
             content.setFileName(fileChosenDir);
             body.addBodyPart(content);
             
             // Send the complete message parts
             message.setContent(body);
             
             // Send message
             Transport.send(message);
        }catch (MessagingException e) {throw new RuntimeException(e);}
    }
    public static void main(String[] args) {new SendEmail("cardinalarnav@gmail.com", System.getProperty("user.dir") + "Output.png");}
}