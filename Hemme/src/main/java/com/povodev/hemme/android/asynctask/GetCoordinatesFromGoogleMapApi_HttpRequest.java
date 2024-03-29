package com.povodev.hemme.android.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.povodev.hemme.android.bean.LocationCoordinates;

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
public class GetCoordinatesFromGoogleMapApi_HttpRequest extends AsyncTask<Void,Void,double[]> {

    private String TAG = "GetCoordinatesFromGoogleMapApi_HttpRequest_AsyncTask";

    private Context context;

    private String via;
    private String numerocivico;
    private String citta;
    private int radius;

    public GetCoordinatesFromGoogleMapApi_HttpRequest(Context context, String via, String numerocivico, String citta, int radius) {
        this.context = context;
        this.via = via;
        this.numerocivico = numerocivico;
        this.citta = citta;
        this.radius = radius;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private float lat;
    private float lon;
    @Override
    protected double[] doInBackground(Void... params) {

        String url = "http://maps.google.com/maps/api/geocode/json?address="+numerocivico.replace(" ","")+"+";
        String[] token = via.split(" ");
        for (int i=0; i<token.length; i++){
            url+=token[i]+"+";
        }
        url+=","+citta.replace(" ","");
        url+="&sensor=false";

        Log.d(TAG, url);

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(url);

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


        return parsePoints(result);
    }

    @Override
    protected void onPostExecute(double[] result) {
        super.onPostExecute(result);

        if (result!=null) {
            LocationCoordinates locationCoordinates = new LocationCoordinates();
            locationCoordinates.setLatitude(result[0]);
            locationCoordinates.setLongitude(result[1]);
            locationCoordinates.setRadius(radius);

            new SetLocationVariables_HttpRequest(context, locationCoordinates).execute();
        } else {
            Toast.makeText(context,"Errore! Inserire un indirizzo valido", Toast.LENGTH_SHORT).show();
        }

    }

    private double[] parsePoints(String strResponse) {

        double[] result = new double[2];
        try {
            JSONObject obj = new JSONObject(strResponse);

            String status = obj.getString("status");

            Log.d(TAG,"Status: " + status);

            if (status.equals("OK")){

                JSONArray array = obj.getJSONArray("results");

                for(int i=0;i<array.length();i++) {
                    JSONObject item = array.getJSONObject(i);

                    JSONObject geoJson = item.getJSONObject("geometry");
                    JSONObject locJson = geoJson.getJSONObject("location");
                    result[0] = Double.parseDouble(locJson.getString("lat"));
                    result[1] = Double.parseDouble(locJson.getString("lng"));

                    return result;
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}