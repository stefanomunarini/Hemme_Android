package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * Created by gbonadiman.stage on 15/04/2014.
 */
public class BitmapDownload extends AsyncTask<Void, Void, Void> implements AbsListView.OnScrollListener{

    private final String TAG = "BitmapDownload_AsyncTask";

    int control = 0;
    Bitmap image;
    ArrayList<Document> diario;
    ProgressDialog mProgressDialog;
    Context context;
    ListView mListView;
    int max_view_element=8;
    int min_view_element=5;
    ArrayAdapter adapter;
    ArrayList<Document> diario_minus;

    public BitmapDownload(ArrayList<Document> diario, Context context){
        this.diario = diario;
        this.context = context;
        this.mProgressDialog = new CustomProgressDialog(context,"Creazione Diario in corso");
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }

    @Override
    public Void doInBackground(Void... params) {
        try {
            image = downloadBitmap("https://cdn2.iconfinder.com/data/icons/despicable-me-2-minions/128/despicable-me-2-Minion-icon-5.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();

        diario_minus = new ArrayList<Document>();
        for (int i = 0; i<5; i++){
            if (i < diario.size() ){
                Document doc = diario.get(i);
                doc.setFile_immagine(image);
                diario_minus.add(doc);
            }
        }

        adapter = new Document_Adapter(context, R.layout.diary_row_layout,diario_minus);
        Diary diary = (Diary)context;

        mListView = (ListView)diary.findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(this);
    }


    public Bitmap downloadBitmap(String url) {
        // initialize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttpGet request
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
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}
    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        if (mListView.getAdapter().getCount()>0) {
            Log.d(TAG,"a "+mListView.getAdapter().getCount());
            if (mListView.getLastVisiblePosition() ==
                    mListView.getAdapter().getCount() - 1
                    && mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                if (max_view_element <= diario.size() && control == 0) {
                    Log.d(TAG, "entrato nella fine");
                    for (int j = min_view_element; j < max_view_element; j++) {
                        Document doc = diario.get(j);
                        doc.setFile_immagine(image);
                        diario_minus.add(doc);
                    }
                    min_view_element += 3;
                    max_view_element += 4;
                    adapter.notifyDataSetChanged();
                } else if (control == 1) {
                    Toast toast;
                    toast = Toast.makeText(context, "Fine del Diario", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    for (int j = min_view_element; j < diario.size(); j++) {
                        Document doc = diario.get(j);
                        doc.setFile_immagine(image);
                        diario_minus.add(doc);
                    }
                    adapter.notifyDataSetChanged();
                    control++;
                }
            }
        } else {
            Toast.makeText(context,"mListView is null",Toast.LENGTH_SHORT).show();
        }
    }
}
