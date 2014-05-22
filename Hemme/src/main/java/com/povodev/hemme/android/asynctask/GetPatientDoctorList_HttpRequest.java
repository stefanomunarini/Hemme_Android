package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.activity.Associa_Dispositivi;
import com.povodev.hemme.android.bean.PatientDoctorItem;
import com.povodev.hemme.android.bean.PatientDoctorList;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.Header_Creator;
import com.povodev.hemme.android.utils.SessionManagement;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Stefano on 08/05/14.
 */
public class GetPatientDoctorList_HttpRequest extends AsyncTask<Void, Void, ArrayList<PatientDoctorItem>> {

    private final String TAG = "GetPatientDoctorList_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Attendere prego...";

    private Context context;
    private ProgressDialog progressDialog;
    private static int user_id;

    public GetPatientDoctorList_HttpRequest(Context context){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.user_id = SessionManagement.getUserInSession(context).getId();
        this.context = context;
    }

    private static String url;

    @Override
    protected ArrayList<PatientDoctorItem> doInBackground(Void... params) {

        url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/getPatientDoctorList?tutor_id=" + user_id;

        try {

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<PatientDoctorList> utenteRequest = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    PatientDoctorList.class);

            ArrayList<PatientDoctorItem> patientDoctorList = utenteRequest.getBody();
            return patientDoctorList;

        }catch (Exception e) {Log.e(TAG, e.getMessage(), e);}
        return null;
    }

    private class items extends ArrayList<PatientDoctorItem>{
        public ArrayList<PatientDoctorItem> items;

        public items(Collection<? extends PatientDoctorItem> c){
            super(c);
        }
    }

    @Override
    protected void onPreExecute(){
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<PatientDoctorItem> items) {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        if (items!=null){
            Associa_Dispositivi.populateListView(items);
        }
    }
}
