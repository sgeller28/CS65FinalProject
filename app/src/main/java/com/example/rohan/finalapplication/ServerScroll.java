package com.example.rohan.finalapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServerScroll extends AppCompatActivity {

    ListView theList;
    EditText mSearch;
    String search = "";

    ServerPostScroll adapter;

    ArrayList<ServerPost> postArrayList;
    ArrayList<String> names;
    ArrayList<String> images;
    ArrayList<String> descriptions;
    ArrayList<String> prices;
    ArrayList<String> emails;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_scroll);

        mSearch = findViewById(R.id.search);


        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // save the character to a string
                search = mSearch.getText().toString();
                load();
            }
        });
        load();

    }

    protected void onResume() {
        super.onResume();
    }


    public void load() {

        // load all info from server
        postArrayList = new ArrayList<ServerPost>();

        names = new ArrayList<>();
        images = new ArrayList<>();
        descriptions = new ArrayList<>();
        prices = new ArrayList<>();
        emails = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = (String) childSnapshot.child("name").getValue();
                    String image = (String) childSnapshot.child("image").getValue();
                    String description = (String) childSnapshot.child("description").getValue();
                    String price = (String) childSnapshot.child("price").getValue();
                    String email = (String) childSnapshot.child("email").getValue();
                    if (name.contains(search)) {
                        names.add(name);
                        images.add(image);
                        descriptions.add(description);
                        prices.add(price);
                        emails.add(email);
                        //Log.d("post", childSnapshot.toString());
                        //Log.d("post email", email);
                    }
                }
                adapter = new ServerPostScroll(ServerScroll.this, names, images, descriptions, prices, emails);
                theList=(ListView) findViewById(R.id.scrollList);
                theList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(eventListener);

        Log.d("names list", String.valueOf(names.size()));

    }

}

