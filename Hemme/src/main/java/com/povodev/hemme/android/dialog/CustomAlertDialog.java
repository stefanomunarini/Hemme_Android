package com.povodev.hemme.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by Stefano on 07/05/14.
 */
public class CustomAlertDialog extends AlertDialog {

    protected CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(final Context context, String message, final String title){
        super(context);

        setMessage(message);
        setTitle(title);

        setButton(BUTTON_NEUTRAL,"Ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
    }

    public CustomAlertDialog(final Context context, String message, final String buttonText, final String buttonAction){
        super(context);

        setMessage(message);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setButton(BUTTON_NEUTRAL,buttonText,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(buttonAction));
            }
        });
    }

    public CustomAlertDialog(final Context context, String message, String title, String buttonText, final String buttonAction){
        super(context);

        setMessage(message);
        setTitle(title);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setButton(BUTTON_NEUTRAL,buttonText,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(buttonAction));
            }
        });
    }
}
