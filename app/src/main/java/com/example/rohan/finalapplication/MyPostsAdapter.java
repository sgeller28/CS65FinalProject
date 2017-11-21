package com.example.rohan.finalapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by rohan on 11/18/17.
 */

public class MyPostsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    private final ArrayList<String> pics;
    private final ArrayList<String> descriptions;
    private final ArrayList<String> prices;
    private final ArrayList<String> contacts;

    public MyPostsAdapter(Activity context, ArrayList<String> names, ArrayList<String> pics, ArrayList<String> descriptions, ArrayList<String> prices, ArrayList<String> contacts) {
        super(context, R.layout.server_post_scroll, names);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.names = names;
        this.pics = pics;
        this.descriptions = descriptions;
        this.prices = prices;
        this.contacts = contacts;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        // create a custom rowView
        final View rowView=inflater.inflate(R.layout.server_post_scroll, null,true);

        TextView name = rowView.findViewById(R.id.name);
        TextView description = rowView.findViewById(R.id.description);
        TextView price = rowView.findViewById(R.id.price);
        TextView contact = rowView.findViewById(R.id.email);
        ImageView pic = rowView.findViewById(R.id.pic);

        // load info
        name.setText(names.get(position));
        description.setText(descriptions.get(position));
        price.setText(prices.get(position));
        contact.setText(contacts.get(position));
        // load picture
        Bitmap picBitmap = decodeImageString(pics.get(position));
        pic.setImageBitmap(picBitmap);
        Button contactButton = rowView.findViewById(R.id.contact);
        contactButton.setText("Delete");
        contactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView Temail = rowView.findViewById(R.id.email);
                String email = Temail.getText().toString();
                TextView Tname = rowView.findViewById(R.id.name);
                String name = Tname.getText().toString();
                email = email.replace(".", "");
                email = email.replace("#", "");
                email = email.replace("$", "");
                email = email.replace("[", "");
                email = email.replace("]", "");
                name = name.replace(".", "");
                name = name.replace("#", "");
                name = name.replace("$", "");
                name = name.replace("[", "");
                name = name.replace("]", "");
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().getRoot().child(email + ": " + name);
                dbRef.setValue(null);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }

    public Bitmap decodeImageString(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
