package com.sm.homeautomation.di;


import android.app.Application;

import androidx.annotation.NonNull;

import com.sm.homeautomation.utils.SessionHandler;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class UtilsModule {

    @Provides
    SessionHandler bindSessionHandler(@NonNull Application mApplication){
        return new SessionHandler(Application)
    }
}
