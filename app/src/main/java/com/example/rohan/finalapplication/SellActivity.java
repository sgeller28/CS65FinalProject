package com.example.rohan.finalapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rohan on 11/15/17.
 */

public class SellActivity extends AppCompatActivity {

    private Intent openCamera;
    private Uri mImageCaptureUri;
    final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView theImage;
    Bitmap theBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        theImage = findViewById(R.id.photo);
        theImage.setImageResource(R.drawable.cameraicon);
    }

    public void onPostClick(View view) {
        EditText mName = findViewById(R.id.name);
        String name = mName.getText().toString();
        EditText mDescription = findViewById(R.id.description);
        String description = mDescription.getText().toString();
        EditText mPrice = findViewById(R.id.price);
        String price = mPrice.getText().toString();
        // check also if picture is null???
        ImageView thePic = findViewById(R.id.photo);
        boolean hasPhotoBeenTaken;
        if (thePic.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.cameraicon).getConstantState()) {
            hasPhotoBeenTaken = false;
        } else {
            hasPhotoBeenTaken = true;
        }
        if ((mName.length() > 0) & (mDescription.getText().length() > 0) & (hasPhotoBeenTaken) & (mPrice.getText().length() >0)) {
            // post to the server!!
            SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            String emailEntered = sharedPref.getString("email", "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String image = encodeImage(theImage);
            Map<String, String> postInfo = new HashMap<String, String>(); // put together info!!
            postInfo.put("email", emailEntered);
            postInfo.put("name", name);
            emailEntered = emailEntered.replace(".", "");
            emailEntered = emailEntered.replace("#", "");
            emailEntered = emailEntered.replace("$", "");
            emailEntered = emailEntered.replace("[", "");
            emailEntered = emailEntered.replace("]", "");
            name = name.replace(".", "");
            name = name.replace("#", "");
            name = name.replace("$", "");
            name = name.replace("[", "");
            name = name.replace("]", "");
            DatabaseReference myRef = database.getReference(emailEntered + ": " + name);
            postInfo.put("description", description);
            postInfo.put("image", image);
            postInfo.put("price", price);
            myRef.setValue(postInfo);
            Toast.makeText(this, "Successful Post", Toast.LENGTH_SHORT).show();
            Intent startServerScroll = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startServerScroll);
        } else {
            Toast.makeText(SellActivity.this, "Cannot post with a missing field",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onClearClick(View view) {
        EditText name = findViewById(R.id.name);
        EditText description = findViewById(R.id.description);
        ImageView thePic = findViewById(R.id.photo);
        name.setText(null);
        description.setText(null);
        thePic.setImageResource(R.drawable.cameraicon);
    }

    public void onPhotoClick(View view) {
        // send Intent to open camera
        openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        mImageCaptureUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        openCamera.putExtra("return-data", true);

        if (openCamera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(openCamera, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // save the image taken by the camera

        if (resultCode != RESULT_OK)
            return;

        // how to get rid of cropping?

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // Send image taken from camera for cropping
                beginCrop(mImageCaptureUri);
                break;

            case Crop.REQUEST_CROP: //We changed the RequestCode to the one being used by the library.
                // Update image view after image crop
              handleCrop(resultCode, data);
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists())
                    f.delete();

               break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // parce the image
        super.onSaveInstanceState(outState);
        outState.putParcelable("image", mImageCaptureUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        mImageCaptureUri = savedInstanceState.getParcelable("image");
        theImage.setImageURI(mImageCaptureUri);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            theImage.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSnap() {

        // Load profile photo from internal storage
        try {
            FileInputStream fis = openFileInput("profile_photo.png");
            theBitmap = BitmapFactory.decodeStream(fis);
            theImage.setImageBitmap(theBitmap);
            fis.close();
        } catch (IOException e) {
            // Default profile photo if no photo saved before.
        }
    }

    private void saveSnap() {

        // Commit all the changes into preference file
        // Save profile image into internal storage.
        theImage.buildDrawingCache();
        Bitmap bmap = theImage.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    "profile_photo.png", MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String encodeImage(ImageView theImage) {
        theImage.buildDrawingCache();
        Bitmap bmap = theImage.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return encodedImage;
    }
}
