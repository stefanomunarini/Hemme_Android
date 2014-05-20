package com.povodev.hemme.android.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.povodev.hemme.android.TimerTask.ScheduleClient;
import com.povodev.hemme.android.utils.VibratingToast;

/*This is the Receiver for the Broadcast sent, here our app will be notified if the User is
* in the region specified by our proximity alert.
*/
public class ProximityReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    /*
     *  The receiver gets the context & the intent that
     *  fired the broadcast as arg0 & agr1
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // Key for determining whether user is leaving or entering
        String k = LocationManager.KEY_PROXIMITY_ENTERING;

        //Gives whether the user is entering or leaving in boolean form
        boolean state = intent.getBooleanExtra(k, false);

        if (state) {
            // Call the Notification Service or anything else that you would like to do here
            new VibratingToast(context, "Welcome to my Area", Toast.LENGTH_SHORT);
            Log.d(ScheduleClient.TAG,"Welcome to my Area");
        } else {
            //Other custom Notification
            new VibratingToast(context, "Thank you for visiting my Area,come back again !!", Toast.LENGTH_SHORT).show();
            Log.d(ScheduleClient.TAG,"Thank you for visiting my Area,come back again !!");
        }

        notifyMe(context);

        /*NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);
        Notification notification = createNotification();
        notification.setLatestEventInfo(context,
                "Proximity Alert!", "You are near your point of interest.", pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, notification);*/
    }

    private void notifyMe(Context context) {

        Notification notification = createNotification();

        /* Intent intent = new Intent(this,MyClass.class);
        intent.putExtra("myExtra",myExtra);*/

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, null, 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(context,
                "Proximity Alert!", "You are near your point of interest.", contentIntent);

        notification.defaults |= Notification.DEFAULT_SOUND;

        //custom sound
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.siren);

        //custom vibration
        long[] vibrate = { 0, 100, 200, 300, 400, 500 };
        notification.vibrate = vibrate;

        //notification.defaults |= Notification.DEFAULT_VIBRATE;

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Send the notification to the system.
        notificationManager.notify(NOTIFICATION_ID, notification);

        // Stop the service when we are finished
        //stopSelf();
    }

    private Notification createNotification() {
        Notification notification = new Notification();
        notification.icon = android.R.drawable.ic_dialog_alert;
        notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;
        return notification;
    }

}