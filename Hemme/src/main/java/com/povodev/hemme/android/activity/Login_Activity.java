package com.povodev.hemme.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Login_Activity extends RoboFragmentActivity implements View.OnClickListener{

    @InjectView(R.id.username_edittext) private EditText mUsernameEditText;
    @InjectView(R.id.password_edittext) private EditText mPasswordEditText;
    @InjectView(R.id.login_button)      private Button mLoginButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        setComponentsListener();

    }

    private void setComponentsListener() {
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_button:
                new Login_HttpRequest(context,getUsername(),getPassword()).execute();
                break;
        }
    }

    private String getUsername(){
        return mUsernameEditText.getText().toString();
    }

    private String getPassword(){
        return mPasswordEditText.getText().toString();
    }

    private class Login_HttpRequest extends AsyncTask<Void, Void, User> {
        private String username;
        private String password;

        public Login_HttpRequest(Context context, String username, String password){
            progressDialog = new ProgressDialog(context);
            //progressDialog.setTitle("Benvenuto in HeMMe");
            progressDialog.setMessage("Registrazione in corso...");

            this.username = username;
            this.password = password;
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
