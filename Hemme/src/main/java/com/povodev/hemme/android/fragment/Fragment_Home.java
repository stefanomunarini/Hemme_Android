package com.povodev.hemme.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Associa_Dispositivi;
import com.povodev.hemme.android.activity.Diary;
import com.povodev.hemme.android.activity.Login_Activity;
import com.povodev.hemme.android.activity.NewClinicaEvent_Activity;
import com.povodev.hemme.android.activity.Patient_Activity;
import com.povodev.hemme.android.activity.clinicalFolder.ClinicalFolderListActivity;
import com.povodev.hemme.android.activity.memory_results.MemoryResultsListActivity;
import com.povodev.hemme.android.asynctask.GetUserList_HttpRequest;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Fragment_Home extends RoboFragment implements View.OnClickListener {

    private final static String TAG = "Fragment_Home";

    @InjectView(R.id.login_button)                  private Button mLoginButton;
    @InjectView(R.id.newclinicalevent_button)       private Button mNewClinicalEventButton;
    @InjectView(R.id.clinicalfolder_button)         private Button mClinicalFolderButton;
    @InjectView(R.id.test_button)                   private Button mTestButton;
    @InjectView(R.id.visaulizza_diario)             private Button mDiaryButton;
    @InjectView(R.id.manage_devices)                private Button mManageDeviceButton;
    @InjectView(R.id.user_detail_home)              private TextView mUserDetailTextView;

    public static Spinner mPatientSpinner;

    private User user;
    private String url;

    private static Context context;

    /*
     * true if user is logged in, false otherwise
     */
    private boolean isUserLoggedIn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        context = getActivity();
        user = SessionManagement.getUserInSession(getActivity());
        isUserLoggedIn = SessionManagement.isUserLoggedIn(getActivity());

        Log.d(TAG,"User_id: " + user.getId());

        int user_role = checkUserType(user);
        //Toast.makeText(context,"User role: "+ user_role, Toast.LENGTH_SHORT).show();
        if (user_role==0){
            //TUTOR
            url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/patientList?tutor_id="+user.getId();;
            new GetUserList_HttpRequest(getActivity(),user.getId(),url).execute();
        } else if (user_role==1){
            //DOCTOR
            //TODO
            url = null;
            new GetUserList_HttpRequest(getActivity(),user.getId(),url).execute();
        } else if (user_role==2){
            //PATIENT
            Intent intent = new Intent(this.getActivity(),Patient_Activity.class);
            redirect(intent);
        }


        /*
         * SENSORE DI PROSSIMITA'
         */
        //proximityFunctions();

        /*
         * PAZIENTE
         */
        /*if (user.getRole()==2){
            Intent intent = new Intent(this.getActivity(),Patient_Activity.class);
            redirect(intent);
        }*/
    }


/*


    private LocationManager lm;
    private void proximityFunctions() {

        HashMap<String,Double> latLong = Localization.getCoordinates(getActivity());
        double myLatitude = latLong.get(Localization.LATITUDE);
        double myLongitude = latLong.get(Localization.LONGITUDE);
        Toast.makeText(getActivity(),Localization.LATITUDE +": "+myLatitude +"  "+Localization.LONGITUDE+": "+myLongitude, Toast.LENGTH_SHORT).show();


        //Intent i = new Intent("com.povodev.hemme.proximity_alert");
        //PendingIntent pi = PendingIntent.getBroadcast(getActivity(), -1, i, 0);

        //lm.addProximityAlert(latitude, longitude, radius, -1, pi);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPatientSpinner = (Spinner)getActivity().findViewById(R.id.patient_spinner);
        initComponents();
    }

    public static void setAdapter(ArrayAdapter adapter){
        mPatientSpinner.setAdapter(adapter);
        mPatientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                User selectedItem = (User) mPatientSpinner.getSelectedItem();
                SessionManagement.editPatientIdInSharedPreferences(context,selectedItem.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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


    /*
     * The adapter used to populate the spinner
     */
    public static ArrayAdapter<User> spinnerAdapter;

    private void initComponents() {
        int userType = checkUserType(user);

        /*
         * Tutor
         */
        if(userType==0){
            //mPatientSpinner.setAdapter(spinnerAdapter);
            mTestButton.setOnClickListener(this);
            mDiaryButton.setOnClickListener(this);
        }
        /*
         * Doctor
         */
        else if (userType==1){
            //mPatientSpinner.setAdapter(spinnerAdapter);
            mTestButton.setVisibility(View.GONE);
            mDiaryButton.setVisibility(View.GONE);
        }

        mLoginButton.setOnClickListener(this);
        mNewClinicalEventButton.setOnClickListener(this);
        mClinicalFolderButton.setOnClickListener(this);
        mManageDeviceButton.setOnClickListener(this);
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
                intent = new Intent(this.getActivity(),NewClinicaEvent_Activity.class);
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
            case R.id.manage_devices:
                intent = new Intent(this.getActivity(),Associa_Dispositivi.class);
                redirect(intent);
                break;
        }
    }

    private void redirect(Intent intent) {
        startActivity(intent);
        //getActivity().finish();
    }
}
