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
import com.povodev.hemme.android.activity.Diary;
import com.povodev.hemme.android.activity.NewClinicaEvent_Activity;
import com.povodev.hemme.android.activity.Patient_Activity;
import com.povodev.hemme.android.activity.clinicalFolder.ClinicalFolderListActivity;
import com.povodev.hemme.android.activity.memory_results.MemoryResultsListActivity;
import com.povodev.hemme.android.asynctask.GetUserList_HttpRequest;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.utils.SessionManagement;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Fragment_Home extends RoboFragment implements View.OnClickListener {

    private final static String TAG = "Fragment_Home";

    @InjectView(R.id.newclinicalevent_button)       private Button mNewClinicalEventButton;
    @InjectView(R.id.clinicalfolder_button)         private Button mClinicalFolderButton;
    @InjectView(R.id.test_button)                   private Button mTestButton;
    @InjectView(R.id.visaulizza_diario)             private Button mDiaryButton;
    @InjectView(R.id.user_detail_home)              private TextView mUserDetailTextView;

    public static Spinner mPatientSpinner;

    private User user;
    private String url;

    private static Context context;

    private static ArrayList<User> patient_list_spinner;

    /*
     * true if user is logged in, false otherwise
     */
    private boolean isUserLoggedIn = false;

    public static void setPatient_list_spinner_size(ArrayList<User> patient_list_spinner) {
        Fragment_Home.patient_list_spinner = patient_list_spinner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        context = getActivity();
        user = SessionManagement.getUserInSession(getActivity());
        isUserLoggedIn = SessionManagement.isUserLoggedIn(getActivity());

        int user_role = checkUserType(user);
        if (user_role==0){
            //TUTOR
            url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/patientList?tutor_id="+user.getId();;
            new GetUserList_HttpRequest(getActivity(),user.getId(),url).execute();
        } else if (user_role==1){
            //DOCTOR
            url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/patientListDoctor?doctor_id="+user.getId();;
            new GetUserList_HttpRequest(getActivity(),user.getId(),url).execute();
        } else if (user_role==2){
            //PATIENT
            Intent intent = new Intent(this.getActivity(),Patient_Activity.class);
            redirect(intent);
            getActivity().finish();
        }
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

        mPatientSpinner = (Spinner)getActivity().findViewById(R.id.patient_spinner);
        initComponents();
    }

    public static void setAdapter(ArrayAdapter adapter){
        mPatientSpinner.setAdapter(adapter);

        if (patient_list_spinner.size()==0){
            mPatientSpinner.setVisibility(View.GONE);
        } else {

            int current_patient_id_selected =  SessionManagement.getPatientIdInSharedPreferences(context);
            int spinner_position;

            if (current_patient_id_selected==-1){
                current_patient_id_selected = ((User)mPatientSpinner.getAdapter().getItem(0)).getId();
                SessionManagement.editPatientIdInSharedPreferences(context,current_patient_id_selected);
                spinner_position = 0;
            } else {
                spinner_position = getCurrentSelectedPatientPosition(current_patient_id_selected);
            }
            mPatientSpinner.setSelection(spinner_position);
            //SessionManagement.editPatientIdInSharedPreferences(context, ((User)mPatientSpinner.getAdapter().getItem(spinner_position)).getId());

            mPatientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    User selectedItem = (User) mPatientSpinner.getSelectedItem();
                    SessionManagement.editPatientIdInSharedPreferences(context, selectedItem.getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private static int getCurrentSelectedPatientPosition(int user_id){
        int position = 0;
        for (int i=0; i<patient_list_spinner.size(); i++){
            Log.d(TAG,position+" ] user_id: " + user_id + "   " + patient_list_spinner.get(i).getId());
            if (user_id==patient_list_spinner.get(i).getId()){
                return position;
            }
            position++;
        }
        return position;
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

        /*
         * Tutor
         */
        if(userType==0){
            mTestButton.setOnClickListener(this);
            mDiaryButton.setOnClickListener(this);
        }
        /*
         * Doctor
         */
        else if (userType==1){
            mTestButton.setVisibility(View.GONE);
            mDiaryButton.setVisibility(View.GONE);
        }

        mNewClinicalEventButton.setOnClickListener(this);
        mClinicalFolderButton.setOnClickListener(this);
        mUserDetailTextView.setText("Benvenuto " + user.getName() + " " + user.getSurname());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id){
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
        }
    }

    private void redirect(Intent intent) {
        startActivity(intent);
    }
}
