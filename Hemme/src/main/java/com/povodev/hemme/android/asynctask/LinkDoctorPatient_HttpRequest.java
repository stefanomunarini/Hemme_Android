package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.Header_Creator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class LinkDoctorPatient_HttpRequest extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "New_link_doctroPatient";
    private String url;
    private final String mDialogLoadingMessage = "Associazione in corso...";
    private Context context;
    private ProgressDialog progressDialog;

    public LinkDoctorPatient_HttpRequest(Context context,String url){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);
        this.url = url;
        this.context = context;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return restTemplate.postForObject(url, requestEntity, Boolean.class);

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
    }

}
