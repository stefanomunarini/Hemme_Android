package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Stefano on 07/02/14.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(Context context, int resource, List<String> SpinnerArray) {
        super(context, resource, SpinnerArray);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
