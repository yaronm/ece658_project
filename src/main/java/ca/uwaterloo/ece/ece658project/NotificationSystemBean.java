package ca.uwaterloo.ece.ece658project;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class NotificationSystemBean{
    private int port = 587;
    private String host = "smtp.gmail.com";
    private String from = "ece658.potlucky@gmail.com";
    
    private String username = "ece658.potlucky@gmail.com";
    private String password = "yaronmilwid";
    private boolean debug = true;
    public void sendEmail(String to, String subject, String body){
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", true);
        Authenticator authenticator = null;
        
        props.put("mail.smtp.auth", true);
        authenticator = new Authenticator(){
            private PasswordAuthentication pa = new PasswordAuthentication(username, password);
            @Override
            public PasswordAuthentication getPasswordAuthentication(){
                return pa;
            }
        };
        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);
        MimeMessage message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(body);
            Transport.send(message);
        } catch(MessagingException ex){
            ex.printStackTrace();
        }
    }
}