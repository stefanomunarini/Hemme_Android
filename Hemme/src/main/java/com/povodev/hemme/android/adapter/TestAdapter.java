package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.povodev.hemme.android.bean.Result;

import java.util.ArrayList;

/**
 * Created by Stefano on 03/04/14.
 */
public class TestAdapter extends ArrayAdapter<Result> {

    private Context context;

    public TestAdapter(Context context, int textViewResourceId, ArrayList<Result> items) {
        super(context, textViewResourceId, items);

        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_activated_2, null);
        }

        Result item = getItem(position);
        if (item!= null) {

            TextView gradeView = (TextView) view.findViewById(android.R.id.text1);
            TextView dateView = (TextView) view.findViewById(android.R.id.text2);

            if (gradeView != null) {
                gradeView.setText(item.getGrade());
            }

            if (dateView != null) {
                dateView.setText(item.getDate()+"");
            }
        }

        return view;
    }


}
