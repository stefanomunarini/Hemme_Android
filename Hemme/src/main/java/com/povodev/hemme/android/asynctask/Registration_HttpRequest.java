package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.management.SessionManagement;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class Registration_HttpRequest extends AsyncTask<Void, Void, User> {

    private final String TAG = "Registration_AsyncTask";

    private Context context;

    private User user;

    private final String message = "Registrazione in corso...";

    private final String title = "Benvenuto in HeMMe";

    private ProgressDialog progressDialog;

    public Registration_HttpRequest(Context context, User user){
        progressDialog = new CustomProgressDialog(context,title,message);

        this.user = user;
        this.context = context;

        Log.d(TAG, "Registering user... username: " + user.getEmail() + " / name: " + user.getName() + " / surname: " + user.getSurname());
    }

    @Override
    protected User doInBackground(Void... params) {

        try {

            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/registration";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            return restTemplate.postForObject(url, user, User.class);
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
            Log.d(TAG,"User has been registered and logged in succesfully");
            SessionManagement.createLoginSession(context, user);
            Log.d(TAG, "User username registered: " + user.getEmail());
        }
        else Log.d(TAG,"Failed to register/login");
    }
}