package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.NewResult_HttpRequest;
import com.povodev.hemme.android.bean.Result;

import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 03/04/14.
 */
public class New_Result extends RoboActivity implements View.OnClickListener {

    private final String TAG = "NewResult_Activity";

    @InjectView(R.id.grade_edittext)            private EditText mGradeEditText;
    @InjectView(R.id.time_numberpicker)         private NumberPicker mTimeNumberPicker;
    @InjectView(R.id.date_datepicker)           private DatePicker mDateDatePicker;
    @InjectView(R.id.insert_new_result_button)  private Button mInsertNewResultButton;

    private Context context;

    private int user_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_result);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        // TODO eliminare queste righe
        mGradeEditText.setText("kdfkfd");
        mTimeNumberPicker.setMinValue(1);
        mTimeNumberPicker.setMaxValue(200);

        // TODO inserire il giusto user_id
        user_id = 1;

        setComponentsListener();
    }

    private void setComponentsListener() {
        mInsertNewResultButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.insert_new_result_button:
                new NewResult_HttpRequest(context,getResult(),user_id).execute();
                break;
        }

    }

    private Result getResult() {
        return getResultValues();
    }

    private String grade;
    private int time;

    private Result getResultValues() {
        grade = mGradeEditText.getText().toString();
        time = mTimeNumberPicker.getValue();
        String data =  mDateDatePicker.getDayOfMonth() + "-" + mDateDatePicker.getMonth() + "-" + mDateDatePicker.getYear();

        DateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat("dd-MM-yy");
        try {
            date = (Date)formatter.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timeStampDate = new
                Timestamp(date.getTime());

        return setResultValues(grade,time,timeStampDate);
    }

    private Result setResultValues(String grade, int time, Timestamp date) {
        Result result = new Result();
        result.setGrade(grade);
        result.setTime(time);
        result.setDate(date);

        return result;
    }

}
