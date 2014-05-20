package com.povodev.hemme.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.TimerTask.ScheduleClient;
import com.povodev.hemme.android.asynctask.GetLocationVariables_HttpRequest;
import com.povodev.hemme.android.utils.SessionManagement;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 06/05/14.
 */
public class Patient_Activity extends RoboActivity {

    @InjectView(R.id.nome_value)                        private EditText mNomeValueEditText;
    @InjectView(R.id.indirizzo_value)                   private EditText mIndirizzoValueEditText;
    @InjectView(R.id.numerotelefono_value)              private EditText mNumeroTelefonoValueEditText;
    @InjectView(R.id.confirm_button)                    private Button mConfirmButton;

    @InjectResource(R.string.nome_preference)           private String mNomeValuePreference;
    @InjectResource(R.string.indirizzo_preference)      private String mIndirizzoValuePreference;
    @InjectResource(R.string.numerotelefono_preference) private String mNumeroTelefonoValuePreference;

    private SharedPreferences preferences;

    private ScheduleClient scheduleClient;

    private int button_press_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        preferences = SessionManagement.getPreferences(this);

        /*scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();*/

        setComponentsText();
        setComponentsListener();

        new GetLocationVariables_HttpRequest(this).execute();

        //setAlarm();
    }

    private void setAlarm() {

        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification();
        scheduleClient.doUnbindService();
    }

    @Override
    public void onStop(){
        super.onStop();
        scheduleClient.doUnbindService();
    }

    private void setComponentsText() {
        String nome = preferences.getString(mNomeValuePreference,null);
        String indirizzo = preferences.getString(mIndirizzoValuePreference,null);
        String numero = preferences.getString(mNumeroTelefonoValuePreference,null);

        if (nome!=null) mNomeValueEditText.setText(nome);
        if (indirizzo!=null) mIndirizzoValueEditText.setText(indirizzo);
        if (numero!=null) mNumeroTelefonoValueEditText.setText(numero);
    }

    private void setComponentsListener() {
        mNomeValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                preferences.edit().putString(mNomeValuePreference,editable.toString()).commit();
            }
        });

        mIndirizzoValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                preferences.edit().putString(mIndirizzoValuePreference,editable.toString()).commit();
            }
        });

        mNumeroTelefonoValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                preferences.edit().putString(mNumeroTelefonoValuePreference,editable.toString()).commit();
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button_press_counter%2==0){
                    mConfirmButton.setText("Modifica Dati");
                    mNumeroTelefonoValueEditText.setEnabled(false);
                    mNumeroTelefonoValueEditText.setTextColor(getResources().getColor(android.R.color.black));
                    mIndirizzoValueEditText.setEnabled(false);
                    mIndirizzoValueEditText.setTextColor(getResources().getColor(android.R.color.black));
                    mNomeValueEditText.setEnabled(false);
                    mNomeValueEditText.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    mConfirmButton.setText("Conferma");
                    mNumeroTelefonoValueEditText.setEnabled(true);
                    mIndirizzoValueEditText.setEnabled(true);
                    mNomeValueEditText.setEnabled(true);
                    mNomeValueEditText.requestFocus();
                }
                button_press_counter++;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this, Login_Activity.class);
                SessionManagement.closeSession(this);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
