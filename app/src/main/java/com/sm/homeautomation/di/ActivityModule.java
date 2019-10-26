package com.sm.homeautomation.di;

import com.sm.homeautomation.AddDeviceActivity;
import com.sm.homeautomation.MainActivity;
import com.sm.homeautomation.data.LoginRepository;
import com.sm.homeautomation.ui.login.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract AddDeviceActivity contributeaddDeviceActivity();

    @ContributesAndroidInjector()
    abstract LoginActivity contributeLoginActivity();
}