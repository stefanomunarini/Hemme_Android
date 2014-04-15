package com.povodev.hemme.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.R;
import com.povodev.hemme.android.adapter.Document_Adapter;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.dialog.CustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class Diary extends RoboActivity {

    private final String TAG = "Diary_Activity";
    final int ACTIVITY_CHOOSE_FILE = 1;
    private Context context;
    ProgressDialog mProgressDialog;

    @InjectView(R.id.note_image)        private TextView mNoteImageText;
    //@InjectView(R.id.image)             private ImageView mImageView;
    @InjectView(R.id.listview)          private ListView mListView;
    Bitmap image;

    private ArrayList<Document> diario;

    public static final String URL = "http://andreariscassi.files.wordpress.com/2011/12/sabato-6-novembre-2010-big-babol-party-discoteca-shango-roma.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading..");
        new NewDocument_HttpRequest(this).execute();
    }




    /**
     *    DOWNLOAD DELL'IMMAGINE E IMMAGAZZINAMENTO NEL SINGOLO DOCUMENT
     */
    class bitmapDownload extends AsyncTask<Void, Void, Void> {

        ArrayList<Document> d;

        public bitmapDownload( ArrayList<Document> d){
            this.d = d;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                image = downloadBitmap("https://cdn2.iconfinder.com/data/icons/despicable-me-2-minions/128/despicable-me-2-Minion-icon-5.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            Iterator i = diario.iterator();

            while (i.hasNext()){
                Document doc = (Document)i.next();
                doc.setFile_immagine(image);
            }

            ArrayAdapter adapter= new Document_Adapter(context,R.layout.diary_row_layout,diario);
            mListView.setAdapter(adapter);
            //mImageView.setImageBitmap(image);
        }
    }



    private Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final org.apache.http.HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    image = BitmapFactory.decodeStream(inputStream);


                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diary, menu);
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




    /**
     *    CREAZIONE ARRAY DI DOCUMENTI PRESENTI NEL DATABASE
     */
    private class NewDocument_HttpRequest extends AsyncTask<Void, Void, ArrayList<Document>> {

        private final String message = "Creazione Array in corso";
        private ProgressDialog progressDialog;

        public NewDocument_HttpRequest(Context context) {
            progressDialog = new CustomProgressDialog(context, message);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<Document> doInBackground(Void... params) {
            try {
                final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/getDiary?user_id=1";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                diario = restTemplate.getForObject(url, com.povodev.hemme.android.bean.Diary.class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Document> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            new bitmapDownload(diario).execute();
            //finito di generare il mio array di dcoument setto ad ognuno una immagine BiTmAp

        }
    }
}