package com.povodev.hemme.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.BitmapDownload;
import com.povodev.hemme.android.asynctask.Diary_HttpRequest;
import com.povodev.hemme.android.bean.Document;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class Diary extends RoboActivity {

    private final String TAG = "Diary_Activity";
    private Context context;
    public static ArrayList<Document> diario;

    @InjectView(R.id.listview)          private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;

        new Diary_HttpRequest(context).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.diary_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new_document:
                startNewDocumentActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startNewDocumentActivity() {
        Intent intent = new Intent(this,NewDocument_Activity.class);
        startActivity(intent);
        finish();
    }
}