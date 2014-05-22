package com.povodev.hemme.android.activity.memory_results;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.povodev.hemme.android.Configurator;
import com.povodev.hemme.android.adapter.MemoryResultsAdapter;
import com.povodev.hemme.android.bean.Result;
import com.povodev.hemme.android.bean.Test;
import com.povodev.hemme.android.utils.SessionManagement;
import com.povodev.hemme.android.utils.Header_Creator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Result. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MemoryDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MemoryResultsListFragment extends ListFragment {

    private final static String TAG = "MemoryResultsListFragment";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemoryResultsListFragment() {
    }

    private int user_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //User user = SessionManagement.getUserInSession(getActivity());
        user_id = SessionManagement.getPatientIdInSharedPreferences(getActivity());

        new TestLoader_HttpRequest(getActivity(),user_id).execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        ListView listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(8);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(""+position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    /**
     * Used to set the list adapter for this fragment
     */
    private void populateListView(ArrayList<Result> test) {
        ArrayAdapter adapter = new MemoryResultsAdapter(getActivity(),android.R.id.text1,test);
        this.setListAdapter(adapter);
    }

    private class TestLoader_HttpRequest extends AsyncTask<Void, Void, ArrayList<Result>> {

        private int user_id;
        public TestLoader_HttpRequest(Context context, int user_id){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Caricamento in corso...");

            this.user_id = user_id;
        }

        ProgressDialog progressDialog;

        @Override
        protected ArrayList<Result> doInBackground(Void... params) {

            try {
                final String url = "http://"+ Configurator.ip+"/"+Configurator.project_name+"/getTest?user_id="+user_id;
                HttpHeaders headers = Header_Creator.create();
                HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<Test> testRequest = restTemplate.exchange(url,
                        HttpMethod.GET,
                        requestEntity,
                        com.povodev.hemme.android.bean.Test.class);
                return testRequest.getBody();

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
        protected void onPostExecute(ArrayList<Result> test) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (test!=null) {
                MemoryResultsListFragment.test = test;
                populateListView(test);
            }
        }
    }

    private static ArrayList<Result> test;

    public static ArrayList<Result> getTest(){
        return test;
    }
}
