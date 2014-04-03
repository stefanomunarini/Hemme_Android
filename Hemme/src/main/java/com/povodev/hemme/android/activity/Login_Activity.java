package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.Login_HttpRequest;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Login_Activity extends RoboFragmentActivity implements View.OnClickListener{

    private final String TAG = "Login_Activity";

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

        //TODO eliminare queste due righe
        mUsernameEditText.setText("ste");
        mPasswordEditText.setText("ste");

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
}
