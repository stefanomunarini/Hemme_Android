package com.povodev.hemme.android.activity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.cardgame.GameSettings;
import com.povodev.hemme.android.dialog.ListDialog;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 09/04/14.
 */
public class Game_Activity extends RoboActivity implements ListDialog.OnDifficultySelectedListener {

    public static final String TAG = "Game_Activity";

    /*
     * Timer TextView
     */
    @InjectView(R.id.timer_textview)                private TextView mTimerTextView;

    /*
     * Game grid
     */
    @InjectView(R.id.gridview)                      private GridView mGridView;

    /*
     * Convenient class to help settings Result bean
     * {@See GameSetting}
     */
    private GameSettings gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameSettings = new GameSettings(this);

        showStartingDialog();
    }

    // A String array containing difficulties to be choosen for the current game
    @InjectResource(R.array.difficulty)             private String[] list;

    // Title for difficulty chooser dialog
    @InjectResource(R.string.difficulty_title)      private String title;

    /*
     * Creates and shows the dialog that allows the user
     * to choose the game difficulty
     */
    private void showStartingDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putStringArray("list",list);
        ListDialog listDialog = new ListDialog();
        listDialog.setArguments(bundle);
        listDialog.show(getFragmentManager().beginTransaction(), "difficulty dialog");
    }

    /*
     * Get the event touch from ListDialog
     * Called when a user select the game difficulty
     */
    @Override
    public void onDifficultySelected(int difficulty) {
        gameSettings.initAndStartGame(difficulty);
    }

    /*
     * Called by GameTimer (Handler for Runnable)
     * to increment game timer
     */
    public void setTvText(String str){
        mTimerTextView.setText(str);
    }

    @Override
    protected void onStop(){
        super.onStop();
        gameSettings.stopRunner();
    }
}