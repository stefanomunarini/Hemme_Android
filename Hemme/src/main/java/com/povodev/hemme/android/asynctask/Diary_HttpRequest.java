package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.activity.Diary;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.management.SessionManagement;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 *    CREAZIONE ARRAY DI DOCUMENTI PRESENTI NEL DATABASE
 */
public class Diary_HttpRequest extends AsyncTask<Void, Void, ArrayList<Document>> {

    private String TAG = "NewDocumentHttpRequest";
    private final String message = "Recupero dati..";
    private ProgressDialog progressDialog;
    public static ArrayList<Document> diario;
    private Context context;
    private int user_id;

    public Diary_HttpRequest(Context context) {
        this.context = context;
        user_id = SessionManagement.getUserInSession(context).getId();
        progressDialog = new CustomProgressDialog(context, message);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected ArrayList<Document> doInBackground(Void... params) {
        try {
            Log.d(TAG,"user id = " + user_id);
            final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/getDiary?user_id="+user_id;

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<com.povodev.hemme.android.bean.Diary> diarioRequest = restTemplate.exchange(url,
                                           HttpMethod.GET,
                                           requestEntity,
                                           com.povodev.hemme.android.bean.Diary.class);
            Diary.diario = diarioRequest.getBody();
            return diarioRequest.getBody();
        }

        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Document> result) {
        super.onPostExecute(result);

        new BitmapDownload(result,context).execute();

        //finito di generare il mio array di dcoument setto ad ognuno una immagine BiTmAp
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }
}