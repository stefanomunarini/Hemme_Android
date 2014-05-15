package com.povodev.hemme.android.utils;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Stefano on 15/05/14.
 */
public class VibratingToast extends Toast {

    public VibratingToast(Context context, CharSequence text, int duration) {
        super(context);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        super.makeText(context, text, duration).show();
    }
}
