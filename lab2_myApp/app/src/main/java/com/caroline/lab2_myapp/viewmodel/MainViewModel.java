package com.caroline.lab2_myapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.caroline.lab2_myapp.database.AppRepository;
import com.caroline.lab2_myapp.database.petEntity;
import com.caroline.lab2_myapp.util.SampleData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<petEntity>> mPets;
    private AppRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mPets = mRepository.mPets;
    }

    public void addSamplePets() {
        mRepository.addSamplePets();
    }

    public void deleteAllData() {
        mRepository.deleteAllPets();
    }
}
