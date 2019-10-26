package com.sm.homeautomation.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.PendingAddDevice;


@Database(entities = {DbaseDevice.class, PendingAddDevice.class}, version = SMDatabase.VERSION, exportSchema = false)
public abstract class SMDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "sm_db";
    private static SMDatabase instance;

    static final int VERSION = 1;

   /* public static SMDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SMDatabase.class,
                    DATABASE_NAME)
                    .build();
        }
        return instance;
    }*/

    public abstract DevicesDao getDevicesDao();
}
