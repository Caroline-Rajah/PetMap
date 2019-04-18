package com.caroline.lab2_myapp.viewmodel;

        import android.support.annotation.NonNull;

        import com.caroline.lab2_myapp.database.petEntity;

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
