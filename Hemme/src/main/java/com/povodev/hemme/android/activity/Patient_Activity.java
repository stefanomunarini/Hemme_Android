package com.povodev.hemme.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.management.SessionManagement;

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

    @InjectResource(R.string.nome_preference)           private String mNomeValuePreference;
    @InjectResource(R.string.indirizzo_preference)      private String mIndirizzoValuePreference;
    @InjectResource(R.string.numerotelefono_preference) private String mNumeroTelefonoValuePreference;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        preferences = SessionManagement.getPreferences(this);

        setComponentsText();
        setComponentsListener();
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
