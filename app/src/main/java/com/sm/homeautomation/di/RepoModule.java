package com.sm.homeautomation.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.sm.homeautomation.room.database.DevicesDao;
import com.sm.homeautomation.room.database.SMDatabase;
import com.sm.homeautomation.room.repository.SMRepository;
import com.sm.homeautomation.room.utils.AppExecutors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {

    @Provides
    public AppExecutors providesAppExecutors(){
        return AppExecutors.getInstance();
    }


    @Singleton
    @Provides
    SMDatabase providesRoomDatabase(@NonNull Application mApplication) {
        return Room.databaseBuilder(mApplication, SMDatabase.class, "sm-db").build();
    }

    @Singleton
    @Provides
    public DevicesDao providesDevicesDao(@NonNull SMDatabase smDatabase){
        return smDatabase.getDevicesDao();
    }


}
