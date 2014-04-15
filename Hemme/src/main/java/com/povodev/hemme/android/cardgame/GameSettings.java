package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Game_Activity;
import com.povodev.hemme.android.asynctask.NewResult_HttpRequest;
import com.povodev.hemme.android.bean.Result;

/**
 * Created by Stefano on 15/04/14.
 */
public class GameSettings implements View.OnClickListener{

    private Result result;

    //TODO change it
    private int user_id = 1;

    private Context context;

    public GameSettings(Context context){
        this.context = context;
        result = new Result();
    }

    public void initAndStartGame(int difficulty){
        setSize(difficulty);
        cardSet = new CardSet(context, size);
        startGame();
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

    private ViewGroup activity_layout;

    /*
     * Start the game
     */
    private void startGame() {

        Game_Activity game_activity = (Game_Activity) context;
        activity_layout = (ViewGroup) game_activity.findViewById(R.id.game_activity_container);

        for (int i=0; i<size; i++){
            cardSet.get(i).setCardPosition(i);
            cardSet.get(i).setId(i);
            setComponentListener(cardSet.get(i));
            cardSet.get(i).setText(button_label);
            activity_layout.addView(cardSet.get(i));
        }
        startTimer();
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

        if (currentPosition!=-1){
            previousPosition = currentPosition;
        }

        currentPosition = view.getId();
        cardSet.get(cardSet.getArrayPosition(currentPosition)).setText(cardSet.get(cardSet.getArrayPosition(currentPosition)).getCardValue()+"");

        if (counter%2==0){
            cardSet.deletePairs(cardSet.get(cardSet.getArrayPosition(currentPosition)),cardSet.get(cardSet.getArrayPosition(previousPosition)));

            if (cardSet.winnerWinnerChickenDinner()) {
                Toast toast = Toast.makeText(context,"HAI VINTO!",Toast.LENGTH_SHORT);
                toast.show();
                stopTimer();
            }
        }
    }

    private GameTimer gameTimer;
    /*
     * Called to start the runnable.
     * This allows to have a auto-refreshing timer
     * for the game activity
     */
    private void startTimer() {
        gameTimer = new GameTimer(context);
        gameTimer.startRunner();
    }

    /*
     * Called to stop the runnable
     * and insert the result into
     * the database
     */
    private void stopTimer(){
        gameTimer.stopRunner();
        setTiming();
        new NewResult_HttpRequest(context,result,user_id).execute();
    }

    /*
     * Called to stop the runnable
     * without inserting the result into
     * the database
     */
    public void stopRunner(){
        gameTimer.stopRunner();
    }

    /*
     * Set grade for the bean Result
     */
    private void setGrade(String grade) {
        result.setGrade(grade);
    }

    /*
     * Set timing for the bean Result
     */
    private void setTiming() {
        result.setTime(gameTimer.getTiming());
    }

    /*
     * Those strings are used for game difficulty label
     */
    private final static String gradeEasy = "Facile (4)";
    private final static String gradeEasyMedium = "Facile (6)";
    private final static String gradeMedium = "Medio (8)";
    private final static String gradeMediumHard = "Medio (10)";
    private final static String gradeHard = "Difficile (12)";

    // Get the size of the game-map for the choosen difficulty
    private void setSize(int difficulty) {
        if (difficulty==1){
            setGrade(gradeEasy);
            size = 4;
        } else if (difficulty==2){
            setGrade(gradeEasyMedium);
            size = 6;
        } else if (difficulty==3){
            setGrade(gradeMedium);
            size = 8;
        } else if (difficulty==4){
            setGrade(gradeMediumHard);
            size = 10;
        } else {
            setGrade(gradeHard);
            size = 12;
        }
    }

    /*
     * Set the listener to the cards
     */
    private void setComponentListener(Button button) {
        button.setOnClickListener(this);
    }

}
