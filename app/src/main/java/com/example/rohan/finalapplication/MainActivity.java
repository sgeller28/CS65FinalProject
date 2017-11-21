package com.example.rohan.finalapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by rohan on 11/15/17.
 */

public class MainActivity extends AppCompatActivity {

    ImageView theImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theImage = findViewById(R.id.photo);
        theImage.setImageResource(R.drawable.dbuy);
    }

    protected void onSellClick (View v) {
        Intent sell = new Intent(this, SellActivity.class);
        startActivity(sell);

    }

    protected void onServerClick(View v) {
        Intent startServerScroll = new Intent(getApplicationContext(), ServerScroll.class);
        startActivity(startServerScroll);
    }

    public void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }

    public void onSignOutClick(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void onMyPostsClick(View v){
        Intent intent = new Intent(this, MyPostsActivity.class);
        startActivity(intent);
    }



}
