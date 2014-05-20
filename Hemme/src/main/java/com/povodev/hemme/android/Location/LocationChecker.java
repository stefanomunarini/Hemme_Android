package com.povodev.hemme.android.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.povodev.hemme.android.TimerTask.ScheduleClient;
import com.povodev.hemme.android.asynctask.GetTutorEmail_HttpRequest;
import com.povodev.hemme.android.utils.SessionManagement;

/**
 * Created by Stefano on 15/05/14.
 */
public class LocationChecker {

    private Location location;

    public void checkLocation(final Context context) {

        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (location == null) {
            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    // Called when a new location is found by the network
                    // location provider.
                    locationManager.removeUpdates(this);

                    new GetTutorEmail_HttpRequest(context, SessionManagement.getUserInSession(context).getId()).execute();

                    Log.d(ScheduleClient.TAG, "onLocationChanged {Lat: " + location.getLatitude() + "} {Long: " + location.getLongitude() + "}");
                    Log.d(ScheduleClient.TAG, "Localizzazione acquisita");
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    Log.d(ScheduleClient.TAG, "onStatusChanged");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.d(ScheduleClient.TAG, "onProviderDisabled");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.d(ScheduleClient.TAG, "onProviderEnabled");
                }
            };

            // Register the listener with the Location Manager to receive
            // location updates
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            else
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    private static final String PROX_ALERT_INTENT = "com.povodev.hemme.ProximityAlert";

    /*
     * Called by {@see GetLocationVariables_HttpRequest} when lat, lon and radius
     * have been downloaded from the server/database.
     *
     * Set a proximity alert.
     * @param context
     * @param latitude
     * @param longitude
     * @param POINT_RADIUS
     */
    public static void addProximityAlert(Context context, double latitude, double longitude, int POINT_RADIUS) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                -1, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );
    }
}
