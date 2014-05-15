package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.povodev.hemme.android.R;

/**
 * The handler used for card flipping
 * Created by Stefano on 15/04/14.
 */
public class CardFlipper extends Handler {

    /*
     * Card flip timing
     * 1000 millisec (1sec)
     */
    private int cardFlipTiming = 1000;

    /*
     * Call this method whenever
     * you need to flip cards back after
     * a period of time {@see #cardFlipTiming}
     * if their values does not correspond
     * @param context
     * @param first the first card to flip
     * @param second the second card to flip
     */
    protected void startRunner(final Context context, final Card first, final Card second){

        final Drawable image = context.getResources().getDrawable(R.drawable.card_back);

        postDelayed(new Runnable() {
            public void run() {

                first.setImageDrawable(image);
                second.setImageDrawable(image);

            }
        }, cardFlipTiming);
    }
}
