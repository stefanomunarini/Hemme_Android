package com.povodev.hemme.android.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/*This is the Receiver for the Broadcast sent, here our app will be notified if the User is
* in the region specified by our proximity alert.
*/
public class ProximityReceiver extends BroadcastReceiver {

    /*
     *  The receiver gets the context & the intent that
     *  fired the broadcast as arg0 & agr1
     */
    @Override
    public void onReceive(Context arg0, Intent arg1) {

        Toast.makeText(arg0,"ProximityReceiver",Toast.LENGTH_SHORT).show();

        // Key for determining whether user is leaving or entering
        String k = LocationManager.KEY_PROXIMITY_ENTERING;

        //Gives whether the user is entering or leaving in boolean form
        boolean state = arg1.getBooleanExtra(k, false);

        if (state) {
            // Call the Notification Service or anything else that you would like to do here
            Toast.makeText(arg0, "Welcome to my Area", 600).show();
        } else {
            //Other custom Notification
            Toast.makeText(arg0, "Thank you for visiting my Area,come back again !!", 600).show();
        }
    }
}