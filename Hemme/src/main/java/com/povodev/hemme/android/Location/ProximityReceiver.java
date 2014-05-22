package com.povodev.hemme.android.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.povodev.hemme.android.TimerTask.AlarmTask;
import com.povodev.hemme.android.TimerTask.ScheduleClient;
import com.povodev.hemme.android.asynctask.GetLocationVariables_HttpRequest;
import com.povodev.hemme.android.asynctask.GetTutorEmail_HttpRequest;
import com.povodev.hemme.android.utils.SessionManagement;

/*
* This is the Receiver for the Broadcast sent, here our app will be notified if the User is
* in the region specified by our proximity alert.
*/
public class ProximityReceiver extends BroadcastReceiver {

    /*
     *  The receiver gets the context & the intent that
     *  fired the broadcast as arg0 & agr1
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(GetLocationVariables_HttpRequest.TAG,"onReceive ProximityReceiver");

        // Key for determining whether user is leaving or entering
        String k = LocationManager.KEY_PROXIMITY_ENTERING;

        //Gives whether the user is entering or leaving in boolean form
        boolean state = intent.getBooleanExtra(k, false);

        if (state) {

            //TODO testare se funziona
            cancelAlarm(context);



            // Call the Notification Service or anything else that you would like to do here
            Log.d(GetLocationVariables_HttpRequest.TAG, "Welcome to my Area");
        } else {

            //The patient has left the area
            notifyMe(context);

            Log.d(GetLocationVariables_HttpRequest.TAG,"Thank you for visiting my Area,come back again !!");
        }
    }

    private void cancelAlarm(Context context) {
        PendingIntent psender = PendingIntent.getBroadcast(context, AlarmTask.intent_id, new Intent(), PendingIntent.FLAG_NO_CREATE);
        if (psender!=null) {
            AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            am.cancel(psender);
        }
    }

    private ScheduleClient scheduleClient;

    private void notifyMe(Context context) {

        new GetTutorEmail_HttpRequest(context, SessionManagement.getUserInSession(context).getId()).execute();

        //TODO testare
        //setta un allarme ogni 5 minuti se si esce dalla zona indicata
        scheduleClient = new ScheduleClient(context);
        scheduleClient.doBindService();
        scheduleClient.setAlarmForNotification();
        scheduleClient.doUnbindService();

    }
}