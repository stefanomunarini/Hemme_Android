package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.adapter.RegistrationRoleAdapter;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class RegistrationActivity extends RoboFragmentActivity implements View.OnClickListener {

    @InjectView(R.id.name_edittext)         private EditText mNameEditText;
    @InjectView(R.id.surname_edittext)      private EditText mSurnameEditText;
    @InjectView(R.id.email_edittext)        private EditText mEmailEditText;
    @InjectView(R.id.password_edittext)     private EditText mPasswordEditText;
    @InjectView(R.id.registration_button)   private Button mRegistrationButton;
    @InjectView(R.id.role_spinner)          private Spinner mRoleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initComponents();
        populateSpinner();
    }

    private void populateSpinner() {
        mRoleSpinner.setAdapter(new RegistrationRoleAdapter());
    }

    private void initComponents() {
        mRegistrationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.registration_button:
                getValues();
                break;
        }
    }

    private String name;
    private String surname;
    private String email;
    private String password;
    private String imei;
    private int role;
    private void getValues() {
        name = mNameEditText.getText().toString();
        surname = mSurnameEditText.getText().toString();
        email = mEmailEditText.getText().toString();
        password = mPasswordEditText.getText().toString();
        role = mRoleSpinner.getSelectedItemPosition();
        imei = getImei();
    }

    private String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
