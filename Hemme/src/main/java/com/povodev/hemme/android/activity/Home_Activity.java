package com.povodev.hemme.android.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.povodev.hemme.android.Location.Location_Service;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.dialog.CustomAlertDialog;
import com.povodev.hemme.android.fragment.Fragment_Home;
import com.povodev.hemme.android.management.SessionManagement;
import com.povodev.hemme.android.utils.ConnectionChecker;

import roboguice.activity.RoboFragmentActivity;

/**
 * Created by Stefano on 27/03/14.
 */
public class Home_Activity extends RoboFragmentActivity {

    /*
     * true if user is logged in, false otherwise
     */
    private boolean isUserLoggedIn = false;

    private Context context;

    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;

        isUserLoggedIn = SessionManagement.isUserLoggedIn(this);

        if (!isUserLoggedIn){
            Intent intent = new Intent(this,Login_Activity.class);
            startActivity(intent);
            finish();
        } else {
            Fragment fragment_home = new Fragment_Home();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_home_container, fragment_home)
                    .commit();
        }

        if (!checkForANetwork(this)) createAlertDialog();

        startService(new Intent(this, Location_Service.class));

        /*while (true){
            new Thread(new Runnable() {
                @Override
                public void run() {*/
                    //Localization.checkLocation(context);
                    /*try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/

    }


    /*
     * Check for a network connection.
     * If none, "block" the app
     */
    private boolean checkForANetwork(Context context) {
        return ConnectionChecker.isNetworkAvailable(context);
    }

    private final String alertDialogTitle = "Nessuna connessione";
    private final String alertDialogMessage = "Connettiti ad un network per utilizzare l'applicazione!";

    private void createAlertDialog() {
        CustomAlertDialog  customAlertDialog = new CustomAlertDialog(this,alertDialogMessage,alertDialogTitle,"Impostazioni",android.provider.Settings.ACTION_SETTINGS);
        customAlertDialog.show();
    }

    @Override
    public void onResume () {
        super.onResume();
        if(!checkForANetwork(this)){
            createAlertDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this, Login_Activity.class);
                SessionManagement.closeSession(this);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
