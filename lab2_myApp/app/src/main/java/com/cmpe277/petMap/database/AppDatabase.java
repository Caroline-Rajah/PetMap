package com.cmpe277.petMap.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {petEntity.class},version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME="AppDatabase.db";
    public static volatile AppDatabase instance;
    public static final Object LOCK = new Object();

    public abstract com.cmpe277.petMap.database.petDAO petDAO();

    public static AppDatabase getInstance(Context context) {
        if(instance==null){
            synchronized (LOCK){
                if(instance==null){
                    instance= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
