package com.povodev.hemme.android.asynctask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.activity.Home_Activity;
import com.povodev.hemme.android.activity.Login_Activity;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.management.SessionManagement;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 03/04/14.
 */
public class Login_HttpRequest extends AsyncTask<Void, Void, User> {

    private final String TAG = "Login_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Login in corso...";

    private Context context;
    private String username;
    private String password;
    private ProgressDialog progressDialog;

    public Login_HttpRequest(Context context, String username, String password){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.context = context;
        this.username = username;
        this.password = password;
    }

    @Override
    protected User doInBackground(Void... params) {
        Log.d(TAG, "Login di " + username + " / passw: " + password);
        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/login?email="+username+"&password="+password;
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

    private final String mDialogErrorTitle = "Errore";
    private final String mDialogErrorMessage = "Rieffettuare il login o procedere con una nuova registrazione.";

    @Override
    protected void onPostExecute(User user) {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        if (user!=null){
            SessionManagement.createLoginSession(context, user);
            Log.d(TAG, "User has been logged succesfully");
            Log.d(TAG,"Username: " + user.getEmail());
            startActivity(context);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(mDialogErrorTitle)
                    .setMessage(mDialogErrorMessage);
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

    private void startActivity(Context context) {
        Intent intent = new Intent(context, Home_Activity.class);
        context.startActivity(intent);
        ((Login_Activity)context).finish();
    }
}