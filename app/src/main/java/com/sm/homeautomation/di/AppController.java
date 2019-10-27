package com.sm.homeautomation.di;

import android.app.Activity;
import android.app.Application;

import com.sm.homeautomation.BuildConfig;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import timber.log.Timber;

/*
 * we use our AppComponent (now prefixed with Dagger)
 * to inject our Application class.
 * This way a DispatchingAndroidInjector is injected which is
 * then returned when an injector for an activity is requested.
 * */
public class AppController extends Application implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;


    @Override
    public AndroidInjector<Object> androidInjector() {

        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        //AndroidInjection.inject(this);
        DaggerAppComponent
                .builder()
                .application(this)
//                .contextModule(new ContextModule(getApplicationContext()))
                .build()
                .injectApplication(this);
                //.create()
                //.injectApplication(this);

    }


}