package com.povodev.hemme.android.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.management.SessionManagement;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 06/05/14.
 */
public class PatientActivity extends RoboActivity {

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
}
