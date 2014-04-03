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
    private int user_id = 1;

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
        clinicalEvent.setAuthor(1);
        return clinicalEvent;
    }

    /*private class NewClinicalEvent_HttpRequest extends AsyncTask<Void, Void, Boolean> {

        private ClinicalEvent clinicalEvent;

        public NewClinicalEvent_HttpRequest(Context context, ClinicalEvent clinicalEvent){
            progressDialog = new ProgressDialog(context);
            //progressDialog.setTitle("Benvenuto in HeMMe");
            progressDialog.setMessage("Inserimento evento clinico in corso...");
            this.clinicalEvent = clinicalEvent;
        }

        ProgressDialog progressDialog;

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/newClinicalEvent?user_id=1";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                return restTemplate.postForObject(url, clinicalEvent, Boolean.class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean created) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (created) Log.d(TAG, "Evento clinico inserito.");
            else Log.d(TAG,"Failed to insert new clinical event");
        }
    }*/
}
