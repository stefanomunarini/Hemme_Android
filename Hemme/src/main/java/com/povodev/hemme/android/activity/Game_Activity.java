package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.cardgame.CardSet;
import com.povodev.hemme.android.dialog.ListDialog;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;

/**
 * Created by Stefano on 09/04/14.
 */
public class Game_Activity extends RoboActivity implements View.OnClickListener, ListDialog.OnDifficultySelectedListener {

    public static final String TAG = "Game_Activity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.context = this;
        showStartingDialog();
    }

    // A String array containing difficulties to be choosen for the current game
    @InjectResource(R.array.difficulty)             private String[] list;

    // Title for difficulty chooser dialog
    @InjectResource(R.string.difficulty_title)      private String title;

    // Creates and shows the dialog that lets the user to choose the difficulty of the game
    private void showStartingDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putStringArray("list",list);
        ListDialog listDialog = new ListDialog();
        listDialog.setArguments(bundle);
        listDialog.show(getFragmentManager().beginTransaction(), "difficulty dialog");
    }

    // Counts the number of cards flipped
    private int counter = 0;

    private final String button_label = "Click me!";

    int currentPosition = -1;
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
            if(!cardSet.deletePairs(cardSet.get(cardSet.getArrayPosition(currentPosition)),cardSet.get(cardSet.getArrayPosition(previousPosition)))){
                /*cardSet.get(cardSet.getArrayPosition(currentPosition)).setText(button_label);
                cardSet.get(cardSet.getArrayPosition(previousPosition)).setText(button_label);*/
            } else if (cardSet.size()==0){
                Toast toast = Toast.makeText(context,"HAI VINTO!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // The Activity layout
    ViewGroup activity_layout;

    // The size for the CardSet based on the difficulty choosed
    int size;

    CardSet cardSet;

    // Get the event touch from ListDialog
    // Called when a user select the game difficulty
    //TO-DO start a Thread
    @Override
    public void onDifficultySelected(int difficulty) {
        size = getSize(difficulty);
        cardSet = new CardSet(this, size);

        activity_layout = (ViewGroup) findViewById(R.id.game_activity_container);
        startGame(size);
    }

    // Init the game
    private void startGame(int size) {
        for (int i=0; i<size; i++){

            cardSet.get(i).setCardPosition(i);
            cardSet.get(i).setId(i);

            setComponentListener(cardSet.get(i));
            cardSet.get(i).setText(button_label);
            activity_layout.addView(cardSet.get(i));
        }
    }

    // Set the listener to the object passed
    private void setComponentListener(Button button) {
        button.setOnClickListener(this);
    }

    // Get the size of the map for the choosen difficulty
    private int getSize(int difficulty) {
        if (difficulty==1){
            return 4;
        } else if (difficulty==2){
            return 6;
        } else if (difficulty==3){
            return 8;
        } else if (difficulty==4){
            return 10;
        } else {
            return 12;
        }
    }
}