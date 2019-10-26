package com.sm.homeautomation.di;

import com.sm.homeautomation.ui.home.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();
}