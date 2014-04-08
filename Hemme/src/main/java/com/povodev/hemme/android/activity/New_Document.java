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

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.FileManager;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class New_Document extends RoboActivity implements View.OnClickListener{

    private final String TAG = "NewDocument_Activity";
    final int ACTIVITY_CHOOSE_FILE = 1;

    @InjectView(R.id.note_edittext)                         private EditText mNoteEditText;
    @InjectView(R.id.insert_new_file_button)                private Button mInserNewFileButton;
    @InjectView(R.id.insert_new_document_button)            private Button mInsertNewDocumentButton;

    private Context context;


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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,ACTIVITY_CHOOSE_FILE);
            }
            break;

            case R.id.insert_new_document_button:{
                  new NewDocument_HttpRequest(context,getDocument()).execute();
            }
            break;

        }

    }

    String filePath;
    String file;
    String note;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case ACTIVITY_CHOOSE_FILE:
                if(resultCode==RESULT_OK){
                    String uri = data.getData().getPath();
                    filePath = uri;
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

        public NewDocument_HttpRequest(Context context, Document document){
            progressDialog = new CustomProgressDialog(context,message);
            this.document = document;
            Log.d(TAG,"_______________--nota del documento--__________" + document.getNote());
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String note = document.getNote();
                final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/uploadDocument?nota="+note;

                MultiValueMap<String, Object> para = new LinkedMultiValueMap<String, Object>();
                para.add("file",new FileSystemResource(filePath));

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type","multipart/form-data");
                headers.set("enctype","multipart/form-data");
                headers.set("method","post");

                HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(para,headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                return restTemplate.postForObject(url,requestEntity,boolean.class);

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
        }
    }
}
