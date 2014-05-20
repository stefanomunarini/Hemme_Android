package com.povodev.hemme.android.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.location.LocationChecker;
import com.povodev.hemme.android.location.LocationVariables;
import com.povodev.hemme.android.bean.CoordinatesHashMap;
import com.povodev.hemme.android.dialog.CustomProgressDialog;
import com.povodev.hemme.android.utils.Header_Creator;
import com.povodev.hemme.android.utils.SessionManagement;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Stefano on 19/05/14.
 */
public class GetLocationVariables_HttpRequest extends AsyncTask<Void, Void, CoordinatesHashMap/*HashMap<String, Number>*/> {

    private String TAG = "GetCoordinates_AsyncTask";

    private final String message = "Recupero dati...";
    private ProgressDialog progressDialog;
    private int user_id;
    private Context context;

    public GetLocationVariables_HttpRequest(Context context) {
        user_id = SessionManagement.getUserInSession(context).getId();
        progressDialog = new CustomProgressDialog(context, message);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected CoordinatesHashMap doInBackground(Void... params) {
        try {

            //TODO change address
            final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/getCoordinates?user_id="+user_id;

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<CoordinatesHashMap> request = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    CoordinatesHashMap.class);
            return request.getBody();
        }

        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(CoordinatesHashMap result) {
        super.onPostExecute(result);
        if (progressDialog.isShowing()) progressDialog.dismiss();

        float lat = result.get(LocationVariables.LATITUDE_SHAREDPREFERENCES).floatValue();
        float lon = result.get(LocationVariables.LONGITUDE_SHAREDPREFERENCES).floatValue();
        int radius = result.get(LocationVariables.RADIUS_SHAREDPREFERENCES).intValue();

        LocationVariables.setLat(context,lat);
        LocationVariables.setLon(context, lon);
        LocationVariables.setRadius(context, radius);

        // add proximity alert
        LocationChecker.addProximityAlert(context,lat,lon,radius);

    }
}