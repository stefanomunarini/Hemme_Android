package com.povodev.hemme.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.fragment.Fragment_Home;

import roboguice.activity.RoboFragmentActivity;

/**
 * Created by Stefano on 27/03/14.
 */
public class Home_Activity extends RoboFragmentActivity {

    //@InjectView(R.id.activity_home_title) TextView titolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment fragment_home = new Fragment_Home();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_home_container, fragment_home)
                .commit();


    }
}
