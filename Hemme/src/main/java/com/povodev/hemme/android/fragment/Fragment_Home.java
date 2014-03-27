package com.povodev.hemme.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Login_Activity;
import com.povodev.hemme.android.activity.Registration_Activity;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Fragment_Home extends RoboFragment implements View.OnClickListener {

    @InjectView(R.id.login_button)              private Button mLoginButton;
    @InjectView(R.id.registration_button)       private Button mRegistrationButton;
    @InjectView(R.id.password_forget_textview)  private TextView mPasswordForgetTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initComponents() {
        mLoginButton.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);
        mPasswordForgetTextView.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id){
            case R.id.login_button:
                intent = new Intent(this.getActivity(),Login_Activity.class);
                redirect(intent);
                break;
            case R.id.registration_button:
                intent = new Intent(this.getActivity(),Registration_Activity.class);
                redirect(intent);
                break;
            case R.id.password_forget_textview:
                intent = new Intent(this.getActivity(),Login_Activity.class);
                redirect(intent);
                break;

        }
    }

    private void redirect(Intent intent) {
        startActivity(intent);
        //getActivity().finish();
    }
}