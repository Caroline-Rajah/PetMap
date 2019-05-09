package com.cmpe277.petMap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpe277.petMap.database.petEntity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class AddActivity extends AppCompatActivity {

    static int REQUEST_CAMERA_CAPTURE = 1;
    private Button btnAddImage;
    private Button btnSave;
    private ImageView petImage;
    private EditText txtAddName;
    private EditText txtAddBreed;
    private EditText txtAddSize;
    private EditText txtAddDescription;
    private Spinner spnAge;
    private Spinner spnSpecies;
    private Spinner spnGender;

    private Bitmap bp;

    private LatLng currentLocation = new LatLng(37.3382, -121.8863);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        petImage = (ImageView) findViewById(R.id.addImage);
        petImage.setVisibility(View.GONE);

        btnSave = (Button) findViewById(R.id.btnSave);
        txtAddName = (EditText) findViewById(R.id.txtAddName);
        txtAddBreed = (EditText) findViewById(R.id.txtAddBreed);
        txtAddSize = (EditText) findViewById(R.id.txtAddSize);
        txtAddDescription = (EditText) findViewById(R.id.txtAddDescription);

        spnAge = (Spinner) findViewById(R.id.spnAge);
        spnSpecies = (Spinner) findViewById(R.id.spnSpecies);
        spnGender = (Spinner) findViewById(R.id.spnGender);

        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA_CAPTURE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petEntity pet = new petEntity();
                pet.setAddress("37.3382, -121.8863");
                pet.setName(txtAddName.getText().toString());
                pet.setSize(txtAddSize.getText().toString());
                pet.setBreed(txtAddBreed.getText().toString());
                pet.setDescription(txtAddDescription.getText().toString());
                pet.setAge(spnAge.getSelectedItem().toString());
                pet.setGender(spnGender.getSelectedItem().toString());

                Intent intent = new Intent(AddActivity.this, PetDetailsActivity.class);
                intent.putExtra("name", pet.getName());

                intent.putExtra("imageBitMap", bp);
                intent.putExtra("gender",pet.getGender());
                intent.putExtra("age",pet.getAge());
                intent.putExtra("size",pet.getSize());
                intent.putExtra("email","dhruvil@gmail.com");
                intent.putExtra("phone","444-444-4444");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                btnAddImage.setVisibility(View.GONE);
                petImage.setVisibility(View.VISIBLE);
                bp = (Bitmap) data.getExtras().get("data");
                petImage.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

    }
}
