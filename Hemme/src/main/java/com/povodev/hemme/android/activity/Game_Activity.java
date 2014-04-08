package com.povodev.hemme.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.dialog.ListDialog;
import com.povodev.hemme.android.utils.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;

/**
 * Created by Stefano on 08/04/14.
 */
public class Game_Activity extends RoboActivity implements View.OnClickListener, ListDialog.OnDifficultySelectedListener {

    private final String TAG = "Game_Activity";
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

    // The previous card id
    int previousCardId;

    @Override
    public void onClick(View view) {
        counter++;

        // The current card (button) id
        int currentCardid = view.getId();

        switch (currentCardid){
        }

        Toast toast;
        if (counter%2==0){
            if (cards.get(currentCardid) == cards.get(previousCardId)){
                toast =  Toast.makeText(context,"Corretto! " +  cards.get(currentCardid)+"-"+cards.get(previousCardId),Toast.LENGTH_SHORT);
            } else {
                toast =  Toast.makeText(context,"Ritenta! " + cards.get(currentCardid)+" "+cards.get(previousCardId),Toast.LENGTH_SHORT);
            }
            toast.show();
        } else {
            previousCardId = currentCardid;
        }
    }

    // Used to combine cards (button) ids with their values in the game
    HashMap<Integer,Integer> cards;

    // The Activity layout
    ViewGroup activity_layout;

    // The size for the HashMap based on the difficulty choosed
    int size;

    // Get the event touch from ListDialog
    // Called when a user select the game difficulty
    //TO-DO start a Thread
    @Override
    public void onDifficultySelected(int difficulty) {
        size = getSize(difficulty);
        cards = new HashMap(size);
        populateValuesArray(size);
        activity_layout = (ViewGroup) findViewById(R.id.game_activity_container);
        startGame(size,cards);
    }

    // Populate the array used for card values
    private void populateValuesArray(int size) {
        values = new ArrayList<Integer>(size);
        for (int i=0; i<size/2; i++){
            values.add(i);
            values.add(i);
        }
    }

    // pseudo random array with cards values
    ArrayList <Integer> values;

    // Init the game
    private void startGame(int size, HashMap<Integer, Integer> cards) {
        for (int i=0; i<size; i++){

            // Min position from where to take the value
            int min = 0;
            // Max position from where to take the value
            int max = size - i - 1;
            // remove a random value from the values array
            int value = values.remove(NumberUtils.randInt(min, max));

            // add the random value just removed from the array and add it to the HashMap
            cards.put(i,value);

            Button button = new Button(this);
            button.setId(i);
            setComponentListener(button);
            button.setText("Click me");
            activity_layout.addView(button);
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
        } else {
            return 8;
        }
    }
}