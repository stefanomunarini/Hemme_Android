package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.povodev.hemme.android.bean.ClinicalEvent;

/**
 * Created by Stefano on 02/04/14.
 */
public class ClinicalFolderAdapter extends ArrayAdapter<ClinicalEvent> {

    ArrayAdapter<ClinicalEvent> clinicalFolder;

    public ClinicalFolderAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
