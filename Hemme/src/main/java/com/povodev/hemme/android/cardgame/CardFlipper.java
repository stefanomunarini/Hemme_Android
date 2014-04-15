package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.povodev.hemme.android.R;

/**
 * Created by Stefano on 15/04/14.
 */
public class CardFlipper extends Handler {

    /*
     * The label for the front side of the Card
     * TODO change it
     */
    private final String button_label = "Click me!";

    /*
     * Card flip timing
     * 1000 millisec (1sec)
     */
    private int cardFlipTiming = 1000;


    protected void startRunner(final Context context, final Card first, final Card second){

        final Drawable image = context.getResources().getDrawable(R.drawable.flipme);

        postDelayed(new Runnable() {
            public void run() {

                first.setImageDrawable(image);
                second.setImageDrawable(image);

                /*first.setText(button_label);
                second.setText(button_label);*/
            }
        }, cardFlipTiming);
    }
}
