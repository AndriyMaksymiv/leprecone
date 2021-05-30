package com.project_ikni.leprecone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();
    }
    Bitmap bitmap;
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")){
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String imageName = getIntent().getStringExtra("image_name");
            String imageAbout = getIntent().getStringExtra("image_about");
            if(getIntent().hasExtra("image_url")) {

               bitmap = BitmapFactory.decodeByteArray(
                        getIntent().getByteArrayExtra("image_url"), 0, getIntent().getByteArrayExtra("image_url").length);

            }

            setImage(bitmap, imageName, imageAbout);
        }
    }


    private void setImage(Bitmap imageUrl, String imageName, String imageAbout){
        Log.d(TAG, "setImage: setting te image and name to widgets.");

        TextView name = findViewById(R.id.image_description);
        TextView about = findViewById(R.id.image_about);
        name.setText(imageName);
        about.setText(imageAbout);

        ImageView image = findViewById(R.id.image);
//        image.setImageBitmap(imageUrl);

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }

}
