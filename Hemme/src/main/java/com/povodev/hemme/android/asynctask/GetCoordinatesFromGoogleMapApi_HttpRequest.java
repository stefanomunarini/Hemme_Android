package com.povodev.hemme.android.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.povodev.hemme.android.utils.VibratingToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Given an address, get the coordinates usi google api.
 * Example here: http://maps.google.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=false
 * Created by Stefano on 19/05/14.
 */
public class GetCoordinatesFromGoogleMapApi_HttpRequest extends AsyncTask<Void,Void,long[]> {

    private String TAG = "GetCoordinatesFromGoogleMapApi_HttpRequest_AsyncTask";

    private Context context;

    private String via;
    private String numerocivico;
    private String citta;

    public GetCoordinatesFromGoogleMapApi_HttpRequest(Context context, String via, String numerocivico, String citta) {
        this.context = context;
        this.via = via;
        this.numerocivico = numerocivico;
        this.citta = citta;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private long lat;
    private long lon;
    @Override
    protected long[] doInBackground(Void... params) {

        String url = "http://maps.google.com/maps/api/geocode/json?address="+numerocivico+"+";
        String[] token = via.split(" ");
        for (int i=0; i<token.length; i++){
            url+=token[i]+"+";
        }
        url+=","+citta;
        url+="&sensor=false";

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(url);

        Log.d(TAG,url);

        // Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            // Oops
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }


        JSONObject json = null;
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getCurrentLocationViaJSON(json);

        /*assert json != null;
        JSONArray results = null;
        try {
            results = json.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert results != null;
        JSONArray address_components = null;
        try {
            address_components = results.getJSONArray(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert address_components != null;
        JSONObject geometry = null;
        try {
            geometry = address_components.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,geometry.names().toString());

        assert geometry != null;
        JSONObject location = null;
        try {
            location = geometry.getJSONObject("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,location.names().toString());

        assert location != null;
        try {
            lat = location.getLong("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            lon = location.getLong("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"Lat: "+lat+" Lon: "+lon);

        long[] array = {lat,lon};*/

        return null;
    }

    private long[] getCurrentLocationViaJSON(JSONObject jsonObject) {

        //Log.i(TAG, jsonObject.toString());

        long lat;
        long lon;

        try {
            String status = jsonObject.getString("status").toString();

            Log.d(TAG,"Status: "+status);

            if(status.equals("OK")){
                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject geometry = results.getJSONObject(2);

                Log.d(TAG, geometry.toString());

                JSONObject location = geometry.getJSONObject("location");

                Log.d(TAG, location.toString());

                lat = location.getLong("lat");
                lon = location.getLong("lng");

                new VibratingToast(context,lat + " /// " + lon, Toast.LENGTH_SHORT);

                Log.d(TAG, lat + " /// " + lon);

                return null;
            }
        } catch (JSONException e) {
            Log.e("testing","Failed to load JSON");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(long[] result) {
        super.onPostExecute(result);

        /*CoordinatesHashMap coordinatesHashMap = new CoordinatesHashMap();
        coordinatesHashMap.put(LocationVariables.LATITUDE_SHAREDPREFERENCES,result[0]);
        coordinatesHashMap.put(LocationVariables.LONGITUDE_SHAREDPREFERENCES,result[1]);*/

        //TODO chiamare questa funzione queando lato server è stato implementato
        //new SetLocationVariables_HttpRequest(context,coordinatesHashMap).execute();

    }
}