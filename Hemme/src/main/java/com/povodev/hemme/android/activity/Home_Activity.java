package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.dialog.CustomAlertDialog;
import com.povodev.hemme.android.fragment.Fragment_Home;
import com.povodev.hemme.android.utils.ConnectionChecker;

import roboguice.activity.RoboFragmentActivity;

/**
 * Created by Stefano on 27/03/14.
 */
public class Home_Activity extends RoboFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment fragment_home = new Fragment_Home();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_home_container, fragment_home)
                .commit();

        //if (!checkForANetwork(this)) createAlertDialog();

    }

    /*
     * Check for a network connection.
     * If none, "block" the app
     */
    private boolean checkForANetwork(Context context) {
        return ConnectionChecker.isNetworkAvailable(context);
    }

    //private AlertDialog.Builder builder;
    //private AlertDialog dialog;
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
            /*if (dialog!=null){
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }*/
            createAlertDialog();
        }
    }
}
