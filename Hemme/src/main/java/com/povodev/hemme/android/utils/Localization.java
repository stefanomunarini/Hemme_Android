package com.povodev.hemme.android.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.povodev.hemme.android.dialog.CustomAlertDialog;

import java.util.HashMap;

/**
 * Created by Stefano on 07/05/14.
 */
public class Localization {

    /*
         * Center point from where to start to check the radius
         */
    private static double latitude = 46;
    private static double longitude = 11;
    /*
     * The radius from the center point.
     * Outside it, fire the Intent
     */
    private static float radius = 1;

    public static LocationManager getLocationManager(Context context){
        return (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static void addProximityAlert(Context context){
        Intent i = new Intent("com.povodev.hemme.proximity_alert");
        PendingIntent pi = PendingIntent.getBroadcast(context, -1, i, 0);
        LocationManager lm = getLocationManager(context);
        lm.addProximityAlert(getLatitude(), getLongitude(), radius, -1, pi);
    }

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    public static HashMap<String,Double> getCoordinates(Context context){
        LocationManager lm = getLocationManager(context);
        boolean enabled = lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        if (enabled) {
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            String provider = lm.getBestProvider(criteria, false);

            Location location = lm.getLastKnownLocation(provider);

            if (location != null) {
                HashMap<String,Double> result = new HashMap<String, Double>(2);
                result.put(LATITUDE,location.getLatitude());
                result.put(LONGITUDE,location.getLongitude());

                return result;
            }
        } else {
            createAlertDialog(context);
        }

        return null;
    }

    private static final String alertDialogTitle = "Attiva GPS";
    private static final String alertDialogMessage = "Segnale GPS non disponibile o servizi di localizzazione spenti. Si prega di riprovare!";

    private static void createAlertDialog(Context context) {
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(context,alertDialogMessage,alertDialogTitle,1);
        customAlertDialog.show();
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        Localization.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        Localization.longitude = longitude;
    }
}
