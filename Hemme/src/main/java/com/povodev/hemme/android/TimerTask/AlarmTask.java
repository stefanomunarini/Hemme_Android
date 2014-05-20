package com.povodev.hemme.android.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * 
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * 
 * This will run on it's own thread.
 */
public class AlarmTask implements Runnable {

    public static int intent_id = 1234;

	// The android system alarm manager
	private final AlarmManager am;
	// Your context to retrieve the alarm manager from
	private final Context context;

	public AlarmTask(Context context) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

    /*
     * Check patient position every 5 minutes
     * when he/she exit the area
     */
    private final static int timer_delay = (5 * 60 * 1000);

	@Override
	public void run() {
		// Request to start are service when the alarm date is upon us
		// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
		Intent intent = new Intent(context, NotifyService.class);
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, intent_id, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timer_delay, pendingIntent);
	}
}
