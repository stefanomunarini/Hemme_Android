package com.povodev.hemme.android.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Stefano on 22/05/14.
 */
public class LocationUtils {

    public static String getAddressFromCoordinates(Context context, double latitude, double longitude){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                return listAddresses.get(0).getAddressLine(0) + " " + listAddresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
