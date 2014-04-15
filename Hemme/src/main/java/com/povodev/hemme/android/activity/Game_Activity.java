package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.asynctask.NewResult_HttpRequest;
import com.povodev.hemme.android.bean.Result;
import com.povodev.hemme.android.cardgame.CardSet;
import com.povodev.hemme.android.cardgame.GameSettings;
import com.povodev.hemme.android.cardgame.GameTimer;
import com.povodev.hemme.android.dialog.ListDialog;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * Created by Stefano on 09/04/14.
 */
public class Game_Activity extends RoboActivity implements View.OnClickListener, ListDialog.OnDifficultySelectedListener {

    private static final String TAG = "Game_Activity";
    private Context context;

    /*
     * Timer TextView
     */
    @InjectView(R.id.timer_textview)                private TextView mTimerTextView;

    /*
     * Store the result of the game
     */
    private Result result;

    /*
     * Convenient class to help setting Result bean
     * {@See GameSetting}
     */
    private GameSettings gameSettings;

    private int user_id;

    // The Activity container layout
    private ViewGroup activity_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.context = this;
        activity_layout = (ViewGroup) findViewById(R.id.game_activity_container);

        gameSettings = new GameSettings();


        //TODO change with correct user_id
        user_id = 1;

        showStartingDialog();
    }

    // A String array containing difficulties to be choosen for the current game
    @InjectResource(R.array.difficulty)             private String[] list;

    // Title for difficulty chooser dialog
    @InjectResource(R.string.difficulty_title)      private String title;

    /*
     * Creates and shows the dialog that lets the user choose the difficulty of the game
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
     * The number of cards flipped
     */
    private int counter = 0;

    /*
     * The label for the front side of the Card
     * TODO change it
     */
    private final String button_label = "Click me!";

    /*
     * The button just pressed id
     */
    int currentPosition = -1;

    /*
     * The button previously pressed id
     */
    int previousPosition = -1;

    @Override
    public void onClick(View view) {
        counter++;

        if (counter%3==0){
            cardSet.get(cardSet.getArrayPosition(currentPosition)).setText(button_label);
            cardSet.get(cardSet.getArrayPosition(previousPosition)).setText(button_label);
        }

        if (currentPosition!=-1){
            previousPosition = currentPosition;
        }

        currentPosition = view.getId();
        cardSet.get(cardSet.getArrayPosition(currentPosition)).setText(cardSet.get(cardSet.getArrayPosition(currentPosition)).getCardValue()+"");

        if (counter%2==0){
            /*if(!*/cardSet.deletePairs(cardSet.get(cardSet.getArrayPosition(currentPosition)),cardSet.get(cardSet.getArrayPosition(previousPosition)));//){


            /*} else*/ if (cardSet.winnerWinnerChickenDinner()) {
                Toast toast = Toast.makeText(context,"HAI VINTO!",Toast.LENGTH_SHORT);
                toast.show();
                stopTimer();
            }
        }
    }

    /*
     * The size for the CardSet based on the difficulty choosed
     */
    private int size;

    /*
     * The CardSet used for the game
     * {@See CardSet}
     */
    private CardSet cardSet;

    /*
     * Get the event touch from ListDialog
     * Called when a user select the game difficulty
     */
    @Override
    public void onDifficultySelected(int difficulty) {

        gameSettings.setSize(difficulty);
        size = gameSettings.getSize();
        cardSet = new CardSet(this, size);

        startGame(size);
    }

    /*
     * Init the game
     */
    private void startGame(int size) {
        for (int i=0; i<size; i++){

            cardSet.get(i).setCardPosition(i);
            cardSet.get(i).setId(i);

            setComponentListener(cardSet.get(i));
            cardSet.get(i).setText(button_label);
            activity_layout.addView(cardSet.get(i));
        }
        startTimer();
    }

    private GameTimer gameTimer;
    /*
     * Called to start the runnable.
     * This allows to have a auto-refreshing timer
     * for the game activity
     */
    private void startTimer() {
        gameTimer = new GameTimer(this);
        gameTimer.startRunner();
    }

    /*
     * Called to stop the runnable
     */
    private void stopTimer(){
        gameTimer.stopRunner();
        gameSettings.setTiming(gameTimer);
        result = gameSettings.getResult();
        new NewResult_HttpRequest(context,result,user_id).execute();
    }

    /*
     * Set the listener to the objects (the cards)
     */
    private void setComponentListener(Button button) {
        button.setOnClickListener(this);
    }

    /*
     * Called by GameTimer (Handler for Runnable)
     * to increment game timer
     */
    public void setTvText(String str){
        mTimerTextView.setText(str);
    }
}