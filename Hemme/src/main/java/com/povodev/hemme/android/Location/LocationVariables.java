package com.povodev.hemme.android.location;

import android.content.Context;
import android.content.SharedPreferences;

import com.povodev.hemme.android.utils.SessionManagement;

/**
 * Created by Stefano on 19/05/14.
 */
public class LocationVariables {

    private float lat = 0;
    private float lon = 0;
    private int radius = 0;

    public final static String LATITUDE_SHAREDPREFERENCES = "Latitude";
    public final static String LONGITUDE_SHAREDPREFERENCES = "Latitude";
    public final static String RADIUS_SHAREDPREFERENCES = "Latitude";

    public static float getLat(Context context) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        return preferences.getFloat(LATITUDE_SHAREDPREFERENCES,0);
    }

    public static void setLat(Context context, float lat) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        preferences.edit().putFloat(LATITUDE_SHAREDPREFERENCES,lat).commit();
    }

    public static float getLon(Context context) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        return preferences.getFloat(LONGITUDE_SHAREDPREFERENCES,0);
    }

    public static void setLon(Context context, float lon) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        preferences.edit().putFloat(LONGITUDE_SHAREDPREFERENCES,lon).commit();
    }

    public static int getRadius(Context context) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        return preferences.getInt(RADIUS_SHAREDPREFERENCES, 0);
    }

    public static void setRadius(Context context, int radius) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        preferences.edit().putFloat(LATITUDE_SHAREDPREFERENCES,radius).commit();
    }
}
