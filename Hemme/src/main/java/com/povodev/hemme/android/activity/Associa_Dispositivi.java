package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.adapter.PatientDoctorAdapter;
import com.povodev.hemme.android.asynctask.GetPatientDoctorList_HttpRequest;
import com.povodev.hemme.android.asynctask.GetUserList_HttpRequest;
import com.povodev.hemme.android.asynctask.LinkDoctorPatient_HttpRequest;
import com.povodev.hemme.android.bean.PatientDoctorItem;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.utils.SessionManagement;

import java.util.ArrayList;

public class Associa_Dispositivi extends FragmentActivity implements View.OnClickListener{

    public static Spinner spinner_dottori;
    public static Spinner spinner_pazienti;

    public static ListView patient_doctor_ListView;

    private static Context context;
    private String url;
    private User user;

    private static User selected_doctor;
    private static User selected_patient;

    static RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associa_dispositivi);

        user = SessionManagement.getUserInSession(this);
        this.context = this;

        spinner_dottori= (Spinner)findViewById(R.id.spinner_medici);
        spinner_pazienti = (Spinner) findViewById(R.id.spinner_pazienti);

        patient_doctor_ListView = (ListView) findViewById(R.id.patient_doctor_list);

        Button mConfirmButton = (Button) findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);

        url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/getListDoctor";
        new GetUserList_HttpRequest(this, "dottori", url).execute();

        url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/patientList?tutor_id="+user.getId();
        new GetUserList_HttpRequest(this, "pazienti", url).execute();

        new GetPatientDoctorList_HttpRequest(context).execute();
    }

    public static void setAdapterMedici(ArrayAdapter adapter){
        spinner_dottori.setAdapter(adapter);
        spinner_dottori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_doctor = (User) spinner_dottori.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
    public static void setAdapterPazienti(ArrayAdapter adapter){
        spinner_pazienti.setAdapter(adapter);
        spinner_pazienti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_patient = (User) spinner_pazienti.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.associa_dispositivi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.confirm_button:
                url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/newHasDp?patient_id="+selected_patient.getId()+"&doctor_id="+selected_doctor.getId();
                new LinkDoctorPatient_HttpRequest(context,url).execute();
                break;
        }
    }

    public static void populateListView(ArrayList<PatientDoctorItem> data){

        PatientDoctorAdapter patientDoctorAdapter = new PatientDoctorAdapter(context,R.layout.patient_doctor_row,data);
        patient_doctor_ListView.setAdapter(patientDoctorAdapter);
    }
}
