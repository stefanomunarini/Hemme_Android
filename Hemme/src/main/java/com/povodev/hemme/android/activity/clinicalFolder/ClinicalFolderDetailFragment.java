package com.povodev.hemme.android.activity.clinicalFolder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.NewClinicaEvent_Activity;
import com.povodev.hemme.android.bean.ClinicalEvent;
import com.povodev.hemme.android.management.SessionManagement;
import com.povodev.hemme.android.utils.Formatters;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * A fragment representing a single ClinicalFolder detail screen.
 * This fragment is either contained in a {@link ClinicalFolderListActivity}
 * in two-pane mode (on tablets) or a {@link ClinicalFolderDetailActivity}
 * on handsets.
 */
public class ClinicalFolderDetailFragment extends RoboFragment implements View.OnClickListener {

    @InjectView(R.id.modify_clinical_event_button)  private Button mModifyClinicalEventButton;

    // Author name + surname to be retrieven from rest service
    public static String author;

    private final static String TAG = "ClinicalFolderDetailFragment";

    /**
     * The content this fragment is presenting.
     */
    private ClinicalEvent clinicalEvent;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClinicalFolderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clinicalEvent = (ClinicalEvent) getArguments().getSerializable("clinical_event");

        // retrieve author name and surname
        //new GetAuthor_HttpRequest(getActivity(),clinicalEvent.getAuthor()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clinicalfolder_detail, container, false);

        Log.d(TAG,"Terapia: " + clinicalEvent.getTherapy());

        Toast.makeText(getActivity(),clinicalEvent.getId()+"",Toast.LENGTH_SHORT).show();

        // Show the content as text in TextViews.
        if (clinicalEvent != null) {
            ((TextView) rootView.findViewById(R.id.clinicalfolder_therapy)).setText(clinicalEvent.getTherapy());
            ((TextView) rootView.findViewById(R.id.clinicalfolder_note)).setText(clinicalEvent.getNote());
            String date = Formatters.dateFormat(clinicalEvent.getDate());
            ((TextView) rootView.findViewById(R.id.clinicalfolder_date)).setText(date);
            ((TextView) rootView.findViewById(R.id.clinicalfolder_author)).setText(clinicalEvent.getAuthor_name());
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
    }

    private void initComponents() {
        /*
         * If the author of the clinical event is the same currently using the application
         * then modifications are allowed, otherwise not (so the button will not be shown)
         */
        if (clinicalEvent.getAuthor()==SessionManagement.getUserInSession(getActivity()).getId()) {
            mModifyClinicalEventButton.setOnClickListener(this);
        } else {
            mModifyClinicalEventButton.setVisibility(View.GONE);
        }
    }

    public static final String THERAPY_4_BUNDLE = "therapy";
    public static final String NOTE_4_BUNDLE = "note";
    public static final String ID_4_BUNDLE = "clinicalEvent_id";

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.modify_clinical_event_button:
                Intent intent = new Intent(getActivity(), NewClinicaEvent_Activity.class);
                intent.putExtra(THERAPY_4_BUNDLE,clinicalEvent.getTherapy());
                intent.putExtra(NOTE_4_BUNDLE, clinicalEvent.getNote());
                intent.putExtra(ID_4_BUNDLE,clinicalEvent.getId());
                startActivity(intent);
                break;
        }
    }

    /*private class GetAuthor_HttpRequest extends AsyncTask<Void, Void, String> {

        private int user_id;
        private ProgressDialog progressDialog;

        public GetAuthor_HttpRequest(Context context, int user_id){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Caricamento in corso...");
            this.user_id = user_id;
            Log.d(TAG,"Getting doctor name... user_id: "+user_id);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/getAuthor?user_id="+user_id;
                HttpHeaders headers = Header_Creator.create();
                HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);


                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ResponseEntity<String> stringRequest = restTemplate.exchange(url,
                        HttpMethod.GET,
                        requestEntity,
                        String.class);
                String result = stringRequest.getBody();

                //return restTemplate.getForObject(url, String.class);
                return result;

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String authorName) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (authorName!=null){
                Log.d(TAG, "Author: " + authorName);
                ClinicalFolderDetailFragment.author = authorName;
                ((TextView) getView().findViewById(R.id.clinicalfolder_author)).setText(author);
            }
            else Log.d(TAG,"Failed to retrieve author");
        }
    }*/
}
