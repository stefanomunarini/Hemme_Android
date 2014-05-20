package com.povodev.hemme.android.location;

import android.content.Context;
import android.content.SharedPreferences;

import com.povodev.hemme.android.utils.SessionManagement;

/**
 * Created by Stefano on 19/05/14.
 */
public class LocationVariables {

    public final static String LATITUDE_SHAREDPREFERENCES = "Latitude";
    public final static String LONGITUDE_SHAREDPREFERENCES = "Latitude";
    public final static String RADIUS_SHAREDPREFERENCES = "Latitude";

    public static double getLat(Context context) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        return Double.longBitsToDouble(preferences.getLong(LATITUDE_SHAREDPREFERENCES,0));
    }

    public static void setLat(Context context, double lat) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        preferences.edit().putFloat(LATITUDE_SHAREDPREFERENCES,Double.doubleToRawLongBits(lat)).commit();
    }

    public static double getLon(Context context) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        return Double.longBitsToDouble(preferences.getLong(LONGITUDE_SHAREDPREFERENCES,0));
    }

    public static void setLon(Context context, double lon) {
        SharedPreferences preferences = SessionManagement.getPreferences(context);
        preferences.edit().putFloat(LONGITUDE_SHAREDPREFERENCES,Double.doubleToRawLongBits(lon)).commit();
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
