package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.os.Handler;

import com.povodev.hemme.android.activity.NewGame_Activity;

/**
 * Created by Stefano on 15/04/14.
 */
public class GameTimer extends Handler {

    private int timer = 0;
    private int increment = 500;
    private Context context;

    public GameTimer(Context context){
        this.context =  context;
        startRunner();
        ((NewGame_Activity)context).setTvText(timer+"");
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            postDelayed(this,1000);

            timer += increment;
            ((NewGame_Activity)context).setTvText(timer/1000+"");
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
