package com.sm.homeautomation.room.cloud;

import androidx.lifecycle.LiveData;

import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.DeviceResponse;
import com.sm.homeautomation.room.model.PendingAddDevice;
import com.sm.homeautomation.room.utils.ApiResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface DeviceApi {

    @GET("/get")
    LiveData<ApiResponse<DeviceResponse>> getInfo();

    @GET("http://192.168.4.1/getInfo")
    LiveData<ApiResponse<DeviceResponse>> getDeviceInfo();

    @GET("http://192.168.4.1/configure")
    LiveData<ApiResponse<DeviceResponse>> configure(@QueryMap Map<String, String> options);

    @GET("http://192.168.4.1/getConnectStatus")
    LiveData<ApiResponse<DeviceResponse>> getConnectStatus();

    @GET("http://192.168.4.1/allowSoftAPClose")
    LiveData<ApiResponse<DeviceResponse>> getCloseHotspot();


    @FormUrlEncoded
    @POST("/mlogin.php")
    LiveData<ApiResponse<LoginResponse>> login(@FieldMap Map<String, String> options);


    @GET("/getDevices.php")
    LiveData<ApiResponse<List<DbaseDevice>>> getDbaseDevices();



}
