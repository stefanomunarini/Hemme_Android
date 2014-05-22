package com.povodev.hemme.android.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.povodev.hemme.android.asynctask.GetTutorEmail_HttpRequest;
import com.povodev.hemme.android.utils.SessionManagement;

/**
 * Created by Stefano on 15/05/14.
 */
public class LocationChecker {

    private static final String TAG = "LocationChecker";

    private static Location location;

    public static void checkLocation(final Context context) {

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

                    Log.d(TAG, "onLocationChanged {Lat: " + location.getLatitude() + "} {Long: " + location.getLongitude() + "}");
                    Log.d(TAG, "Localizzazione acquisita");
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    Log.d(TAG, "onStatusChanged");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.d(TAG, "onProviderDisabled");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.d(TAG, "onProviderEnabled");
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

    public static void checkLocation(final Context context, final double lat, final double lon, final int radius) {

        Log.d(TAG,"checkLocation");

        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (location == null) {
            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    // Called when a new location is found by the network
                    // location provider.
                    locationManager.removeUpdates(this);

                    if (distFrom(lat,lon,location.getLatitude(),location.getLongitude())>=radius){
                        Log.d(TAG, "distFrom >= radius");
                        new GetTutorEmail_HttpRequest(context, SessionManagement.getUserInSession(context).getId()).execute();
                    }

                    //new GetTutorEmail_HttpRequest(context, SessionManagement.getUserInSession(context).getId()).execute();

                    Log.d(TAG, "onLocationChanged {Lat: " + location.getLatitude() + "} {Long: " + location.getLongitude() + "}");
                    Log.d(TAG, "{Lat impostata " + lat + "} {Long impostata: " + lon + "}");
                    Log.d(TAG, "Localizzazione acquisita");
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    Log.d(TAG, "onStatusChanged");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.d(TAG, "onProviderDisabled");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.d(TAG, "onProviderEnabled");
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

    public static int distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        Log.d(TAG,"Distanza tra posizione attuale ed impostata: " + (int) (dist * meterConversion));

        return (int) (dist * meterConversion);
    }
}
