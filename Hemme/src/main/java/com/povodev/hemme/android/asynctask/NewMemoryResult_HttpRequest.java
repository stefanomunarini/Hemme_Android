package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.activity.NewGame_Activity;
import com.povodev.hemme.android.activity.memory_results.MemoryResultsListActivity;
import com.povodev.hemme.android.bean.Result;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.SessionManagement;
import com.povodev.hemme.android.utils.Encoding_MD5;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class NewMemoryResult_HttpRequest extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "NewResult_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Salvataggio risultato in corso...";

    private Result result;
    private int user_id;
    private ProgressDialog progressDialog;
    private Context context;

    public NewMemoryResult_HttpRequest(Context context, Result result, int user_id){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.context = context;
        this.result = result;
        this.user_id = SessionManagement.getPatientIdInSharedPreferences(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        Log.d(TAG,"Inserting result into database");

        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/insertResult?user_id=" + user_id;

            HttpHeaders headers = new HttpHeaders();
            String salt = Encoding_MD5.getMD5EncryptedString("povodevforhemmeABC");
            headers.set("salt", salt);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // /usato per risolvere bug http://sapandiwakar.in/eofexception-with-spring-rest-template-android/
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            MultiValueMap<String,Object> para = new LinkedMultiValueMap<String, Object>();
            para.add("result",result);
            HttpEntity entity = new HttpEntity(result, headers);
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

        Intent intent = new Intent(context, MemoryResultsListActivity.class);
        context.startActivity(intent);
        ((NewGame_Activity)context).finish();
    }
}