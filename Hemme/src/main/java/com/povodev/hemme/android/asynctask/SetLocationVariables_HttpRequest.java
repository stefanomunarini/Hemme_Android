package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.LocationCoordinates;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.Encoding_MD5;
import com.povodev.hemme.android.utils.SessionManagement;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 19/05/14.
 */
public class SetLocationVariables_HttpRequest extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "SetCoordinates_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Attendere prego...";

    private LocationCoordinates locationCoordinates;
    private int user_id;
    private ProgressDialog progressDialog;
    private Context context;

    public SetLocationVariables_HttpRequest(Context context, LocationCoordinates locationCoordinates){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.context = context;
        this.locationCoordinates = locationCoordinates;

        Log.d(TAG, locationCoordinates.getLatitude() + "  " + locationCoordinates.getLongitude());

        this.user_id = SessionManagement.getPatientIdInSharedPreferences(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        Log.d(TAG, "Inserting result into database");

        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/setCoordinates?user_id=" + user_id;

            HttpHeaders headers = new HttpHeaders();
            String salt = Encoding_MD5.getMD5EncryptedString("povodevforhemmeABC");
            headers.set("salt", salt);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            //usato per risolvere bug http://sapandiwakar.in/eofexception-with-spring-rest-template-android/
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            MultiValueMap<String,Object> para = new LinkedMultiValueMap<String, Object>();
            para.add("coordinates",locationCoordinates);
            HttpEntity entity = new HttpEntity(locationCoordinates, headers);
            return restTemplate.postForObject(url, entity, Boolean.class);

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

        if (created) Log.d(TAG, "Result inserted correctly");
        else Log.d(TAG,"Failed to insert new result");

        //TODO notify patient device to set proximity alert
    }
}