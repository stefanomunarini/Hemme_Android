package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.clinicalFolder.ClinicalFolderDetailFragment;
import com.povodev.hemme.android.asynctask.ClinicalEvent_HttpRequest;
import com.povodev.hemme.android.bean.ClinicalEvent;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 01/04/14.
 */
public class NewClinicaEvent_Activity extends RoboActivity implements View.OnClickListener {

    private final String TAG = "NewClinicalEvent_Activity";

    private int user_id;

    @InjectView(R.id.therapy_edittext)                      private EditText mTherapyEditText;
    @InjectView(R.id.note_edittext)                         private EditText mNoteEditText;
    @InjectView(R.id.insert_new_clinicalevent_button)       private Button mInserNewClinicalEventButton;

    private Context context;
    private boolean newClinicalEvent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_clinicalevent);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        /*
         * If the Activity has been launched to modify a Clinical Event
         * get the bundle. Otherwise, do normal work.
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            therapy = extras.getString(ClinicalFolderDetailFragment.THERAPY_4_BUNDLE);
            note = extras.getString(ClinicalFolderDetailFragment.NOTE_4_BUNDLE);
            clinicalEvent_id =  extras.getInt(ClinicalFolderDetailFragment.ID_4_BUNDLE);

            /*
             * This helps us to know if it's going to be created a new Clinical Event
             * or just modifying an existing one (to send the right request to the server)
             */
            newClinicalEvent = false;

            mTherapyEditText.setText(therapy);
            mNoteEditText.setText(note);
        }

        //TODO prendere l'id del paziente
        user_id = SessionManagement.getUserInSession(this).getId();

        setComponentsListener();
    }

    private void setComponentsListener() {
        mInserNewClinicalEventButton.setOnClickListener(this);
    }

    /*
     * These Strings are used to send the right request to the server.
     * Case 1: New Clinical Event
     * Case 2: Modify an existing Clinical Event
     */
    private String newClinicalEventUrl = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/newClinicalEvent?user_id=";
    private String modifyClinicalEventUrl = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/modifyClinicalEvent";

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.insert_new_clinicalevent_button:
                Toast.makeText(context,"newClinicalEvent: "+newClinicalEvent,Toast.LENGTH_SHORT).show();
                ClinicalEvent clinicalEvent = getClinicalEvent();
                if (newClinicalEvent) {
                    new ClinicalEvent_HttpRequest(context, clinicalEvent, newClinicalEventUrl.concat(clinicalEvent.getAuthor()+"")).execute();
                } else {
                    new ClinicalEvent_HttpRequest(context,clinicalEvent, modifyClinicalEventUrl).execute();
                }
                break;
        }
    }

    private ClinicalEvent getClinicalEvent() {
        return getClinicalEventValues();
    }

    private String therapy;
    private String note;
    private int clinicalEvent_id;

    private ClinicalEvent getClinicalEventValues() {
        therapy = mTherapyEditText.getText().toString();
        note = mNoteEditText.getText().toString();

        return setClinicalEventValues(therapy, note);
    }

    private ClinicalEvent setClinicalEventValues(String therapy, String note) {
        ClinicalEvent clinicalEvent = new ClinicalEvent();
        clinicalEvent.setTherapy(therapy);
        clinicalEvent.setNote(note);
        clinicalEvent.setAuthor(user_id);
        if (!newClinicalEvent) {
            clinicalEvent.setId(clinicalEvent_id);
        }
        return clinicalEvent;
    }
}
