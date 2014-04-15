package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.Result;
import com.povodev.hemme.android.dialog.CustomProgressDialog;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class NewResult_HttpRequest extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "NewResult_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Caricamento file in corso...";

    private Result result;
    private int user_id;
    private ProgressDialog progressDialog;

    public NewResult_HttpRequest(Context context, Result result, int user_id){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.result = result;
        this.user_id = user_id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/insertResult?user_id=" + user_id;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return restTemplate.postForObject(url, result, Boolean.class);
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
    }
}