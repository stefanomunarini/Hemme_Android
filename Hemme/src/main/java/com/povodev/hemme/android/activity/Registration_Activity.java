package com.povodev.hemme.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.adapter.SpinnerAdapter;
import com.povodev.hemme.android.bean.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Registration_Activity extends RoboFragmentActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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
        setComponentsListener();
    }

    private void initComponents() {
        initSpinner();
    }

    private void initSpinner() {

        List<String> SpinnerArray =  new ArrayList<String>();
        SpinnerArray.add("Tutor");
        SpinnerArray.add("Dottore");

        ArrayAdapter<String> adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, SpinnerArray);

        mRoleSpinner.setAdapter(adapter);
        mRoleSpinner.setOnItemSelectedListener(this);
    }

    private void setComponentsListener() {
        mRegistrationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.registration_button:
                new Registration_HttpRequest(context,getUser()).execute();
                break;
        }
    }

    private User getUser() {
        return getUserValues();
    }

    private String name;
    private String surname;
    private String email;
    private String password;
    private String imei;
    private int role;

    private User getUserValues() {
        name = mNameEditText.getText().toString();
        surname = mSurnameEditText.getText().toString();
        email = mEmailEditText.getText().toString();
        password = mPasswordEditText.getText().toString();
        role = mRoleSpinner.getSelectedItemPosition();
        imei = getImei();

        return setUserValues(name, surname, email, password, role, imei);
    }

    private User setUserValues(String name, String surname, String email, String password, int role, String imei) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setImei(imei);
        return user;
    }

    /*
         Retrieve device IMEI code
         @Return: the imei of the device
      */
    private String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    // Spinner listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class Registration_HttpRequest extends AsyncTask<Void, Void, User> {

        public Registration_HttpRequest(Context context, User user){
            progressDialog = new ProgressDialog(context);
            //progressDialog.setTitle("Benvenuto in HeMMe");
            progressDialog.setMessage("Registrazione in corso...");
        }
        ProgressDialog progressDialog;

        @Override
        protected User doInBackground(Void... params) {
            try {
                final String url = "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                User user = restTemplate.getForObject(url, User.class);
                return null;
            } catch (Exception e) {
                Log.e("MyActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(User user) {
            if (progressDialog.isShowing()) progressDialog.dismiss();
        }
    }
}
