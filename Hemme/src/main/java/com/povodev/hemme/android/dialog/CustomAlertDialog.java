package com.povodev.hemme.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Stefano on 07/05/14.
 */
public class CustomAlertDialog extends AlertDialog {

    protected CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(final Context context, String message, String buttonText, int buttonAction){
        super(context);

        setMessage(message);

        setButton(BUTTON_NEUTRAL,buttonText,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
    }

    public CustomAlertDialog(Context context, String message, String title, String buttonText, int buttonAction){
        super(context);

        setMessage(message);
        setTitle(title);
        setButton(BUTTON_NEUTRAL,buttonText,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
    }
}
