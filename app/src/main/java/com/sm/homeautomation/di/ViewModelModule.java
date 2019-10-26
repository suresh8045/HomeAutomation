package com.sm.homeautomation.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sm.homeautomation.AddDeviceActivity;
import com.sm.homeautomation.activityviewmodels.AddDeviceActivityViewModel;
import com.sm.homeautomation.ui.home.HomeViewModel;
import com.sm.homeautomation.ui.login.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


    /*
     * This method basically says
     * inject this object into a Map using the @IntoMap annotation,
     * with the  MovieListViewModel.class as key,
     * and a Provider that will build a MovieListViewModel
     * object.
     *
     * */

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    protected abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddDeviceActivityViewModel.class)
    protected abstract ViewModel bindAddDeviceActivityViewModel(AddDeviceActivityViewModel addDeviceActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    protected abstract ViewModel bindLoginActivityViewModel(LoginViewModel loginViewModel);


}