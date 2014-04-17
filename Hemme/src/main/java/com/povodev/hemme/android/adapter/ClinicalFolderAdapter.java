package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.povodev.hemme.android.bean.ClinicalEvent;
import com.povodev.hemme.android.utils.Formatters;

import java.util.ArrayList;

/**
 * Created by Stefano on 02/04/14.
 */
public class ClinicalFolderAdapter extends ArrayAdapter<ClinicalEvent> {

    private Context context;

    public ClinicalFolderAdapter(Context context, int textViewResourceId, ArrayList<ClinicalEvent> items) {
        super(context, textViewResourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_activated_2, null);
        }

        ClinicalEvent item = getItem(position);
        if (item!= null) {
            TextView theraphyView = (TextView) view.findViewById(android.R.id.text1);
            TextView dateView = (TextView) view.findViewById(android.R.id.text2);
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


}
