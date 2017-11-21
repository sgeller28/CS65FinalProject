package com.example.rohan.finalapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPostsActivity extends AppCompatActivity {

    ListView theList;

    MyPostsAdapter adapter;

    ArrayList<ServerPost> postArrayList;
    ArrayList<String> names;
    ArrayList<String> images;
    ArrayList<String> descriptions;
    ArrayList<String> prices;
    ArrayList<String> emails;

    String contact;

    DatabaseReference ref;
    FirebaseDatabase database;
    ValueEventListener eventListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_scroll);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        contact = user.getEmail();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        postArrayList = new ArrayList<ServerPost>();

        load();

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ref.removeEventListener(eventListener);
                load();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void load() {
        Log.d("LOAD CALLED", "CALLED");
        postArrayList = new ArrayList<ServerPost>();

        names = new ArrayList<>();
        images = new ArrayList<>();
        descriptions = new ArrayList<>();
        prices = new ArrayList<>();
        emails = new ArrayList<>();

        // load all info from server

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter = new MyPostsAdapter(MyPostsActivity.this, names, images, descriptions, prices, emails);
                theList=(ListView) findViewById(R.id.scrollList);
                theList.setAdapter(null);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = (String) childSnapshot.child("name").getValue();
                    String image = (String) childSnapshot.child("image").getValue();
                    String description = (String) childSnapshot.child("description").getValue();
                    String price = (String) childSnapshot.child("price").getValue();
                    String email = (String) childSnapshot.child("email").getValue();
                    if (email.equals(contact)) {
                        names.add(name);
                        images.add(image);
                        descriptions.add(description);
                        prices.add(price);
                        emails.add(email);
                        //Log.d("post", childSnapshot.toString());
                        //Log.d("post email", email);
                    }
                }
                adapter = new MyPostsAdapter(MyPostsActivity.this, names, images, descriptions, prices, emails);
                theList=(ListView) findViewById(R.id.scrollList);
                theList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(eventListener);

        //Log.d("names list", String.valueOf(names.size()));

    }

}
