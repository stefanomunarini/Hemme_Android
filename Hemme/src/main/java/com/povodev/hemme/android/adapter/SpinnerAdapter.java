package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.povodev.hemme.android.bean.User;

import java.util.ArrayList;

/**
 * Created by Stefano on 07/02/14.
 */
public class SpinnerAdapter extends ArrayAdapter<User> {

    private ArrayList<User> users;
    private Context context;

    public SpinnerAdapter(Context context, int resource, ArrayList<User> items) {
        super(context, resource, items);
        this.users = items;
        this.context = context;
        //setDropDownViewResource(resource);
    }

    private final String TAG = "SpinnerAdapter";
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        User item = users.get(position);
        if (item != null) {
            Log.d(TAG,item.getName());
            TextView tv = (TextView) view
                    .findViewById(android.R.id.text1);
            tv.setText(item.getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        User item = users.get(position);
        if (item != null) {
            Log.d(TAG,item.getName());
            TextView tv = (TextView) view
                    .findViewById(android.R.id.text1);
            tv.setText(item.getName());
        }
        return view;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }
}
