package com.povodev.hemme.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.povodev.hemme.android.R;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Login_Activity extends RoboFragmentActivity implements View.OnClickListener{

    @InjectView(R.id.username_edittext) private EditText mUsernameEditText;
    @InjectView(R.id.password_edittext) private EditText mPasswordEditText;
    @InjectView(R.id.login_button)      private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

        }
    }
}
