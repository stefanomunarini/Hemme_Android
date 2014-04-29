package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.Result;
import com.povodev.hemme.android.utils.Formatters;

import java.util.ArrayList;

/**
 * Created by Stefano on 03/04/14.
 */
public class MemoryResultsAdapter extends ArrayAdapter<Result> {

    private Context context;

    public MemoryResultsAdapter(Context context, int textViewResourceId, ArrayList<Result> items) {
        super(context, textViewResourceId, items);

        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view = inflater.inflate(android.R.layout.simple_list_item_activated_2, null);
            view = inflater.inflate(R.layout.game_result_list_row, null);
        }

        Result item = getItem(position);
        if (item!= null) {

            //TextView gradeView = (TextView) view.findViewById(android.R.id.text1);
            //TextView dateView = (TextView) view.findViewById(android.R.id.text2);

            TextView gradeView = (TextView) view.findViewById(R.id.result_row_difficulty);
            TextView dateView = (TextView) view.findViewById(R.id.result_row_timestamp);
            TextView timeView = (TextView) view.findViewById(R.id.result_row_time);

            if (gradeView != null) {
                gradeView.setText(item.getGrade());
            }

            if (dateView != null) {
                String date = Formatters.timestampFormat(item.getDate());
                dateView.setText(date);
            }

            if (timeView != null) {
                String time = Formatters.timeFormat(item.getTime()) + "s";
                timeView.setText(time);
            }
        }

        return view;
    }
}
