package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.ClinicalEvent;
import com.povodev.hemme.android.utils.Formatters;

import java.util.ArrayList;

/**
 * Created by Stefano on 02/04/14.
 */
public class ClinicalFolderAdapter extends ArrayAdapter<ClinicalEvent> implements Filterable {

    private final String TAG = "ClinicalFolderAdapter";

    private Context context;
    private ArrayList<ClinicalEvent> clinicalFolder;

    public ClinicalFolderAdapter(Context context, int textViewResourceId, ArrayList<ClinicalEvent> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.clinicalFolder = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.clinicalfolder_list_row, null);
        }

        ClinicalEvent item = getItem(position);
        if (item!= null) {
            TextView theraphyView = (TextView) view.findViewById(R.id.clinicalfolder_row_therapy);
            TextView dateView = (TextView) view.findViewById(R.id.clinicalfolder_row_date);

            if (theraphyView != null) {
                theraphyView.setSingleLine(true);
                theraphyView.setEllipsize(TextUtils.TruncateAt.END);
                theraphyView.setText(item.getTherapy());
            }
            if (dateView != null) {
                String date = Formatters.dateFormat(item.getDate());
                dateView.setSingleLine(true);
                dateView.setEllipsize(TextUtils.TruncateAt.END);
                dateView.setText(date);
            }
        }
        return view;
    }

    public int getCount() {
        return clinicalFolder.size();
    }

    public ClinicalEvent getItem(int position) {
        return clinicalFolder.get(position);
    }

    public long getItemId(int position) {
        return clinicalFolder.get(position).getId();
    }

    private ClinicalFolder_Filter clinicalFolder_filter;

    @Override
    public Filter getFilter() {

        if (clinicalFolder_filter == null)
            clinicalFolder_filter = new ClinicalFolder_Filter(clinicalFolder);

        return clinicalFolder_filter;
    }

    /*
     * Class used to filter the Clinical Folder
     * based on user input in search bar
     */
    private class ClinicalFolder_Filter extends Filter {

        private ArrayList<ClinicalEvent> mmClinicalFolder;

        /*
         * Constructor
         * @param: clinicalFolder
         */
        public ClinicalFolder_Filter(ArrayList<ClinicalEvent> clinicalFolder){
            this.mmClinicalFolder = clinicalFolder;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            Log.d(TAG, "Finding... "+constraint);

            if (constraint == null || constraint.length() == 0) {
                // Don't do filter, because the search word is null
                results.values = mmClinicalFolder;
                results.count = mmClinicalFolder.size();
            } else {
                // We perform filtering operation
                ArrayList<ClinicalEvent> mClinicalFolder = new ArrayList<ClinicalEvent>();

                for (ClinicalEvent clinicalEvent : mmClinicalFolder) {

                    String therapy = clinicalEvent.getTherapy().toUpperCase();
                    String note = clinicalEvent.getNote().toUpperCase();
                    if (therapy.contains(constraint.toString().toUpperCase()) || note.contains(constraint.toString().toUpperCase())) {
                        mClinicalFolder.add(clinicalEvent);
                    }
                }

                results.values = mClinicalFolder;
                results.count = mClinicalFolder.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            // Now we have to inform the adapter about the new list filtered
            if (filterResults.count == 0) {
                notifyDataSetInvalidated();
                Log.d(TAG,"notifyDataSetInvalidated");
            }
            else {
                clinicalFolder = (ArrayList<ClinicalEvent>) filterResults.values;
                Log.d(TAG, "notifyDataSetChanged");
                notifyDataSetChanged();
            }
        }
    }

}
