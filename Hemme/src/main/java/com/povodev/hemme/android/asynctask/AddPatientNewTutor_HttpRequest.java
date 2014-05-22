package com.povodev.hemme.android.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class AddPatientNewTutor_HttpRequest extends AsyncTask<Void, Void, Boolean> {


    String TAG = "AddPatientNewTutor_HttpRequest";
    int old_tutor_id;
    String IMEI;

    public AddPatientNewTutor_HttpRequest(int old_tutor_id, String IMEI){
        this.old_tutor_id = old_tutor_id;
        this.IMEI = IMEI;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            final String url =
                    "http://"+ Configurator.ip+"/"+Configurator.project_name+"/addNewLinkTutorPatient?old_tutor_id="+old_tutor_id+"&IMEI="+IMEI;

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.postForObject(url, requestEntity, Boolean.class);

        } catch (Exception e) {Log.e(TAG, "aggiornamento pazienti nuovo disp tutore FAIL" + e.getMessage(), e);}

        return null;
    }
}
