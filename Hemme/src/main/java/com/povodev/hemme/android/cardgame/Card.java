package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by Stefano on 09/04/14.
 */
public class Card extends ImageView {

    protected Card(Context context) {
        super(context);

        /*ViewGroup.LayoutParams params = new ActionBar.LayoutParams(
                150,
                300
        );


        this.setLayoutParams(params);
        this.setScaleType(ImageView.ScaleType.FIT_START);
*/
    }

    // The id corresponding to the position in the ArrayList
    private int cardPosition;

    // The card value
    private int cardValue;

    protected int getCardPosition() {
        return cardPosition;
    }

    protected void setCardPosition(int cardPosition) {
        this.cardPosition = cardPosition;
    }

    protected int getCardValue() {
        return cardValue;
    }

    protected void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}
