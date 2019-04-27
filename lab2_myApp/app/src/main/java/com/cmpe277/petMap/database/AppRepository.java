package com.cmpe277.petMap.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.cmpe277.petMap.AppExecutors;
import com.cmpe277.petMap.util.SampleData;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {

    private static final Object LOCK = new Object();
    private static AppRepository ourInstance;
    private static Context mContext;
    public LiveData<List<petEntity>> mPets;
    public MutableLiveData<petEntity[]> mPets2;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();
    private final AppExecutors mExecutors = AppExecutors.getInstance();
    private boolean mInitialized = false;


    private PetFinderNetworkDataSource mPetFinderNetworkDataSource;
    private petDAO mPetDao;

    public static AppRepository getInstance(Context context) {
        if(ourInstance==null){
            mContext = context;
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mPets = getAllPets();
        mPetDao = mDb.petDAO();
        mPetFinderNetworkDataSource =  PetFinderNetworkDataSource.getInstance(mContext,mExecutors);
        mPets2 = mPetFinderNetworkDataSource.getPets();

        mPets2.observeForever(myNewPets->{
            mExecutors.diskIO().execute(()->{
                deleteOldData();
                mPetDao.insertAll(Arrays.asList(myNewPets));
            });
        });
    }

    public void addPetsOfType(String type){
        //deleteOldData();
        mPetFinderNetworkDataSource =  PetFinderNetworkDataSource.getInstance(mContext,mExecutors);
        mPetFinderNetworkDataSource.fetchAnimalsByType(type);
        mPets2 = mPetFinderNetworkDataSource.getPets();

        /*mPets2.observeForever(myNewPets->{
            mExecutors.diskIO().execute(()->{
                deleteOldData();
                mPetDao.insertAll(Arrays.asList(myNewPets));
            });
        });*/
    }

    private void deleteOldData() {
        mPetDao.deleteAll();
    }

    public void addSamplePets() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.petDAO().insertAll(SampleData.getPets());
            }
        });
    }
    private LiveData<List<petEntity>> getAllPets(){
        return mDb.petDAO().getAll();
    }

    public void deleteAllPets() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.petDAO().deleteAll();
            }
        });
    }
}
