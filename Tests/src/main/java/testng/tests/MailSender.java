/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testng.tests;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author employee
 */
public class MailSender {

    /**
     * Sets necessary system properties to send email.
     * 
     * @param properties
     * @param host
     * @param port
     * @param userName
     * @param password 
     */
    public static void setSMTPProperties(Properties properties, String host, String port,
            String userName, String password) {
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
    }

    /**
     * Attaches the specified file to email.
     * 
     * @param messageBodyPart
     * @param attachFiles
     * @return
     * @throws MessagingException 
     */
    public static Multipart addAttachmentToMail(MimeBodyPart messageBodyPart, String[] attachFiles) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }
        return multipart;
    }
    
    /**
     * Sends email with the specified attached file.
     * 
     * @param host
     * @param port
     * @param userName
     * @param password
     * @param toAddress
     * @param subject
     * @param message
     * @param attachFiles
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException 
     */

    public static void sendEmailWithAttachments(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String message, String[] attachFiles)
            throws AddressException, MessagingException, IOException {
        // sets SMTP server properties
        Properties properties = new Properties();
        setSMTPProperties(properties, host, port, userName, password);
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
        // sets the multi-part as e-mail's content
        msg.setContent(addAttachmentToMail(messageBodyPart, attachFiles));
        // sends the e-mail
        Transport.send(msg);
    }

    public static void main(String[] args) {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "qatesthorizon@gmail.com";
        String password = "Disney5411";

        // message info
        String mailTo = "tigrankhojoyan@mail.ru";
        String subject = "Horizon web vidoe test results";
        String message = "The test results of the Horizon web video player";

        // attachments
        String[] attachFiles = new String[1];
        attachFiles[0] = "src/main/resources/testFile.txt";

        try {
            sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
                    subject, message, attachFiles);
            System.out.println("Email is sent.");
        } catch (Exception ex) {
            System.out.println("Could not send the email.");
            ex.printStackTrace();
        }
    }
}
