package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Diary;
import com.povodev.hemme.android.adapter.Document_Adapter;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.dialog.CustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by gbonadiman.stage on 15/04/2014.
 */
class bitmapDownload extends AsyncTask<Void, Void, Void> {

    Bitmap image;
    ArrayList<Document> diario;
    ProgressDialog mProgressDialog;
    Context context;

    public bitmapDownload( ArrayList<Document> diario,Context context){
        this.diario = diario;
        this.context = context;
        this.mProgressDialog = new CustomProgressDialog(context,"Creazione Diario in corso");
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

        ArrayAdapter adapter= new Document_Adapter(context, R.layout.diary_row_layout,diario);


        Diary diary = (Diary)context;
        ListView mListView = (ListView)diary.findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        //mImageView.setImageBitmap(image);
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
}
