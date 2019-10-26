package com.sm.homeautomation.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.sm.homeautomation.data.model.LoggedInUser;
import com.sm.homeautomation.room.cloud.DeviceApi;
import com.sm.homeautomation.room.cloud.LoginResponse;
import com.sm.homeautomation.room.model.DeviceResponse;
import com.sm.homeautomation.room.utils.ApiResponse;
import com.sm.homeautomation.room.utils.AppExecutors;
import com.sm.homeautomation.room.utils.NetworkBoundDirectResource;
import com.sm.homeautomation.room.utils.Resource;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
@Singleton
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    private AppExecutors appExecutors;
    private DeviceApi deviceApi;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    @Inject
    public LoginRepository(AppExecutors appExecutors, DeviceApi deviceApi) {
        //this.dataSource = dataSource;
        this.appExecutors = appExecutors;
        this.deviceApi = deviceApi;
    }

    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LiveData<Resource<LoginResponse>> login(Map<String,String> requestMap){
        return new NetworkBoundDirectResource<LoginResponse>(appExecutors){
            @Override
            public void saveCallResult(@NonNull LoginResponse item) {
                Timber.i("saveCallResult");
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<LoginResponse>> createCall() {
                Timber.i("createCall");
                if(deviceApi!=null){
                    Timber.i("deviceapi not null");
                }
                return deviceApi.login(requestMap);
            }

        }.getAsLiveData();
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
