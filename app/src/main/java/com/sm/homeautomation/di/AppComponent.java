package com.sm.homeautomation.di;

import android.app.Application;

import com.sm.homeautomation.AddDeviceActivity;
import com.sm.homeautomation.MainActivity;
import com.sm.homeautomation.room.cloud.DeviceApi;
import com.sm.homeautomation.room.database.SMDatabase;
import com.sm.homeautomation.room.repository.SMRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import retrofit2.Retrofit;


@Singleton
@Component(modules = {UtilsModule.class,RetrofitModule.class,RepoModule.class, ViewModelModule.class, ActivityModule.class, FragmentModule.class, AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<AppController> {

   // Application application();
    //SMRepository getSMRepository();
    //SMDatabase getSMDatabase();
    //DeviceApi getDeviceApi();

    /* We will call this builder interface from our custom Application class.
     * This will set our application object to the AppComponent.
     * So inside the AppComponent the application instance is available.
     * So this application instance can be accessed by our modules
     * such as ApiModule when needed
     * */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    /*
     * This is our custom Application class
     * */
    void injectApplication(AppController appController);

}
