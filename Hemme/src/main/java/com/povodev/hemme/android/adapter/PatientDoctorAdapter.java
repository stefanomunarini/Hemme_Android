package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.PatientDoctorItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano on 21/05/14.
 */
public class PatientDoctorAdapter  extends ArrayAdapter<PatientDoctorItem> {

    private Context context;
    private List<PatientDoctorItem> items;

    public PatientDoctorAdapter(Context context, int resource, ArrayList<PatientDoctorItem> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.patient_doctor_row, null);
        }

        PatientDoctorItem item = getItem(position);
        if (item != null) {
            TextView patientTextView = (TextView) view.findViewById(R.id.patient_item);
            TextView doctorTextView = (TextView) view.findViewById(R.id.doctor_item);
            if (patientTextView != null) {
                patientTextView.setText(item.getPatient_name() + " " + item.getPatient_surname());
            }
            if (doctorTextView != null) {
                doctorTextView.setText(item.getDoctor_name() + " " + item.getDoctor_surname());
            }
        }
        return view;
    }
}