package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.NewClinicalEvent_HttpRequest;
import com.povodev.hemme.android.bean.ClinicalEvent;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 01/04/14.
 */
public class New_ClinicaEvent extends RoboActivity implements View.OnClickListener {

    private final String TAG = "NewClinicalEvent_Activity";


    //TODO set the right user_id
    private int user_id = 2;

    @InjectView(R.id.therapy_edittext)                      private EditText mTherapyEditText;
    @InjectView(R.id.note_edittext)                         private EditText mNoteEditText;
    @InjectView(R.id.insert_new_clinicalevent_button)       private Button mInserNewClinicalEventButton;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_clinicalevent);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        //TODO cancellare queste due righe (usate solo per test)
        mNoteEditText.setText("noteeee");
        mTherapyEditText.setText("terapiaaaa");

        setComponentsListener();
    }

    private void setComponentsListener() {
        mInserNewClinicalEventButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.insert_new_clinicalevent_button:
                new NewClinicalEvent_HttpRequest(context,getClinicalEvent(),user_id).execute();
                break;
        }
    }

    private ClinicalEvent getClinicalEvent() {
        return getClinicalEventValues();
    }

    private String therapy;
    private String note;

    private ClinicalEvent getClinicalEventValues() {
        therapy = mTherapyEditText.getText().toString();
        note = mNoteEditText.getText().toString();

        return setUserValues(therapy, note);
    }

    private ClinicalEvent setUserValues(String therapy, String note) {
        ClinicalEvent clinicalEvent = new ClinicalEvent();
        clinicalEvent.setTherapy(therapy);
        clinicalEvent.setNote(note);
        clinicalEvent.setAuthor(user_id);
        return clinicalEvent;
    }
}
