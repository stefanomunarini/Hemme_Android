package com.povodev.hemme.android.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.GetPassword_HttpRequest;
import com.povodev.hemme.android.asynctask.Login_HttpRequest;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Login_Activity extends RoboFragmentActivity implements View.OnClickListener{

    private final String TAG = "Login_Activity";

    @InjectView(R.id.username_edittext)         private EditText mUsernameEditText;
    @InjectView(R.id.password_edittext)         private EditText mPasswordEditText;
    @InjectView(R.id.login_button)              private Button mLoginButton;
    @InjectView(R.id.registration_button)       private Button mRegistrationButton;
    @InjectView(R.id.password_forget_button)    private Button mPasswordForgetButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = this;

        SessionManagement.closeSession(this);

        //TODO eliminare queste due righe
        mUsernameEditText.setText("ssteee@gmail.com");
        mPasswordEditText.setText("ste");

        setComponentsListener();
    }

    private void setComponentsListener() {
        mLoginButton.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);
        mPasswordForgetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_button:
                new Login_HttpRequest(context,getUsername(),getPassword(),getImei()).execute();
                break;
            case R.id.registration_button:
                Intent intent = new Intent(this,Registration_Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.password_forget_button:
                openDialog(context);
                break;
        }
    }

    private void openDialog(final Context context) {

        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        //TODO eliminare
        input.setText("ssteee@gmail.com");

        final String dialogTitle = "Recupero password";
        final String dialogMessage = "Inserisci email";

        new AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String email = input.getText().toString();
                        new GetPassword_HttpRequest(context,email).execute();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
        }).show();
    }


    private String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    private String getUsername(){
        return mUsernameEditText.getText().toString();
    }
    private String getPassword(){
        return mPasswordEditText.getText().toString();
    }
}
