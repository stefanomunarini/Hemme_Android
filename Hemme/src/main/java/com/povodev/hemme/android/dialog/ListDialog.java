package com.povodev.hemme.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by Stefano on 08/04/14.
 */
public class ListDialog extends DialogFragment {

    private String title;

    private String[] list;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle argsBundle = getArguments();
        title = argsBundle.getString("title");
        list = argsBundle.getStringArray("list");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int position = which + 1;

                        // The 'which' argument contains the index position
                        // of the selected item (starting from 0. So we add 1 to get the real
                        // position.

                    }
                });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("title", title);
        savedInstanceState.putStringArray("list",list);

        super.onSaveInstanceState(savedInstanceState);
    }
}
