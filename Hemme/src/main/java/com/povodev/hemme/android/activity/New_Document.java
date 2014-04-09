package com.povodev.hemme.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.management.SessionManagement;
import com.povodev.hemme.android.utils.FileManager;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class New_Document extends RoboActivity implements View.OnClickListener{

    private final String TAG = "NewDocument_Activity";
    final int ACTIVITY_CHOOSE_FILE = 1;

    @InjectView(R.id.note_edittext)                         private EditText mNoteEditText;
    @InjectView(R.id.insert_new_file_button)                private Button mInserNewFileButton;
    @InjectView(R.id.insert_new_document_button)            private Button mInsertNewDocumentButton;
    @InjectView(R.id.nomi_file)                             private TextView mNomiFile;

    private Context context;
    private int countFileToUpload = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__document);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        setComponentsListener();
    }

    private void setComponentsListener() {
        mInserNewFileButton.setOnClickListener(this);
        mInsertNewDocumentButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new__document, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.insert_new_file_button:{
                if(countFileToUpload<3) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                }else{
                    Toast toast;
                    toast =  Toast.makeText(context,"PUOI INSERIRE MASSIMO 3 FILE", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            break;

            case R.id.insert_new_document_button:{
                new NewDocument_HttpRequest(context,getDocument()).execute();
            }
            break;

        }

    }

    String filePath;
    String note;
    ArrayList<FileSystemResource> fileToUpload = new ArrayList<FileSystemResource>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case ACTIVITY_CHOOSE_FILE:
                if(resultCode==RESULT_OK){
                    String filePath = data.getData().getPath();
                    FileSystemResource fsr = new FileSystemResource(filePath);
                    fileToUpload.add(fsr);
                    countFileToUpload++;
                    mNomiFile.append("  \n" + fsr.getFilename());
                    break;
                }
        }
    }

    private Document getDocument() {
        return getDocumentValues();
    }
    private Document getDocumentValues() {
        note = mNoteEditText.getText().toString();
        return setUserValues(filePath,note);
    }

    private Document setUserValues(String file,String note) {
        Document document =  new Document();
        document.setFile(file);
        document.setNote(note);
        return document;
    }

    private class NewDocument_HttpRequest extends AsyncTask<Void, Void, Boolean> {

        private Document document;
        private final String message = "Caricamento file in corso...";
        private ProgressDialog progressDialog;
        private User user;

        public NewDocument_HttpRequest(Context context, Document document){
            progressDialog = new CustomProgressDialog(context,message);
            this.document = document;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Iterator it = fileToUpload.iterator();

                while(it.hasNext())
                {
                    FileSystemResource fsr = (FileSystemResource) it.next();

                    String note = document.getNote();
                    User user = SessionManagement.getUserInSession(context);
                    /*--------------------------------------------------------
                        DA ELIMINARE QUANDO SI AVRA' LA GESTIONE DI SESSIONE
                    ----------------------------------------------------------*/
                    user.setId(1);
                    final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/uploadDocument?nota=" + note + "&idu=" + user.getId();
                    MultiValueMap<String, Object> para = new LinkedMultiValueMap<String, Object>();
                    para.add("file", fsr);

                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Content-Type", "multipart/form-data");
                    headers.set("enctype", "multipart/form-data");
                    headers.set("method", "post");
                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(para, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.postForObject(url, requestEntity, boolean.class);
                }
                return true;

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return false;
        }

        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean created) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (created) Log.d(TAG, "Inserito file correttamente");
            else Log.d(TAG,"Failed to insert new document");

            countFileToUpload = 0;
            fileToUpload.clear();
            mNomiFile.setText("FINITO!");
        }
    }
}
