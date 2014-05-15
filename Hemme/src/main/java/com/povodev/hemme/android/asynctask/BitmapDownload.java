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


public class BitmapDownload extends AsyncTask<Void, Void, ArrayList<Document>> implements AbsListView.OnScrollListener{

    private final String TAG = "BitmapDownload_AsyncTask";

    int control = 0;
    Bitmap image;

    ArrayList<Document> diario_input;
    ArrayList<Document> diario_output;


    ProgressDialog mProgressDialog;
    Context context;
    ListView mListView;
    int max_view_element=8;
    int min_view_element=5;
    int i = 0;
    ArrayAdapter adapter;
    ArrayList<Document> diario_minus;
    Document d;

    public BitmapDownload(ArrayList<Document> diario, Context context){

        diario_input = diario;
        diario_output = new ArrayList<Document>();

        this.context = context;
        this.mProgressDialog = new CustomProgressDialog(context,"Creazione Diario in corso");
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }

    @Override
    public ArrayList<Document> doInBackground(Void... params) {
        try {

            for (int i = 0; i<diario_input.size(); i++){
                Document doc = diario_input.get(i);
                if(i%2==0){
                    image = downloadBitmap("http://www.lavilladelcane.it/images/cani1b.jpg");
                }else {
                    image = downloadBitmap("http://aironidicarta.files.wordpress.com/2010/11/nonno.jpg");
                }

                doc.setFile_immagine(image);
                diario_output.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diario_output;
    }

    @Override
    public void onPostExecute(ArrayList<Document> result) {
        super.onPostExecute(result);

        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();

        ArrayAdapter adapter = new Document_Adapter(context, R.layout.diary_row_layout,result);
        Diary diary = (Diary)context;
        ListView mListView = (ListView)diary.findViewById(R.id.listview);
        mListView.setAdapter(adapter);





/*
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
*/
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
/*
        if (mListView.getAdapter().getCount()>0) {
            if (mListView.getLastVisiblePosition() ==
                    mListView.getAdapter().getCount() - 1
                    && mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                if (max_view_element <= diario.size() && control == 0) {
                    for (int j = min_view_element; j < max_view_element; j++) {
                        Document doc = diario.get(j);
                        if(j%2 == 0) {
                            image = downloadBitmap("https://cdn2.iconfinder.com/data/icons/despicable-me-2-minions/128/despicable-me-2-Minion-icon-5.png");
                        }else{
                            image = downloadBitmap("http://immagini.disegnidacolorareonline.com/cache/data/disegni-colorati/serie-tv/disegno-videogiochi-mario-bros-a-colori-300x300.jpg");
                        }
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
                        if(j%2 == 0) {
                            image = downloadBitmap("https://cdn2.iconfinder.com/data/icons/despicable-me-2-minions/128/despicable-me-2-Minion-icon-5.png");
                        }else{
                            image = downloadBitmap("http://immagini.disegnidacolorareonline.com/cache/data/disegni-colorati/serie-tv/disegno-videogiochi-mario-bros-a-colori-300x300.jpg");
                        }
                        doc.setFile_immagine(image);
                        diario_minus.add(doc);
                    }
                    adapter.notifyDataSetChanged();
                    control++;
                }
            }
        } else {
            Toast.makeText(context,"mListView is null",Toast.LENGTH_SHORT).show();
        }*/
    }
}
