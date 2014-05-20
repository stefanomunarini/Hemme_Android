package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.Header_Creator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class AssciazioneTutorPaziente extends AsyncTask<Void, Void, Boolean> {

    private final String TAG = "Association_AsyncTask";
    public int paziente_id;
    public int tutor_id;
    private final String mDialogLoadingMessage = "Associazione tutor-paziente";
    private final String mDialogTitle = "Benvenuto in HeMMe";
    private ProgressDialog progressDialog;
    public String url = "";

    public AssciazioneTutorPaziente(Context context, int paziente_id,int tutor_id){
        progressDialog = new CustomProgressDialog(context,mDialogTitle,mDialogLoadingMessage);
        this.paziente_id = paziente_id;
        this.tutor_id = tutor_id;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/association?paziente_id="+paziente_id+"&tutor_id="+tutor_id;

        HttpHeaders headers = Header_Creator.create();
        HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.postForObject(url, requestEntity, Boolean.class);


    }

    @Override
    protected void onPreExecute(){
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        if (result){
            Log.d(TAG,"associazione tutor paziente non riuscita");
        }
    }

}
