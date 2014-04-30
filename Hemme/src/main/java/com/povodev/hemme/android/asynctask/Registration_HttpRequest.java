package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.activity.Home_Activity;
import com.povodev.hemme.android.activity.Registration_Activity;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.management.SessionManagement;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class Registration_HttpRequest extends AsyncTask<Void, Void, User> {

    private final String TAG = "Registration_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Registrazione in corso...";
    /*
     * Loading dialog title
     */
    private final String mDialogTitle = "Benvenuto in HeMMe";

    private Context context;
    private User user;
    private ProgressDialog progressDialog;

    public Registration_HttpRequest(Context context, User user){
        progressDialog = new CustomProgressDialog(context,mDialogTitle,mDialogLoadingMessage);

        this.user = user;
        this.context = context;

        Log.d(TAG, "Registering user...  username: " + user.getEmail() + " / name: " + user.getName() + " / surname: " + user.getSurname());
    }

    @Override
    protected User doInBackground(Void... params) {

        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/registration";
            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


            ResponseEntity<User> utenteRequest= restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    com.povodev.hemme.android.bean.User.class);
            User user = utenteRequest.getBody();

            return user;

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
    protected void onPostExecute(User user) {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        if (user!=null){
            Log.d(TAG,"User has been registered and logged succesfully");
            SessionManagement.createLoginSession(context, user);
            Log.d(TAG, "Username registered: " + user.getEmail());
            jumpIntoApp();
        }
        else Log.d(TAG,"Failed to register/login");
    }

    private void jumpIntoApp() {
        Intent intent = new Intent(context, Home_Activity.class);
        context.startActivity(intent);
        ((Registration_Activity)context).finish();
    }
}