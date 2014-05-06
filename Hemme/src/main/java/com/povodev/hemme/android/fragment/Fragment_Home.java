package com.povodev.hemme.android.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Diary;
import com.povodev.hemme.android.activity.Login_Activity;
import com.povodev.hemme.android.activity.New_ClinicaEvent;
import com.povodev.hemme.android.activity.PatientActivity;
import com.povodev.hemme.android.activity.clinicalFolder.ClinicalFolderListActivity;
import com.povodev.hemme.android.activity.memory_results.MemoryResultsListActivity;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Fragment_Home extends RoboFragment implements View.OnClickListener, LocationListener {

    @InjectView(R.id.login_button)                  private Button mLoginButton;
    @InjectView(R.id.newclinicalevent_button)       private Button mNewClinicalEventButton;
    @InjectView(R.id.clinicalfolder_button)         private Button mClinicalFolderButton;
    @InjectView(R.id.test_button)                   private Button mTestButton;
    @InjectView(R.id.visaulizza_diario)             private Button mDiaryButton;
    @InjectView(R.id.user_detail_home)              private TextView mUserDetailTextView;

    private User user;

    /*
     * true if user is logged in, false otherwise
     */
    private boolean isUserLoggedIn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = SessionManagement.getUserInSession(getActivity());
        isUserLoggedIn = SessionManagement.isUserLoggedIn(getActivity());

        if (!isUserLoggedIn){
            Intent intent = new Intent(getActivity(),Login_Activity.class);
            startActivity(intent);
            getActivity().finish();
        }




        proximityFunctions();






        /*
         * PAZIENTE
         */
        if (user.getRole()==2){
            Intent intent = new Intent(this.getActivity(),PatientActivity.class);
            redirect(intent);
        }
    }





    private LocationManager lm;
    private void proximityFunctions() {

        /*
         * Center point from where to start to check the radius
         */
        double latitude = 46, longitude = 11;
        /*
         * The radius from the center point.
         * Outside it, fire the Intent
         */
        float radius = 3000;

        lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        boolean enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            String provider = lm.getBestProvider(criteria, false);
            Location location = lm.getLastKnownLocation(provider);

            if (location != null) {
                Toast.makeText(getActivity(),"Latitude: " + location.getLatitude() + "    Longitudine: "
                        + location.getLatitude(), Toast.LENGTH_SHORT).show();
                onLocationChanged(location);
            } else {
                Toast.makeText(getActivity(),"Coordinates not available", Toast.LENGTH_SHORT).show();
                //latituteField.setText("Location not available");
                //longitudeField.setText("Location not available");
            }


        }



        Intent i = new Intent("com.povodev.hemme.proximity_alert");
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), -1, i, 0);

        lm.addProximityAlert(latitude, longitude, radius, -1, pi);
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

    /*
     *  Return the type of user
     *  0 TUTOR
     *  1 DOTTORE
     *  2 PAZIENTE
     */
    private int checkUserType(User user) {
        return user.getRole();
    }


    private void initComponents() {
        int userType = checkUserType(user);

        if(userType==0){
            mNewClinicalEventButton.setVisibility(View.GONE);
            mTestButton.setOnClickListener(this);
            mDiaryButton.setOnClickListener(this);
        } else if (userType==1){
            mTestButton.setVisibility(View.GONE);
            mDiaryButton.setVisibility(View.GONE);
            mNewClinicalEventButton.setOnClickListener(this);
        }

        mLoginButton.setOnClickListener(this);
        mClinicalFolderButton.setOnClickListener(this);

        mUserDetailTextView.setText("Benvenuto " + user.getName() + " " + user.getSurname());

        if (isUserLoggedIn){
            mLoginButton.setText("Logout");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id){
            case R.id.login_button:
                if (isUserLoggedIn){
                    SessionManagement.closeSession(getActivity());
                    intent = new Intent(this.getActivity(),Login_Activity.class);
                    redirect(intent);
                    getActivity().finish();
                }
                break;
            case R.id.newclinicalevent_button:
                intent = new Intent(this.getActivity(),New_ClinicaEvent.class);
                redirect(intent);
                break;
            case R.id.clinicalfolder_button:
                intent = new Intent(this.getActivity(),ClinicalFolderListActivity.class);
                redirect(intent);
                break;
            case R.id.test_button:
                intent = new Intent(this.getActivity(),MemoryResultsListActivity.class);
                redirect(intent);
                break;
            case R.id.visaulizza_diario:
                intent = new Intent(this.getActivity(),Diary.class);
                redirect(intent);
                break;
        }
    }

    private void redirect(Intent intent) {
        startActivity(intent);
        //getActivity().finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        //latituteField.setText(String.valueOf(lat));
        //longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
