/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testng.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            final String userName, final String password, String[] toAddress,
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

        InternetAddress[] toAddresses = new InternetAddress[toAddress.length];
        for (int i = 0; i < toAddress.length; i++) {
            toAddresses[i] = new InternetAddress(toAddress[i]);
        }
        //InternetAddress[] toAddresses = {new InternetAddress(toAddress), new InternetAddress("lusine_p@instigatemobile.com")};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/plain");
        // sets the multi-part as e-mail's content
        msg.setContent(addAttachmentToMail(messageBodyPart, attachFiles));
        // sends the e-mail
        Transport.send(msg);
    }

    /**
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getAllFileData(String fileName) {
        byte[] data = null;
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
        } catch (Exception e) {
            System.err.println("Failed to get the content of the '" + fileName + "' file.");
        }
        String fileContent = null;
        try {
            fileContent = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileContent;
    }

    public static void main(String[] args) {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "qatesthorizon@gmail.com";
        String password = "Disney5411";

        // message info
        String[] mailTo = {"tigrankhojoyan@mail.ru", "lusine_p@instigatemobile.com"};
        String subject = "Horizon web vidoe test results";
        String message = "The test results of the Horizon web video player \n"
                + getAllFileData("src/main/resources/testsShortStatus.txt")
                + "\n See more details in attached testFile.txt file.";

        // attachments
        String[] attachFiles = new String[1];
        attachFiles[0] = "src/main/resources/testFile.txt";
        //attachFiles[1] = "src/main/resources/testsShortStatus.txt";

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
