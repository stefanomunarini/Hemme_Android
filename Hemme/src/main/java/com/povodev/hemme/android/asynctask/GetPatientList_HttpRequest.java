package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.adapter.SpinnerAdapter;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.bean.UserList;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.fragment.Fragment_Home;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by Stefano on 08/05/14.
 */
public class GetPatientList_HttpRequest extends AsyncTask<Void, Void, ArrayList<User>> {

    private final String TAG = "GetPatientList_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Attendere prego...";

    private Context context;
    private ProgressDialog progressDialog;
    private int user_id;

    public GetPatientList_HttpRequest(Context context, int user_id, String url){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.user_id = user_id;
        this.context = context;
        this.url = url;
    }

    private String url;

    @Override
    protected ArrayList<User> doInBackground(Void... params) {

        try {
            final String url =
                    "http://"+ Configurator.ip+"/"+Configurator.project_name+"/patientList?tutor_id="+user_id;

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<UserList> utenteRequest = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    UserList.class);

            UserList userList = utenteRequest.getBody();
            return userList;

        }catch (Exception e) {Log.e(TAG, e.getMessage(), e);}

        return null;
    }

    @Override
    protected void onPreExecute(){
        progressDialog.show();
    }

    private final String mDialogErrorTitle = "Errore";
    private final String mDialogErrorMessage = "Rieffettuare il login o procedere con una nuova registrazione.";

    @Override
    protected void onPostExecute(ArrayList<User> userList) {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        if (userList==null){
            //context.getActivity().findViewById(R.id.patient_spinner).setVisibility(View.GONE);
        }
        else {
            //TODO set the spinner adapter
            ArrayAdapter<User> adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, userList);
            //Fragment_Home.spinnerAdapter = adapter;
            Fragment_Home.setAdapter(adapter);
        }
    }
}
