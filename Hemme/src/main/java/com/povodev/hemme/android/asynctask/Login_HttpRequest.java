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
import com.povodev.hemme.android.activity.Registration_Activity;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.SessionManagement;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Login_HttpRequest extends AsyncTask<Void, Void, User> {

    private final String TAG = "Login_AsyncTask";
    private final String mDialogLoadingMessage = "Login in corso...";

    private Context context;
    private String username;
    private String password;
    private ProgressDialog progressDialog;
    private String imei;
    private User tutor_tmp;

    public Login_HttpRequest(Context context, String username, String password, String imei){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.imei = imei;
        this.context = context;
        this.username = username;
        this.password = password;
    }


    @Override
    protected User doInBackground(Void... params) {
        Log.d(TAG, "Login di " + username + " / passw: " + password);
        try {
            final String url =
            "http://"+ Configurator.ip+"/"+Configurator.project_name+"/login?email="+username+"&password="+password+"&imei="+imei;

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<User> utenteRequest = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    com.povodev.hemme.android.bean.User.class);

            User user = utenteRequest.getBody();
            tutor_tmp = user;
            return user;

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
    protected void onPostExecute(final User user) {

        //elimino il dialog
        if (progressDialog.isShowing()) progressDialog.dismiss();

        if (user!=null){
            /*
             * Se il login lo efettua un utente registrato
             */
            if(user.getImei().equals("tmp")){

                final User usr = user;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Come vuoi registrare questo telefono?")
                        .setMessage(mDialogErrorMessage);

                builder.setPositiveButton("Nuovo dispositivo personale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usr.setImei(Login_Activity.IMEI);
                        new Registration_HttpRequest(context,usr).execute();
                        new AddPatientNewTutor_HttpRequest(usr.getId(),usr.getImei()).execute();
                        new Login_HttpRequest(context,usr.getEmail(),usr.getPassword(),Login_Activity.IMEI);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Nuovo Paziente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityReg(context,user);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
            else {
                SessionManagement.createLoginSession(context, user);
                startActivity(context);
            }
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

    private void startActivityReg(Context context,User userTmp) {
        Intent intent = new Intent(context, Registration_Activity.class);
        intent.putExtra("nome",userTmp.getName());
        intent.putExtra("tutor_id",userTmp.getId());
        context.startActivity(intent);
        ((Login_Activity)context).finish();
    }



}