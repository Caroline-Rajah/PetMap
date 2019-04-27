package com.caroline.lab2_myapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.cmpe277.petMap.database.AppDatabase;
import com.cmpe277.petMap.database.petDAO;
import com.cmpe277.petMap.database.petEntity;
import com.cmpe277.petMap.util.SampleData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    public static final String TAG="JUnit";
    private AppDatabase mDb;
    private petDAO mDao;

    @Before
    public void createDb(){
        Context context = InstrumentationRegistry.getContext();
        mDb= Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();
        mDao = mDb.petDAO();
        Log.i(TAG, "createDb: ");
    }

    @After
    public void closeDb(){
        mDb.close();
        Log.i(TAG, "closeDb: ");
    }

    @Test
    public void createAndRetrieveNote(){
        mDao.insertAll(SampleData.getPets());
        int count = mDao.getCount();
        Log.i(TAG, "createAndRetrieveNote: count = "+count);
        assertEquals(SampleData.getPets().size(),count);
    }
    
    @Test
    public void compareString(){
        mDao.insertAll(SampleData.getPets());
        petEntity fromDb = mDao.getPet(1);
        petEntity original = SampleData.getPets().get(0);
        Log.i(TAG, "compareString: ");
        assertEquals(fromDb.getName(),original.getName());

    }
}
