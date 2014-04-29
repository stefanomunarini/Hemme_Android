package com.povodev.hemme.android.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.povodev.hemme.android.asynctask.GetPassword_HttpRequest;
import com.povodev.hemme.android.utils.mail.GMailSender;

/**
 * Created by Stefano on 29/04/14.
 */
public class PasswordRecovery {

    private static final String title = "Recupero password HeMMe";
    private static String message = "La tua password e': ";

    /*
     * Public method to call ONLY from {@See GetPassword_HttpRequest}
     * to recovery password (after have checked that the
     * email exists in the database.
     */
    public static void passwordRecovery(Context context, String recipient_email, String password) {

        createBodyMessage(password);

        sendEmail(recipient_email);

        Toast.makeText(context, "Email Inviata!", Toast.LENGTH_LONG).show();
    }

    /*
     * Create the email body for password recovery service.
     * Can either be just text or mixed text/html
     */
    private static void createBodyMessage(String password) {
        message += password;
    }

    private static void sendEmail(final String recipient_email){
        final GMailSender sender = new GMailSender();
        new AsyncTask<Void, Void, Void>() {             //crea un AsyncTask per inviare la mail
            @Override
            public Void doInBackground(Void... arg) {
                try {
                    sender.sendMail(title,
                            message,
                            recipient_email);
                } catch (Exception e) {
                    Log.e(GetPassword_HttpRequest.TAG, e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }
}
