package com.cmpe277.petMap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpe277.petMap.database.petEntity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;

public class EditorActivity extends AppCompatActivity {


    private static final String TAG = "Editor TAG";
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 1;
    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    private EditText mDescription;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    private Spinner mSizeSpinner;

    private Button mSubmitButton;

    private com.cmpe277.petMap.database.petEntity petEntity;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private String email = "";

    private Button mPhotoPickerButton;
    private static final int RC_PHOTO_PICKER =  2;


    //private int mGender = PetEntry.GENDER_UNKNOWN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mDescription = findViewById(R.id.edit_pet_desc);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mSizeSpinner = findViewById(R.id.spinner_size);
        mSubmitButton = findViewById(R.id.button2);
        mPhotoPickerButton = findViewById(R.id.uploadPhoto);
        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("pets");
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if(user!=null){
            email =user.getEmail();
        }

        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        setupSpinner();

        petEntity = new petEntity();

        //FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"submit button clicked");
                insertPet();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //user is signed in
                    onSignedInInitialize(user.getDisplayName(),user.getEmail());
                    //Toast.makeText(MessengerActivity.this,"You are now signed in. Welcome to PetMap forum",Toast.LENGTH_SHORT).show();
                }else{
                    //user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    private void onSignedInInitialize(String displayName,String currentemail) {
        Toast.makeText(EditorActivity.this,"Signed in! "+displayName,Toast.LENGTH_SHORT).show();
        mUsername = displayName;
        email = currentemail;
        //attachDatabaseReadListener();
    }
    private void onSignedOutCleanup(){
        mUsername =  ANONYMOUS;
        email="";
        //mMessageAdapter.clear();

    }
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        ArrayAdapter sizeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_size_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        sizeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mSizeSpinner.setAdapter(sizeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        //do something
                        petEntity.setGender("Male");
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        //domGender = PetEntry.GENDER_FEMALE;
                        petEntity.setGender("Female");
                    } else {
                        //mGender = PetEntry.GENDER_UNKNOWN;
                        petEntity.setGender("Unknown");
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mGender = PetEntry.GENDER_UNKNOWN;arrays.xml
                petEntity.setGender("Unknown");
            }
        });

        mSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Small")) {
                        //do something
                        petEntity.setSize("Small");
                    } else if (selection.equals("Medium")) {
                        //domGender = PetEntry.GENDER_FEMALE;
                        petEntity.setSize("Medium");
                    } else {
                        //mGender = PetEntry.GENDER_UNKNOWN;
                        petEntity.setSize("Large");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                petEntity.setSize("Small");
            }
        });
    }
    private void insertPet() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        String desc = mDescription.getText().toString().trim();
        //int weight = Integer.parseInt(weightString);

        if(nameString.equals("") || breedString.equals("") || weightString.equals("")){
            Toast.makeText(this,"Fill out all the fields",Toast.LENGTH_SHORT).show();
            return;
        }

        petEntity.setName(nameString);
        petEntity.setBreed(breedString);
        petEntity.setAge(weightString);
        petEntity.setEmail(email);
        petEntity.setDescription(desc);

        mMessagesDatabaseReference.push().setValue(petEntity);
        // Create database helper
        //PetDbHelper mDbHelper = new PetDbHelper(this);

        // Gets the database in write mode
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        /*
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetEntry.COLUMN_PET_BREED, breedString);
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);
        */

        // Insert a new row for pet in the database, returning the ID of that new row.
        //long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        /*
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Pet saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                //sign out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //detachDatabaseReadListener();
        //mMessageAdapter.clear();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(EditorActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(EditorActivity.this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        petEntity.setImage(downloadUri.toString());
                        Log.d(TAG,"upload successful");
                        Log.d(TAG,downloadUri.toString());
                        Log.d(TAG,petEntity.getImage());
                        Log.d(TAG,"Something is wrong");
                        //FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUri.toString());
                        //mMessagesDatabaseReference.push().setValue(friendlyMessage);
                    } else {
                        Toast.makeText(EditorActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
