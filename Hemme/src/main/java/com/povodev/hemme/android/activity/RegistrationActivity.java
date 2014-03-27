package com.povodev.hemme.android.activity;

import android.os.Bundle;

import com.povodev.hemme.android.R;

import roboguice.activity.RoboFragmentActivity;

/**
 * Created by Stefano on 27/03/14.
 */
public class RegistrationActivity extends RoboFragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
}
