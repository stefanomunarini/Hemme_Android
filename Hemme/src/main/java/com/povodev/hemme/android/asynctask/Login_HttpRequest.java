package com.povodev.hemme.android.asynctask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.management.SessionManagement;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class Login_HttpRequest extends AsyncTask<Void, Void, User> {

    private final String TAG = "Login_AsyncTask";

    private Context context;

    private String username;
    private String password;

    public Login_HttpRequest(Context context, String username, String password){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Login in corso...");

        this.context = context;
        this.username = username;
        this.password = password;
    }

    ProgressDialog progressDialog;

    @Override
    protected User doInBackground(Void... params) {
        Log.d(TAG, "Login di " + username + " / passw: " + password);
        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/login?email="+username+"&password="+password;
            Log.d(TAG,"Request URL4Login: " + url);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            User user = restTemplate.getForObject(url, User.class);
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
            SessionManagement.createLoginSession(context, user);
            Log.d(TAG, "User has been logged in succesfully");
            Log.d(TAG,"Username: " + user.getEmail());
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Errore!")
                    .setMessage("Rieffettuare il login o procedere con una nuova registrazione.");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            Log.d(TAG, "Failed to Login");
        }
    }
}