package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.povodev.hemme.android.activity.Game_Activity;
import com.povodev.hemme.android.utils.NumberUtils;

import java.util.ArrayList;

/**
 * Created by Stefano on 09/04/14.
 */
public class CardSet extends ArrayList<Card> {

    public CardSet(Context context, int size){
        super(size);
        for (int i=0;i<size;i++){
            Card card = new Card(context);
            this.add(i, card);
        }
        populateThis(size);
    }

    /*
     * Check if two cards have te same value
     */
    private boolean checkForPairs (Card first, Card second){
        if (first.getCardValue()==second.getCardValue()){
            Log.d(Game_Activity.TAG,"First card value: " + first.getCardValue() + "  Second card value: " + second.getCardValue());
            return true;
        }
        return false;
    }

    /*
     * Delete the two cards from the ArrayList if they pairs together
     * @See checkForPairs to see when cards get removed
     */
    public boolean deletePairs(Card first, Card second){
        if (checkForPairs(first,second)){
            remove(getArrayPosition(first.getId()));
            remove(getArrayPosition(second.getId()));
            first.setVisibility(View.GONE);
            second.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    public int getArrayPosition(int cardId){
        for (int i=0; i<this.size(); i++){
            if (get(i).getCardPosition()==cardId){
                return i;
            }
        }
        return -1;
    }

    // pseudo random array with cards values
    ArrayList <Integer> values;

    // Populate the array used for card values
    private void populateValuesArray(int size) {
        values = new ArrayList<Integer>(size);
        for (int i=1; i<=size/2; i++){
            values.add(i);
            values.add(i);
        }
    }

    private void populateThis(int size){
        populateValuesArray(size);
        for (int i=0; i<size; i++){ 
            // Min position from where to take the value
            int min = 0;
            // Max position from where to take the value
            int max = size - i - 1;
            // remove a random value from the values array
            int value = values.remove(NumberUtils.randInt(min, max));

            Log.d(Game_Activity.TAG,"Position [" + i + "] : " + value);

            get(i).setCardValue(value);
        }
    }
}
