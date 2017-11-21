package com.example.rohan.finalapplication;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class InvalidDialog extends DialogFragment {

    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.invalid_dialog, null);

        // set up AlertDialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Invalid username or password. Try again.")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing, end the dialog
                    }

                })
                .create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
