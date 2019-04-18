package com.caroline.lab2_myapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface petDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPet(petEntity pet);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<petEntity> pets);

    @Delete
    void deletePet(petEntity pet);

    @Query("SELECT * FROM pets WHERE id=:id")
    petEntity getPet(int id);

    @Query("SELECT * FROM pets ORDER BY date DESC")
    LiveData<List<petEntity>> getAll();

    @Query("DELETE FROM pets")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM pets")
    int getCount();
}
