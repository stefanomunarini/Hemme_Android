package com.povodev.hemme.android.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.GetCoordinatesFromGoogleMapApi_HttpRequest;
import com.povodev.hemme.android.dialog.CustomAlertDialog;
import com.povodev.hemme.android.fragment.Fragment_Home;
import com.povodev.hemme.android.utils.ConnectionChecker;
import com.povodev.hemme.android.utils.SessionManagement;

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
        inflater.inflate(R.menu.home_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                intent = new Intent(this, Login_Activity.class);
                SessionManagement.closeSession(this);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_imposta_range:
                createDialog();
                return true;
            case R.id.action_associa_dispositivo:
                intent = new Intent(this, Associa_Dispositivi.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText viaEditText = new EditText(this);
        viaEditText.setHint("Via");

        final EditText numeroCivicoEditText = new EditText(this);
        numeroCivicoEditText.setHint("Num.");

        final EditText cittaEditText = new EditText(this);
        cittaEditText.setHint("Città");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        container.addView(viaEditText);
        container.addView(numeroCivicoEditText);
        container.addView(cittaEditText);

        builder.setTitle("Indirizzo")
                .setView(container)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String via = viaEditText.getText().toString();
                        String numerocivico = numeroCivicoEditText.getText().toString();
                        String citta = cittaEditText.getText().toString();

                        //TODO cancellare
                        via = "della Malvasia";
                        numerocivico = "101";
                        citta = "Trento";

                        new GetCoordinatesFromGoogleMapApi_HttpRequest(context,via,numerocivico,citta).execute();
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
