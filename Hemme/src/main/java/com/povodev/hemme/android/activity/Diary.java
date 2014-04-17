package com.povodev.hemme.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.adapter.Document_Adapter;
import com.povodev.hemme.android.asynctask.NewDocument_HttpRequest;
import com.povodev.hemme.android.bean.Document;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class Diary extends RoboActivity implements AbsListView.OnScrollListener{

    private final String TAG = "Diary_Activity";
    private Context context;
    public static ArrayList<Document> diario;

    private int visibleThreshold = 20;
    private int currentPage = 0;


    @InjectView(R.id.note_image)        private TextView mNoteImageText;
    @InjectView(R.id.listview)          private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;
        new NewDocument_HttpRequest(context).execute();

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {


        if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1
                && mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                Log.d(TAG, "entrato nella fine");
                //scroll end reached
                //Write your code here
        }
    }
}