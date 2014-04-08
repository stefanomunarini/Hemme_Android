package com.povodev.hemme.android.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Stefano on 08/04/14.
 */
public class CustomProgressDialog extends ProgressDialog {

/*
    private String title;

    private String message;
*/


    public CustomProgressDialog(Context context, String title, String message) {
        super(context);

        setMessage(message);
        setTitle(title);
        setCanceledOnTouchOutside(false);
    }

    public CustomProgressDialog(Context context, String message) {
        super(context);

        setMessage(message);
        setCanceledOnTouchOutside(false);
    }


}
