package com.cmpe277.petMap.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.cmpe277.petMap.database.AppRepository;
import com.cmpe277.petMap.database.petEntity;

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

    public void addPetsByType(String type) {
        mRepository.addPetsOfType(type);
    }
}
