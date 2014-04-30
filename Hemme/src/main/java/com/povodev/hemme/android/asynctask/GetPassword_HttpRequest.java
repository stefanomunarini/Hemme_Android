package com.povodev.hemme.android.asynctask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.Diary;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 29/04/14.
 */
public class GetPassword_HttpRequest extends AsyncTask<Void, Void, String> {


    public static final String TAG = "PasswordRecovery_AsyncTask";
    /*
     * Loading dialog message
     */
    private final String mDialogLoadingMessage = "Attendere prego...";

    private Context context;
    private String username;
    private ProgressDialog progressDialog;

    public GetPassword_HttpRequest(Context context, String username){
        progressDialog = new CustomProgressDialog(context,mDialogLoadingMessage);

        this.context = context;
        this.username = username;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d(TAG, "Recupero password di " + username );
        try {
            final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/passwordRecovery?email="+username;
            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            ResponseEntity<String> stringRequest = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
            //String password = restTemplate.getForObject(url, String.class);
            String password = stringRequest.getBody();

            return password;
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
    private final String mDialogErrorMessage = "L'indirizzo email inserito e' errato o non e' ancora registrato.";

    @Override
    protected void onPostExecute(String password) {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        if (password!=null){

            com.povodev.hemme.android.utils.PasswordRecovery.passwordRecovery(context,username,password);

            Log.d(TAG, "Password recovered successfully!");

        } else {

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

            Log.d(TAG, "Failed to recover password");
        }
    }
}
