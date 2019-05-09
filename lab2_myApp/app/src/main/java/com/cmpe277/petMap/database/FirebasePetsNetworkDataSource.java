package com.cmpe277.petMap.database;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cmpe277.petMap.FriendlyMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebasePetsNetworkDataSource {

    private static final String TAG = "InApp";
    MutableLiveData<petEntity[]> mPets;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPetsDatabaseReference;
    private ChildEventListener mChildEventListener;
    List<petEntity> fetchedPets;

    public FirebasePetsNetworkDataSource() {
        mPets = new MutableLiveData<petEntity[]>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPetsDatabaseReference = mFirebaseDatabase.getReference().child("pets");
        fetchedPets = new ArrayList<petEntity>();
    }

    public MutableLiveData<petEntity[]> getPets() {
        Log.d(TAG,"get pets in firebase called");
        attachDatabaseForSingleRead();
        petEntity[] pets = new petEntity[fetchedPets.size()];
        for(int i=0;i<fetchedPets.size();i++){
            pets[i]=fetchedPets.get(i);
            Log.d(TAG,"adding pets");
        }
        mPets.postValue(pets);
        //detachDatabaseReadListener();
        return mPets;
    }
    private void attachDatabaseReadListener(){
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    petEntity petEntity = dataSnapshot.getValue(petEntity.class);
                    Log.d(TAG,petEntity.toString());
                    fetchedPets.add(petEntity);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mPetsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
    private void detachDatabaseReadListener(){
        if(mChildEventListener!=null){
            mPetsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
    private void attachDatabaseForSingleRead(){
        mPetsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ...
                mPets.postValue(toPetEnttity(dataSnapshot));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
    private petEntity[] toPetEnttity(DataSnapshot dataSnapshot){
        petEntity[] pets = new petEntity[(int)dataSnapshot.getChildrenCount()];
        int i=0;
        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

            petEntity petEntity = new petEntity();
            petEntity.setName((String)messageSnapshot.child("name").getValue(String.class));
            Log.d(TAG,petEntity.getName());
            petEntity.setAge((String)messageSnapshot.child("age").getValue(String.class));
            Log.d(TAG,petEntity.getAge());
            petEntity.setBreed((String)messageSnapshot.child("breed").getValue(String.class));
            Log.d(TAG,petEntity.getBreed());
            petEntity.setEmail((String)messageSnapshot.child("email").getValue(String.class));
            Log.d(TAG,petEntity.getEmail());
            petEntity.setGender((String)messageSnapshot.child("gender").getValue(String.class));
            Log.d(TAG,petEntity.getGender());
            if(messageSnapshot.child("image").getValue()!=null){
                petEntity.setImage((String)messageSnapshot.child("image").getValue(String.class));
            }
            pets[i]= petEntity;
            i++;
            Log.d(TAG,petEntity.toString());
        }
        return pets;
    }
}
