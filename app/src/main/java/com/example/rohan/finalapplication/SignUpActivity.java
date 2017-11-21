// Shoshana Geller and Rohan Bose, CS65
// Credit to Varun for some of this code

package com.example.rohan.finalapplication;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.app.DialogFragment;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class SignUpActivity extends AppCompatActivity {

    // class variables
    private EditText mEmail;
    private EditText mPassword;

    public String thePassword;
    public String theEmail;
    private FirebaseAuth mAuth;

    ImageView theImage;


    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        theImage = findViewById(R.id.photo);
        theImage.setImageResource(R.drawable.dbuy);

        // associate EditText with layout
        mEmail = findViewById(R.id.character);
        mPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();


        // access Top button
        final Button theTop = findViewById(R.id.topButton);
        theTop.setEnabled(true);

        // Sets the clear button
        if (((mPassword.getText().length()) > 0)  || ((mEmail.getText().length()) > 0)) {
            // sets top button text to clear if text entered
            theTop.setText("Clear");

        } else {
            theTop.setText("I Have an Account");
        }

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((mPassword.getText().length()) > 0)  || ((mEmail.getText().length()) > 0)) {
                    // sets top button text to clear if text entered
                    theTop.setText("Clear");

                } else {
                    theTop.setText("I Have an Account");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if (((mPassword.getText().length()) > 0)  || ((mEmail.getText().length()) > 0)) {
                     // sets top button text to clear if text entered
                     theTop.setText("Clear");

                 } else {
                     theTop.setText("I Have an Account");
                 }
             }

             @Override
             public void afterTextChanged(Editable editable) {
                 // save the password to a string
                 thePassword = mPassword.getText().toString();
             }
         });
        mEmail.setText(null);
        mPassword.setText(null);
        checkPasswordFocus();

    }



    public void uploadInfoToServer(String emailEntered, String passwordEntered) {
        mAuth.createUserWithEmailAndPassword(emailEntered, passwordEntered)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "UPLOADED", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("email", theEmail);
                            editor.putString("password", thePassword);
                            editor.commit();
                            // starts the sliding tab activity
                            Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(startMain);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Sign Up failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSaveClick(View view) {
        // get current views
        mEmail = findViewById(R.id.character);
        mPassword = findViewById(R.id.password);

        if ((mEmail.getText().length() > 0) & (mEmail.getText().toString().contains("dartmouth.edu")) & (mPassword.getText().length() > 0)) {
            // store strings in key-value pairings in Shared Preferences
            theEmail = mEmail.getText().toString();
            thePassword = mPassword.getText().toString();
            uploadInfoToServer(theEmail, thePassword);

            // starts the sliding tab activity
            // Intent startMain = new Intent(this, MainActivity.class);
            // startActivity(startMain);

        }
        else if (!(mEmail.getText().toString().contains("dartmouth.edu"))) {
            Toast.makeText(SignUpActivity.this, "Must use a dartmouth.edu email address",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // start dialog informing user of invalid arguments
            DialogFragment saveDialog = new SaveDialog();
            android.app.FragmentManager manager2 = getFragmentManager();
            saveDialog.show(manager2, "Save");
        }


    }



    public void onClearClick(View view) {
        // clear all text boxes when clear button clicked
        if (mEmail.length() > 0 || mPassword.length() > 0) {
            // when button says clear
            mEmail.setText(null);
            mPassword.setText(null);

        } else {
            // when button says I already have an account, starts sign in page
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }




    /**
     * Returns the text (character name, full name and password) from shared preferences
     */
    private void returnFromSharedPreferences() {
        //check if data previously stored and restore it if possible

        SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        Log.d("sharedPrefObject", sharedPref.toString());
        String savedPassword = sharedPref.getString("password", "");
        if (savedPassword.length() > 0) { // if password exists
            mPassword.setText(savedPassword);
        }

        String savedCharacter = sharedPref.getString("email", "");
        if (savedCharacter.length() > 0) { // if character exists
            mEmail.setText(savedCharacter);
        }

    }

    /**
     * Checks the Password Focus to determine when to launch the confirm password dialog box
     */
    private void checkPasswordFocus() {
        // checks password focus and launches activity to confirm password
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (thePassword.length() > 0) {
                        // Launch the password confirmation dialog
                        DialogFragment passAuthDialog = new PassAuthDialog();
                        android.app.FragmentManager manager = getFragmentManager();
                        passAuthDialog.show(manager, "PassAuth");
                    }
                }
            }
        });
    }


}
