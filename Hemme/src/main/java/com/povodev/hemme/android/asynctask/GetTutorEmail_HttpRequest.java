package com.povodev.hemme.android.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.utils.EmailUtils;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 20/05/14.
 */
public class GetTutorEmail_HttpRequest extends AsyncTask<Void, Void, String> {

    public static final String TAG = "GetTutorEmail_HttpRequest";

    private Context context;
    private int patient_id;

    public GetTutorEmail_HttpRequest(Context context, int patient_id){

        this.context = context;
        this.patient_id = patient_id;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/getTutorEmail?patient_id="+patient_id;
            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            ResponseEntity<String> stringRequest = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
            String tutor_email = stringRequest.getBody();

            return tutor_email;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String tutor_email) {

        if (!tutor_email.equals(null)){

            Log.d(TAG,tutor_email);

            EmailUtils.notifyThatPatientHasExitedTheArea(context,tutor_email);

        }
    }
}