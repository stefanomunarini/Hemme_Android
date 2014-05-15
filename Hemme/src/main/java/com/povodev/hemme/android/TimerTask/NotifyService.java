package com.povodev.hemme.android.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.povodev.hemme.android.Location.LocationChecker;

/**
 * This service is started when an Alarm has been raised
 */
public class NotifyService extends Service {

	/**
	 * Class for clients to access
	 */
	public class ServiceBinder extends Binder {
		NotifyService getService() {
			return NotifyService.this;
		}
	}

	// Unique id to identify the notification.
	private static final int NOTIFICATION = 123;
	// Name of an intent extra we can use to identify if this service was started to create a notification
	public static final String INTENT_NOTIFY = "com.Notification.task.INTENT_NOTIFY";

    @Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// If this service was started by out AlarmTask intent then we want to show our notification
		if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            checkLocation();
		
		// We don't care if this service is stopped as we have already delivered our notification
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients
	private final IBinder mBinder = new ServiceBinder();

	/*
     * checkLocation
	 */
	private void checkLocation() {

        /*
         * THE CODE HERE
         */

        LocationChecker locationChecker = new LocationChecker();
        locationChecker.checkLocation(this);
		
		// Stop the service when we are finished
		stopSelf();
	}
}