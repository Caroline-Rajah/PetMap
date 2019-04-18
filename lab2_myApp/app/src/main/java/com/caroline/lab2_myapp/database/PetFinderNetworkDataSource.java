package com.caroline.lab2_myapp.database;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.caroline.lab2_myapp.AppExecutors;
import com.caroline.lab2_myapp.util.NetworkUtils;
import com.caroline.lab2_myapp.viewmodel.PetFinderResponse;

import java.net.URL;

class PetFinderNetworkDataSource {

    private final AppExecutors mExecutors;
    private final Context mContext;
    private static final String LOG_TAG = PetFinderNetworkDataSource.class.getSimpleName();
    private final MutableLiveData<petEntity[]> mDownloadedPets;
    private static PetFinderNetworkDataSource sInstance;
    private static final Object LOCK = new Object();

    public PetFinderNetworkDataSource(Context context,AppExecutors mExecutors) {
        this.mContext = context;
        this.mExecutors = mExecutors;
        mDownloadedPets = new MutableLiveData<>();
        fetchPets();
    }

    public static PetFinderNetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PetFinderNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public MutableLiveData<petEntity[]> getPets() {
        return mDownloadedPets;
    }

    void fetchPets() {

        mExecutors.networkIO().execute(() -> {
            try {

                // The getUrl method will return the URL that we need to get the forecast JSON for the
                // weather. It will decide whether to create a URL based off of the latitude and
                // longitude or off of a simple location as a String.
                URL tokenurl = NetworkUtils.buildTokenUrl();
                String tokenresponse = NetworkUtils.gettokenFromHttpUrl(tokenurl);
                String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjM4ODkxNTkzNDYzZWFhZmIyNzkyN2NkMTE1Nzg1OWEyZThhZTNhMmEyNTI5NTZiZWIzYWVjYmQ5NjFhZmRhZjA1YWFhMmQ4YzQ4NmYzNWM2In0.eyJhdWQiOiJ4RHl6MHBmZXZtWkFTTnp0eERlaVRxendPSnY2UEx5a2NxdkFadmFtOFpmSDBCZVpubSIsImp0aSI6IjM4ODkxNTkzNDYzZWFhZmIyNzkyN2NkMTE1Nzg1OWEyZThhZTNhMmEyNTI5NTZiZWIzYWVjYmQ5NjFhZmRhZjA1YWFhMmQ4YzQ4NmYzNWM2IiwiaWF0IjoxNTU1NTMwNjg1LCJuYmYiOjE1NTU1MzA2ODUsImV4cCI6MTU1NTUzNDI4NSwic3ViIjoiIiwic2NvcGVzIjpbXX0.Goe8gRKryngd97zSPuNli3H_AI_i8ils6p2p5E5YD53zAtF-XSfte1v7mDZ71Jd_KYK6AhleaZ3IguLJ3pgtyx27xHTTuUr6wulJVuHvELuwpIG65MzCvC9Je6ouBMxOEGNNhyFyrd_tsUhaYM9_GJ5bThnnW92bCjXRucUf3sMrXp4h5GMZRQoa60l4kS7GD4AGGomjZHyPGy0GOomcbaICQJ7aMvKIkBTxbecVzt7uwchAOzOm8R89v1F6V104aTjI2ej_MsogxnYjeY-hqa_cWjDfaVm4oVbCxc-e6yYgmfN86hv7u6_KNfY5FE_-MtNlHiZ9-SIrPGuEg2ikbQ.eyJhdWQiOiJ4RHl6MHBmZXZtWkFTTnp0eERlaVRxendPSnY2UEx5a2NxdkFadmFtOFpmSDBCZVpubSIsImp0aSI6ImY0NDg5ZjdhY2I5MTY2Y2QwYWM4YmJhZTNkZDU2NzYxMGZmNmQyZjdiNmJkZTA5OTdkMmQzNmNhYjQxYWEwOGY1ZmFkMmQyOWJkZTUwYzljIiwiaWF0IjoxNTU1NDU3Nzc0LCJuYmYiOjE1NTU0NTc3NzQsImV4cCI6MTU1NTQ2MTM3Mywic3ViIjoiIiwic2NvcGVzIjpbXX0.pV-Qi7v4q438HQQMKgxCE4CXVC_7Dwvt_FxRkBJNsR_87QAiu4dvCLWA3chZFJFK-A8AmwlbvfBSOsv2hv96dxOyF2deymCnEEmgj4sYPu0vawYFkYs4rk5qyJN3bvCV-AZJwfz0k6Bu9oLxvPbUhvj7b79TT2Ozkgm8_AkDHcYiSat_Xel1xq0WGyRlGn9EzzrzPR5ubcRrXavKtWu1vqTD9QQkmsZDVCXgakSDryDdNkfsn-NTEK0pOo6_MaZbZgvyqfbQrEn0ueQTud4DNqdfFTsZfgHNW18iiLhqwuDHS10bwocL7WbwkuguZbtmYf9LcXKqgVoEXsAonnOZhQ";
                URL weatherRequestUrl = NetworkUtils.getUrl();

                // Use the URL to retrieve the JSON
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl,access_token);

                // Parse the JSON into a list of weather forecasts
                PetFinderResponse response = new PetFinderJsonParser().parse(jsonWeatherResponse);



                // As long as there are weather forecasts, update the LiveData storing the most recent
                // weather forecasts. This will trigger observers of that LiveData, such as the
                // SunshineRepository.
                if (response != null && response.getWeatherForecast().length != 0) {
                    Log.d(LOG_TAG, "JSON not null and has " + response.getWeatherForecast().length
                            + " values");


                    // When you are off of the main thread and want to update LiveData, use postValue.
                    // It posts the update to the main thread.
                    mDownloadedPets.postValue(response.getWeatherForecast());

                    // If the code reaches this point, we have successfully performed our sync
                }
            } catch (Exception e) {
                // Server probably invalid
                e.printStackTrace();
            }
        });
    }
}
