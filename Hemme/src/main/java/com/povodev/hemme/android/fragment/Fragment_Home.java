package com.povodev.hemme.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Diary;
import com.povodev.hemme.android.activity.Game_Activity;
import com.povodev.hemme.android.activity.Login_Activity;
import com.povodev.hemme.android.activity.New_ClinicaEvent;
import com.povodev.hemme.android.activity.New_Document;
import com.povodev.hemme.android.activity.New_Result;
import com.povodev.hemme.android.activity.Registration_Activity;
import com.povodev.hemme.android.activity.clinicalFolder.ClinicalFolderListActivity;
import com.povodev.hemme.android.activity.gameTest.TestListActivity;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 27/03/14.
 */
public class Fragment_Home extends RoboFragment implements View.OnClickListener {

    @InjectView(R.id.login_button)                  private Button mLoginButton;
    @InjectView(R.id.registration_button)           private Button mRegistrationButton;
    @InjectView(R.id.newclinicalevent_button)       private Button mNewClinicalEventButton;
    @InjectView(R.id.clinicalfolder_button)         private Button mClinicalFolderButton;
    @InjectView(R.id.document_button)               private Button mDocumentButton;
    @InjectView(R.id.test_button)                   private Button mTestButton;
    @InjectView(R.id.insert_new_result_button)      private Button mNewResultButton;
    @InjectView(R.id.new_game_button)               private Button mNewGameButton;
    @InjectView(R.id.visaulizza_diario)             private Button mDiaryButton;

    @InjectView(R.id.password_forget_textview)      private TextView mPasswordForgetTextView;


    @InjectView(R.id.user_detail_home)              private TextView mUserDetailTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initComponents() {
        mLoginButton.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);
        mPasswordForgetTextView.setOnClickListener(this);
        mNewClinicalEventButton.setOnClickListener(this);
        mClinicalFolderButton.setOnClickListener(this);
        mDocumentButton.setOnClickListener(this);
        mTestButton.setOnClickListener(this);
        mNewResultButton.setOnClickListener(this);
        mNewGameButton.setOnClickListener(this);
        mDiaryButton.setOnClickListener(this);
        User user = SessionManagement.getUserInSession(getActivity());
        mUserDetailTextView.setText("Benvenuto " + user.getName() + " " + user.getSurname());
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
            case R.id.newclinicalevent_button:
                intent = new Intent(this.getActivity(),New_ClinicaEvent.class);
                redirect(intent);
                break;
            case R.id.clinicalfolder_button:
                intent = new Intent(this.getActivity(),ClinicalFolderListActivity.class);
                redirect(intent);
                break;
            case R.id.password_forget_textview:
                intent = new Intent(this.getActivity(),Login_Activity.class);
                redirect(intent);
                break;
            case R.id.document_button:
                intent = new Intent(this.getActivity(),New_Document.class);
                redirect(intent);
                break;
            case R.id.test_button:
                intent = new Intent(this.getActivity(),TestListActivity.class);
                redirect(intent);
                break;
            case R.id.insert_new_result_button:
                intent = new Intent(this.getActivity(),New_Result.class);
                redirect(intent);
                break;
            case R.id.new_game_button:
                intent = new Intent(this.getActivity(),Game_Activity.class);
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
}
