// Shoshana Geller and Rohan Bose, CS65

package com.example.rohan.finalapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PassAuthDialog extends DialogFragment {

    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // class variables
    private String newPassword;
    EditText mThePassword;
    Button posButton;

    @Override
    public void onStart() {
        super.onStart();
        // initialize AlertDialog
        AlertDialog theDialog = (AlertDialog) getDialog();
        if (theDialog != null) {
            posButton = theDialog.getButton(Dialog.BUTTON_POSITIVE);
            posButton.setEnabled(false);
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog, null);

        // associate EditText with it's input box
        mThePassword = (EditText) view.findViewById(R.id.new_password);
        mThePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newPassword = mThePassword.getText().toString();
                TextView matchText = (TextView) view.findViewById(R.id.matches);
                EditText passwordET = getActivity().findViewById(R.id.password);

                // check in real time if passwords match
                if (newPassword.equals(passwordET.getText().toString())) {
                    matchText.setText("Password matches!");
                    AlertDialog theDialog = (AlertDialog) getDialog();
                    posButton = theDialog.getButton(Dialog.BUTTON_POSITIVE);
                    posButton.setEnabled(true);
                }
                else {
                    matchText.setText("Password does not match");
                    posButton.setEnabled(false);

                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // set up AlertDialog view
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Please re-enter your password")
                // positive button setup

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newPassword = mThePassword.getText().toString();
                        EditText passwordET = getActivity().findViewById(R.id.password);
                        //when password matches
                        if (newPassword.equals(passwordET.getText().toString())) {
                            TextView password2 = view.findViewById(R.id.new_password);
                            newPassword = password2.getText().toString();
                        }
                    }

                })
                // negative button setup
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText passwordBox = getActivity().findViewById(R.id.password);
                        // when the password doesn't match
                        passwordBox.setText(null);
                    }
                })
                .create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
