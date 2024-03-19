package com.example.pet_adoption_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.Properties;

import javax.mail.Authenticator;
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

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private final Context mContext;
    private Session mSession;

    private String mEmail;
    private String mSubject;
    private String mMessage;

    private String codes;

    private ProgressDialog mProgressDialog;

    public JavaMailAPI(Context mContext, String mEmail, String mSubject, String mMessage, String code) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        this.codes = code;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, "Sending message", "Please wait...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressDialog.dismiss();
        Toast.makeText(mContext, "Message Sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587"); // Port for TLS/STARTTLS

        mSession = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.Email, Utils.Password);
                    }
                });

        try {
            MimeMessage mm = new MimeMessage(mSession);
            mm.setFrom(new InternetAddress(Utils.Email));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            mm.setSubject(mSubject);

            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mMessage);
            multipart.addBodyPart(messageBodyPart);


            // Code for adding attachments should go here

            mm.setContent(multipart);

            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
