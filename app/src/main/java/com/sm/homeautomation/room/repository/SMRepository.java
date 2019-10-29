package com.sm.homeautomation.room.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.sm.homeautomation.room.cloud.DeviceApi;
import com.sm.homeautomation.room.database.DevicesDao;
import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.MainResponse;
import com.sm.homeautomation.room.model.DeviceResponse;
import com.sm.homeautomation.room.utils.ApiResponse;
import com.sm.homeautomation.room.utils.AppExecutors;
import com.sm.homeautomation.room.utils.NetworkBoundDirectResource;
import com.sm.homeautomation.room.utils.NetworkBoundResource;
import com.sm.homeautomation.room.utils.RateLimiter;
import com.sm.homeautomation.room.utils.Resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class SMRepository {

    private AppExecutors appExecutors;
    private DeviceApi deviceApi;
    private DevicesDao devicesDao;
    private RateLimiter repoListRateLimit = new RateLimiter<>(10, TimeUnit.SECONDS);

    @Inject
    public SMRepository(AppExecutors appExecutors, DeviceApi deviceApi, DevicesDao devicesDao) {
        this.appExecutors = appExecutors;
        this.deviceApi = deviceApi;
        this.devicesDao = devicesDao;
    }

    public LiveData<Resource<DeviceResponse>> getDeviceInfo(){
        return new NetworkBoundDirectResource<DeviceResponse>(appExecutors){
            @Override
            public void saveCallResult(@NonNull DeviceResponse item) {
                Timber.i("saveCallResult");
                if(item.getResp() != null) {
                    item.getResp().setConfigured(false);
                    devicesDao.insertPendingAddDevice(item.getResp());
                }
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<DeviceResponse>> createCall() {
                Timber.i("createCall");
                if(deviceApi!=null){
                    Timber.i("deviceapi not null");
                }
                return deviceApi.getDeviceInfo();
            }

        }.getAsLiveData();
    }

    public LiveData<Resource<DeviceResponse>> deviceConfigure(final Map<String,String> requestMap){
        return new NetworkBoundDirectResource<DeviceResponse>(appExecutors){
            @Override
            public void saveCallResult(@NonNull DeviceResponse item) {
                Timber.i("saveCallResult");

            }

            @NonNull
            @Override
            public LiveData<ApiResponse<DeviceResponse>> createCall() {
                Timber.i("createCall");
                if(deviceApi!=null){
                    Timber.i("deviceapi not null");
                }
                return deviceApi.configure(requestMap);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<DeviceResponse>> getConnectStatus(){
        return new NetworkBoundDirectResource<DeviceResponse>(appExecutors){
            @Override
            public void saveCallResult(@NonNull DeviceResponse item) {
                Timber.i("saveCallResult");
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<DeviceResponse>> createCall() {
                Timber.i("createCall");
                if(deviceApi!=null){
                    Timber.i("deviceapi not null");
                }
                return deviceApi.getConnectStatus();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<DeviceResponse>> closeDeviceHotspot(){
        return new NetworkBoundDirectResource<DeviceResponse>(appExecutors){
            @Override
            public void saveCallResult(@NonNull DeviceResponse item) {
                Timber.i("saveCallResult");
                if(item.getResp() != null) {
                    item.getResp().setConfigured(true);
                    devicesDao.updatePendingAddDevice(item.getResp());
                }
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<DeviceResponse>> createCall() {
                Timber.i("createCall");
                if(deviceApi!=null){
                    Timber.i("deviceapi not null");
                }
                return deviceApi.closeDeviceHotspot();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<DbaseDevice>>> getDevices(){
        return new NetworkBoundResource<List<DbaseDevice>, List<DbaseDevice>>(appExecutors){

            @Override
            protected void onFetchFailed() {
                super.onFetchFailed();
                Timber.i("onFetchFailed");
                repoListRateLimit.reset("getDevices");
            }

            @Override
            protected void saveCallResult(@NonNull List<DbaseDevice> item) {
                Timber.i("saveCallResult");
                devicesDao.insertDbaseDevice(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<DbaseDevice> data) {
                Timber.i("shouldFetch");
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("getDevices");
            }

            @NonNull
            @Override
            protected LiveData<List<DbaseDevice>> loadFromDb() {
                Timber.i("loadFromDb");
                return devicesDao.getAllDevices();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<DbaseDevice>>> createCall() {
                return deviceApi.getDbaseDevices();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<MainResponse>> addDevice(final Map<String,String> requestMap){
        return new NetworkBoundDirectResource<MainResponse>(appExecutors){
            @Override
            public void saveCallResult(@NonNull MainResponse item) {
                Timber.i("saveCallResult");
            }

            @NonNull
            @Override
            public LiveData<ApiResponse<MainResponse>> createCall() {
                Timber.i("createCall");
                if(deviceApi!=null){
                    Timber.i("deviceapi not null");
                }
                return deviceApi.addDevice(requestMap);
            }
        }.getAsLiveData();
    }


   /*
    public LiveData<Resource<List<DeviceResponse>>> AddDevice(){
        return new NetworkBoundResource<List<DeviceResponse>, List<DeviceResponse>>(appExecutors){


            @Override
            protected void saveCallResult(@NonNull List<DeviceResponse> item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<DeviceResponse> data) {
                Timber.i("shouldFetch");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<DeviceResponse>> loadFromDb() {
                return devicesDao.getAllPendingDevices();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<DeviceResponse>> createCall() {
                return deviceApi.getDeviceInfo();
            }
        }.getAsLiveData();
    }*/

}
