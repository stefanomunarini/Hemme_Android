package com.povodev.hemme.android.cardgame;

import android.os.Handler;

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
     * 100 millisec (1sec)
     */
    private int cardFlipTiming = 1000;


    protected void startRunner(final Card first, final Card second){

        postDelayed(new Runnable() {
            public void run() {
                first.setText(button_label);
                second.setText(button_label);
            }
        }, cardFlipTiming);
    }
}
