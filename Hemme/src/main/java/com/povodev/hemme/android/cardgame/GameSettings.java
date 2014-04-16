package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.Game_Activity;
import com.povodev.hemme.android.asynctask.NewResult_HttpRequest;
import com.povodev.hemme.android.bean.Result;

/**
 * Created by Stefano on 15/04/14.
 */
public class GameSettings implements AdapterView.OnItemClickListener{

    /*
     * The size for the CardList based on the difficulty choosed
     */
    private int size;

    private Result result;

    //TODO change it
    private int user_id = 1;

    private Context context;

    public GameSettings(Context context){
        this.context = context;
        result = new Result();

        Game_Activity game_activity = (Game_Activity) context;
        mGridView = (GridView) game_activity.findViewById(R.id.gridview);
    }

    public void initAndStartGame(int difficulty){
        setSize(difficulty);

        cardSet = new CardList(context, size);

        setGridViewAdapter(size);
        mGridView.setOnItemClickListener(this);

        startGame();
    }

    /*
     * The CardList used for the game
     * {@See CardList}
     */
    private CardList cardSet;

    private GridView mGridView;

    /*
     * Start the game
     */
    private void startGame() {
        startTimer();
    }

    private void setGridViewAdapter(int gridSize){
        mGridView.setAdapter(new ImageAdapter(context,gridSize,cardSet));
    }

    private void setImageToCard(Card card) {
        int value = card.getCardValue();
        Drawable image = getDrawable(value);
        card.setImageDrawable(image);
    }

    private Drawable getDrawable(int value) {
        if(value==1){
            return context.getResources().getDrawable(R.drawable.one);
        } else if(value==2){
            return context.getResources().getDrawable(R.drawable.two);
        } else if(value==3){
            return context.getResources().getDrawable(R.drawable.three);
        } else if(value==4){
            return context.getResources().getDrawable(R.drawable.four);
        } else if(value==5){
            return context.getResources().getDrawable(R.drawable.five);
        } else if(value==6){
            return context.getResources().getDrawable(R.drawable.six);
        }
        return context.getResources().getDrawable(R.drawable.one);
    }

    private GameTimer gameTimer;
    /*
     * Called to start the runnable.
     * This allows to have a auto-refreshing timer
     * for the game activity
     */
    private void startTimer() {
        if (gameTimer!=null){
            gameTimer=null;
        }
        gameTimer = new GameTimer(context);
        gameTimer.startRunner();
    }

    /*
     * Called to stop the runnable
     * and insert the result into
     * the database
     */
    private void stopTimer(){
        stopRunner();
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
     * The number of cards flipped
     */
    private int counter = 0;

    /*
     * The button just pressed id
     */
    int currentPosition = -1;

    /*
     * The button previously pressed id
     */
    int previousPosition = -1;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        counter++;
        if (currentPosition != -1) {
            previousPosition = currentPosition;
        }
        currentPosition = position;

        setImageToCard((Card)mGridView.getChildAt(currentPosition-mGridView.getFirstVisiblePosition()));

        if (counter % 2 == 0) {
            cardSet.deletePairs(cardSet.get(currentPosition), cardSet.get(previousPosition));

            if (cardSet.winnerWinnerChickenDinner()) {
                Toast toast = Toast.makeText(context, "HAI VINTO!", Toast.LENGTH_SHORT);
                toast.show();
                stopTimer();
            }
        }
    }
}
