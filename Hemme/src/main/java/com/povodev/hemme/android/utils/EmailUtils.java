package com.povodev.hemme.android.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.povodev.hemme.android.asynctask.GetPassword_HttpRequest;
import com.povodev.hemme.android.utils.mail.GMailSender;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Stefano on 29/04/14.
 */
public class EmailUtils {

    private static String title;
    private static String message;

    /*
     * Public method to call ONLY from {@See GetPassword_HttpRequest}
     * to recovery password (after have checked that the
     * email exists in the database.
     */
    public static void passwordRecovery(Context context, String recipient_email, String password) {

        message = createBodyMessage(password);

        title = "Recupero password HeMMe";

        sendEmail(recipient_email, title, message);

        Toast.makeText(context, "Email Inviata!", Toast.LENGTH_LONG).show();
    }

    /*
     * Public method to call ONLY when the patient exit the area
     * defined/marked by the tutor
     */
    public static void notifyThatPatientHasExitedTheArea(Context context, String recipient_email) {

        message = "Attenzione! Il paziente è uscito dall'area di sicurezza e si trova ora presso: " + getAddressFromCoordinates(context);

        title = "Attenzione!";

        sendEmail(recipient_email, title, message);

    }

    /*
     * Create the email body for password recovery service.
     * Can either be just text or mixed text/html
     */
    private static String createBodyMessage(String password) {
        message = "La tua password e': ";
        return message += password;
    }

    private static void sendEmail(final String recipient_email, final String title, final String message){
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

    private static String getAddressFromCoordinates(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if(null!=locations && null!=providerList && providerList.size()>0){
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    return listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
