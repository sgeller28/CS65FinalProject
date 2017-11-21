package com.example.rohan.finalapplication;

import android.Manifest;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;

    private Intent newAccount;
    RequestQueue queue;
    private FirebaseAuth mAuth;
    String emailEntered = "";
    String passwordEntered = "";
    ImageView theImage;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // update profile info UI
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        checkPermissions();

        theImage = findViewById(R.id.photo);
        theImage.setImageResource(R.drawable.dbuy);

        mEmail = findViewById(R.id.character);
        mPassword = findViewById(R.id.password);
        final Button clearButton = findViewById(R.id.btnClear);
        clearButton.setEnabled(true);
        queue = Volley.newRequestQueue(this);
        mAuth = FirebaseAuth.getInstance();

        checkPasswordFocus();

    }
    protected void clearClick(View view) {
        mEmail.setText(null);
        mPassword.setText(null);
    }

    protected void onSignInClick(View view) {
        emailEntered = mEmail.getText().toString();
        passwordEntered = mPassword.getText().toString();

        if (emailEntered.contains("dartmouth.edu")) {
            signIn(emailEntered, passwordEntered);
            SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", emailEntered);
            editor.putString("password", passwordEntered);
            editor.commit();
        } else {
            Toast.makeText(getApplicationContext(), "Email must end in dartmouth.edu", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onAccountClick(View view) {
        newAccount = new Intent(this, SignUpActivity.class);
        startActivity(newAccount);
    }


    /**
     * Code to check for runtime permissions.
     */
    public void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 301);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 301);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        }else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)||shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important for the app.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                            }

                        }
                    });
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
                }else{
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }

    public void signIn(String emailEntered, String passwordEntered) {
        mAuth.signInWithEmailAndPassword(emailEntered, passwordEntered)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(startMain);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Sign In Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
        // update info
    }

    private void checkPasswordFocus() {
        // checks password focus and launches activity to confirm password
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordEntered.length() > 0) {
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
