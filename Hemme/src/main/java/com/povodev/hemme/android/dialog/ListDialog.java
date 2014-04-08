package com.povodev.hemme.android.dialog;

import android.app.Activity;
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
                        startGame(position);
                        // The 'which' argument contains the index position
                        // of the selected item (starting from 0. So we add 1 to get the real
                        // position.

                    }
                });
        return builder.create();
    }

    private void startGame(int difficulty) {
        mCallback.onDifficultySelected(difficulty);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("title", title);
        savedInstanceState.putStringArray("list",list);
        super.onSaveInstanceState(savedInstanceState);
    }

    OnDifficultySelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnDifficultySelectedListener {
        public void onDifficultySelected(int difficulty);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDifficultySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
