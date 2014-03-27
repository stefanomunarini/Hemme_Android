package com.povodev.hemme.android.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.povodev.hemme.android.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Activity_Home extends RoboActivity {

    @InjectView(R.id.activity_home_title) TextView titolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //titolo.setText("Prova");
    }
}
