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
import android.widget.Spinner;
import android.widget.Toast;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.GetUserList_HttpRequest;
import com.povodev.hemme.android.asynctask.LinkDoctorPatient_HttpRequest;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class Associa_Dispositivi extends FragmentActivity implements View.OnClickListener{

    public static Spinner spinner_dottori;
    public static Spinner spinner_pazienti;

    private static Context context;
    private String url;
    private User user;

    private static User selected_doctor;
    private static User selected_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associa__dispositivi);

        user = SessionManagement.getUserInSession(this);
        this.context = this;

        spinner_dottori= (Spinner)findViewById(R.id.spinner_medici);
        spinner_pazienti = (Spinner) findViewById(R.id.spinner_pazienti);

        Button mConfirmButton = (Button) findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);

        url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/getListDoctor";
        new GetUserList_HttpRequest(this, "dottori", url).execute();

        url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/patientList?tutor_id="+user.getId();
        new GetUserList_HttpRequest(this, "pazienti", url).execute();

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
        Toast.makeText(context," set adapoter",Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.associa__dispositivi, menu);
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
                Toast.makeText(context," set adapoter" + selected_patient.getId()  + " - " + selected_doctor.getId() ,Toast.LENGTH_SHORT).show();
                url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/newHasDp?patient_id="+selected_patient.getId()+"&doctor_id="+selected_doctor.getId();
                new LinkDoctorPatient_HttpRequest(context,url).execute();
                break;
        }
    }
}
