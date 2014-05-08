package com.povodev.hemme.android.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.GetPassword_HttpRequest;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.bean.User;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.management.SessionManagement;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class NewDocument_Activity extends RoboActivity implements View.OnClickListener{

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
        setContentView(R.layout.activity_new_document);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        setComponentsListener();
    }

    private void setComponentsListener() {
        mInserNewFileButton.setOnClickListener(this);
        mInsertNewDocumentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.insert_new_file_button:
                if(countFileToUpload < 3) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
//                    startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                    break;
                }else{
                    Toast toast;
                    toast =  Toast.makeText(context,"PUOI INSERIRE MASSIMO 3 FILE", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }

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
                    Uri selectedImageURI = data.getData();
                    FileSystemResource fsr = new FileSystemResource (getRealPathFromURI(selectedImageURI));
                    Log.d(TAG,"___" + getRealPathFromURI(selectedImageURI));
                    fileToUpload.add(fsr);
                    countFileToUpload++;
                    mNomiFile.append("  \n- "+ fsr.getFilename());
                    break;
                }
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
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
        private int user_id;

        public NewDocument_HttpRequest(Context context, Document document){
            progressDialog = new CustomProgressDialog(context,message);
            user = SessionManagement.getUserInSession(context);
            this.document = document;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if(!fileToUpload.isEmpty()) {
                    Iterator it = fileToUpload.iterator();
                    while (it.hasNext()) {

                        FileSystemResource fsr = (FileSystemResource) it.next();
                        String note = document.getNote();
                        user_id = user.getId();
                        final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/uploadDocument?nota=" + note + "&idu=" + user_id;

                        MultiValueMap<String, Object> para = new LinkedMultiValueMap<String, Object>();
                        para.add("file", fsr);
                        HttpHeaders headers = Header_Creator.create();
                        headers.set("method", "post");
                        headers.set("Content-Type", "multipart/form-data");
                        headers.set("enctype", "multipart/form-data");

                        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(para, headers);
                        RestTemplate restTemplate = new RestTemplate();
                        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                        if (!restTemplate.postForObject(url, requestEntity, boolean.class))
                            return false;
                    }
                }else{
                    String note = document.getNote();
                    user_id = user.getId();
                    final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/uploadDocumentWithoutFile?nota=" + note + "&idu=" + user_id;

                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                    HttpHeaders headers = Header_Creator.create();
                    HttpEntity entity = new HttpEntity(headers);

                    if (!restTemplate.postForObject(url, entity, boolean.class))
                        return false;
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

            final String dialogTitle = "Caricamento del documento";
            final String dialogMessage = "completato!";

            new AlertDialog.Builder(context)
                    .setTitle(dialogTitle)
                    .setMessage(dialogMessage)
                    .setPositiveButton("Continua", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(context,Diary.class);
                            startActivity(intent);
                            finish();
                        }})
                    .show();
        }
    }
}
