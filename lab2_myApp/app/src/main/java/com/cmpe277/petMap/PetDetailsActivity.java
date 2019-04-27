package com.cmpe277.petMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class PetDetailsActivity extends AppCompatActivity {

    private TextView petName;
    private ImageView petImage;
    private TextView petAge;
    private TextView petGender;
    private TextView petSize;
    private TextView email;
    private TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        petName = findViewById(R.id.petName);
        petImage = findViewById(R.id.petImage);
        petAge = findViewById(R.id.age);
        petGender = findViewById(R.id.gender);
        petSize = findViewById(R.id.size);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        Intent intent = getIntent();

        petName.setText(intent.getStringExtra("name"));
        petAge.setText(intent.getStringExtra("age"));
        petGender.setText(intent.getStringExtra("gender"));
        petSize.setText(intent.getStringExtra("size"));
        email.setText(intent.getStringExtra("email"));
        phone.setText(intent.getStringExtra("phone"));
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(petImage);
        downloadTask.execute(intent.getStringExtra("image"));

    }

    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
