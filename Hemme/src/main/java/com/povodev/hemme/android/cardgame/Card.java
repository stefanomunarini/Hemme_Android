package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Stefano on 09/04/14.
 */
public class Card extends Button {

    public Card(Context context) {
        super(context);
    }

    // The id corresponding to the position in the ArrayList
    private int cardPosition;

    // The card value
    private int cardValue;

    public int getCardPosition() {
        return cardPosition;
    }

    public void setCardPosition(int cardPosition) {
        this.cardPosition = cardPosition;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}
