package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.dialog.ListDialog;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 08/04/14.
 */
public class Game_Activity extends RoboActivity implements View.OnClickListener {

    private final String TAG = "Game_Activity";

    @InjectView(R.id.grade_edittext)            private EditText mGradeEditText;
    @InjectView(R.id.time_numberpicker)         private NumberPicker mTimeNumberPicker;
    @InjectView(R.id.date_datepicker)           private DatePicker mDateDatePicker;
    @InjectView(R.id.insert_new_result_button)  private Button mInsertNewResultButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_result);

        showStartingDialog(this);

    }

    private final String title = "Difficolta'";


    @InjectResource(R.id.difficulty) private final String[] list;

    private void showStartingDialog(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        ListDialog listDialog = new ListDialog();
    }

    private void setComponentsListener() {
        mInsertNewResultButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

        }
    }
}
