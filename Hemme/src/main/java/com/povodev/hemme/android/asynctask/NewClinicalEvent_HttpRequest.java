package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.ClinicalEvent;
import com.povodev.hemme.android.dialog.CustomProgressDialog;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class NewClinicalEvent_HttpRequest extends AsyncTask<Void, Void, Boolean> {

    private final String TAG = "NewClinicalEvent_AsyncTask";

    private ClinicalEvent clinicalEvent;

    private int user_id;

    private final String message = "Inserimento evento clinico in corso...";

    private ProgressDialog progressDialog;

    public NewClinicalEvent_HttpRequest(Context context, ClinicalEvent clinicalEvent, int user_id){
        progressDialog = new CustomProgressDialog(context,message);

        this.clinicalEvent = clinicalEvent;
        this.user_id = user_id;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/newClinicalEvent?user_id=" + user_id;
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
}