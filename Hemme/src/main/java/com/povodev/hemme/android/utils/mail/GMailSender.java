package com.povodev.hemme.android.utils.mail;

import android.util.Log;

import com.povodev.hemme.android.asynctask.GetPassword_HttpRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender extends javax.mail.Authenticator {

    private final String mailhost = "smtp.gmail.com";
    private final String sender_email = "hemme.android@gmail.com";
    private final String password = "povodevforhemme";
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender() {

        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.setProperty("mail.smtp.quitwait", "false");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.connectiontimeout", "2000");
        props.put("mail.smtp.timeout", "1000");

        session = Session.getInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(sender_email, password);
    }

    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        try{
            MimeMessage message = new MimeMessage(session);

            /*
             * Change "text" to "text/html" if you want a better look message body
             */
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/html"));
            message.setSender(new InternetAddress(sender_email));
            message.setSubject(subject);
            message.setDataHandler(handler);

            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        } catch(Exception e){
            Log.e(GetPassword_HttpRequest.TAG, e.getMessage(), e);
        }
    }

    private class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}