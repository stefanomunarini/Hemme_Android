package com.povodev.hemme.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.adapter.RegistrationRoleAdapter;
import com.povodev.hemme.android.bean.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Registration_Activity extends RoboFragmentActivity implements View.OnClickListener {

    @InjectView(R.id.name_edittext)         private EditText mNameEditText;
    @InjectView(R.id.surname_edittext)      private EditText mSurnameEditText;
    @InjectView(R.id.email_edittext)        private EditText mEmailEditText;
    @InjectView(R.id.password_edittext)     private EditText mPasswordEditText;
    @InjectView(R.id.registration_button)   private Button mRegistrationButton;
    @InjectView(R.id.role_spinner)          private Spinner mRoleSpinner;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        //new Registration_HttpRequest(this).execute();


        initComponents();
        //populateSpinner();
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
        Toast toast = Toast.makeText(getApplicationContext(),"onClick id:" + id,Toast.LENGTH_SHORT);
        toast.show();

        switch (id){
            case R.id.registration_button:
                //Toast toast = Toast.makeText(getApplicationContext(),"B.Pressed",Toast.LENGTH_SHORT);
                //toast.show();
                getValues();
                new Registration_HttpRequest(context).execute();
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

    private class Registration_HttpRequest extends AsyncTask<Void, Void, User> {

        public Registration_HttpRequest(Context context){
            progressDialog = new ProgressDialog(context);
        }

        ProgressDialog progressDialog;

        @Override
        protected User doInBackground(Void... params) {
            try {
                final String url = "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //User user = restTemplate.getForObject(url, User.class);
                return null;
            } catch (Exception e) {
                Log.e("MyActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog.setMessage("Registrazione in corso.");
            progressDialog.show();
            //showLoadingProgressDialog();
        }

        @Override
        protected void onPostExecute(User user) {
            //if (progressDialog.isShowing()) progressDialog.dismiss();
        }

    }
}
