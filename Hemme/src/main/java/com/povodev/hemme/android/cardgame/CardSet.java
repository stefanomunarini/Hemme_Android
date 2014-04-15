package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.povodev.hemme.android.utils.NumberUtils;

import java.util.ArrayList;

/**
 * Created by Stefano on 09/04/14.
 */
public class CardSet extends ArrayList<Card> {

    private static final String TAG = "Game_Activity";

    public CardSet(Context context, int size){
        super(size);
        for (int i=0;i<size;i++){
            Card card = new Card(context);
            this.add(i, card);
        }
        populateThis(size);
    }

    /*
     * Insert random (values from 1 to size/2) values
     * into this ArrayList<Card>
     */
    private void populateThis(int size){
        populateValuesArray(size);
        for (int i=0; i<size; i++){
            // Min position from where to take the value
            int min = 0;
            // Max position from where to take the value
            int max = size - i - 1;
            // remove a random value from the values array
            int value = values.remove(NumberUtils.randInt(min, max));

            Log.d(TAG,"Position [" + i + "] : " + value);

            get(i).setCardValue(value);
        }
    }

    // pseudo random array used for cards values
    ArrayList <Integer> values;

    // Populate the array used for card values
    private void populateValuesArray(int size) {
        values = new ArrayList<Integer>(size);
        for (int i=1; i<=size/2; i++){
            values.add(i);
            values.add(i);
        }
    }

    /*
     * Check if two cards have te same value
     */
    private boolean checkForPairs (Card first, Card second){
        if (first.getCardValue()==second.getCardValue()){
            Log.d(TAG,"First card value: " + first.getCardValue() + "  Second card value: " + second.getCardValue());
            return true;
        }
        return false;
    }

    private int pairsCounter = 0;
    /*
     * Delete the two cards from the ArrayList (after have
     * checked via {@See checkForPairs} if their values are equal}
     * @See checkForPairs to see when cards get removed
     */
    public boolean deletePairs(Card first, Card second){
        if (checkForPairs(first,second)){
            //remove(getArrayPosition(first.getId()));
            //remove(getArrayPosition(second.getId()));
            pairsCounter ++;
            pairsCounter ++;
            first.setVisibility(View.INVISIBLE);
            second.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    /*
     * Get the array position
     * @param cardId
     * @return the position where that card is
     */
    public int getArrayPosition(int cardId){
        for (int i=0; i<this.size(); i++){
            if (get(i).getCardPosition()==cardId){
                return i;
            }
        }
        return -1;
    }

    /*
     * Return true if the game is won
     */
    public boolean winnerWinnerChickenDinner(){
        if (pairsCounter==size()){
            return true;
        }
        return false;
    }
}
