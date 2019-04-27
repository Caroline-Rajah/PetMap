package com.cmpe277.petMap.viewmodel;

import android.support.annotation.NonNull;

import com.cmpe277.petMap.database.petEntity;

public class PetFinderResponse {
    @NonNull
    private final petEntity[] mPets;

    public PetFinderResponse(petEntity[] petEntities) {
        mPets = petEntities;
    }

    public petEntity[] getWeatherForecast() {
        return mPets;
    }
}
