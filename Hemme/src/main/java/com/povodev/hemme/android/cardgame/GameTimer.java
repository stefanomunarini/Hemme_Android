package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.os.Handler;

import com.povodev.hemme.android.activity.Game_Activity;

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
        ((Game_Activity)context).setTvText(timer+"");
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            postDelayed(this,1000);

            timer += increment;
            ((Game_Activity)context).setTvText(timer/1000+"");
        }
    };

    public void startRunner(){
        runnable.run();
    }

    public void stopRunner(){
        removeCallbacks(runnable);
    }

    public int getTiming(){
        return timer;
    }
}
