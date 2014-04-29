package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.os.Handler;

import com.povodev.hemme.android.activity.NewGame_Activity;

/**
 * The timer used for the game
 * Created by Stefano on 15/04/14.
 */
public class GameTimer extends Handler {

    /*
     * Timer starting value
     */
    private int timer = 0;
    /*
     * Timer increment
     */
    private final int increment = 500;
    /*
     * The delay time used for the runnable
     * 1000 millisec (1sec)
     */
    private final int delay = 1000;
    private Context context;

    public GameTimer(Context context){
        this.context =  context;
        startRunner();
        ((NewGame_Activity)context).setTvText(Integer.toString(timer));
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            postDelayed(this,delay);

            timer += increment;
            ((NewGame_Activity)context).setTvText(Integer.toString(timer/1000));
        }
    };

    protected void startRunner(){
        runnable.run();
    }

    protected void stopRunner(){
        removeCallbacks(runnable);
    }

    protected int getTiming(){
        return timer;
    }
}
