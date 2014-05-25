package com.povodev.hemme.android.asynctask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.bean.LocationCoordinates;
import com.povodev.hemme.android.location.LocationChecker;
import com.povodev.hemme.android.location.LocationVariables;
import com.povodev.hemme.android.utils.Header_Creator;
import com.povodev.hemme.android.utils.LocationUtils;
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
public class GetLocationVariables_HttpRequest extends AsyncTask<Void, Void, LocationCoordinates> implements SeekBar.OnSeekBarChangeListener {

    public static String TAG = "GetCoordinates_AsyncTask";

    private final String message = "Recupero dati...";
    private ProgressDialog progressDialog;
    private int user_id;
    private Context context;

    private boolean isTutor = false;

    /*
     * This constructor is called by PatientActivity
     */
    public GetLocationVariables_HttpRequest(Context context) {
        user_id = SessionManagement.getUserInSession(context).getId();
        this.context = context;
    }

    /*
     * This constructor is called by Tutor to retrieve coordinates of previous address
     */
    public GetLocationVariables_HttpRequest(Context context, boolean tutor) {
        user_id = SessionManagement.getPatientIdInSharedPreferences(context);
        this.context = context;
        isTutor = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LocationCoordinates doInBackground(Void... params) {
        try {

            Log.d(TAG,"User id " + user_id);

            final String url = "http://" + Configurator.ip + "/" + Configurator.project_name + "/getCoordinates?user_id="+user_id;

            HttpHeaders headers = Header_Creator.create();
            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<LocationCoordinates> request = restTemplate.exchange(url,
                    HttpMethod.GET,
                    requestEntity,
                    LocationCoordinates.class);
            return request.getBody();
        }

        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(LocationCoordinates result) {
        super.onPostExecute(result);

        if (result!=null && !isTutor){

            double lat = result.getLatitude();
            double lon = result.getLongitude();
            int radius = result.getRadius();

            Log.d(TAG, "Tutor: Lat " +lat + "  Lon "+lon);

            LocationVariables.setLat(context,lat);
            LocationVariables.setLon(context, lon);
            LocationVariables.setRadius(context, radius);

            if (lat!=0 && lon!=0) {

                LocationChecker.checkLocation(context,lat,lon,radius);
                // add proximity alert
                LocationChecker.addProximityAlert(context, lat, lon, radius);
            }
        } else if (result!=null && isTutor){
            double lat = result.getLatitude();
            double lon = result.getLongitude();

            Log.d(TAG, "Lat " +lat + "  Lon "+lon);

            String address = LocationUtils.getAddressFromCoordinates(context,lat,lon);

            Log.d(TAG, "Address " +address);

            createDialog(context, address);
        }

    }

    private void createDialog(final Context context, String address) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);

        if (address!=null) {
            TextView lastAddress = new TextView(context);
            lastAddress.setText("Indirizzo precedente: " + address);
            lastAddress.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Medium);

            container.addView(lastAddress);
        }

        final EditText viaEditText = new EditText(context);
        viaEditText.setHint("Indirizzo");

        final EditText numeroCivicoEditText = new EditText(context);
        numeroCivicoEditText.setHint("Numero Civico");

        final EditText cittaEditText = new EditText(context);
        cittaEditText.setHint("Città");

        seekBarValue = new TextView(context);
        seekBarValue.setText("Range[km]: 0");
        seekBarValue.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Medium);
        final SeekBar radiusSeek = new SeekBar(context);
        radiusSeek.setMax(100);
        radiusSeek.setOnSeekBarChangeListener(this);

        /*seekBarContainer.addView(seekBarValue);
        seekBarContainer.addView(radiusSeek);
*/
        container.addView(viaEditText);
        container.addView(numeroCivicoEditText);
        container.addView(cittaEditText);
        container.addView(seekBarValue);
        container.addView(radiusSeek);

        builder.setTitle("Indirizzo")
                .setView(container)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Annulla", null);
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String via = viaEditText.getText().toString();
                        String numerocivico = numeroCivicoEditText.getText().toString();
                        String citta = cittaEditText.getText().toString();

                        if (via.equals("") || numerocivico.equals("") || citta.equals("")) {
                            Toast toast = Toast.makeText(context, "Compilare tutti i campi.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            if (radius<1000){
                                radius = 1000;
                            }
                            new GetCoordinatesFromGoogleMapApi_HttpRequest(context, via, numerocivico, citta, radius).execute();
                            dialog.dismiss();
                        }
                    }
                });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }

    private int radius = 1000;
    TextView seekBarValue;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        radius = progress * 1000;
        seekBarValue.setText("Range[km]: "+String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}