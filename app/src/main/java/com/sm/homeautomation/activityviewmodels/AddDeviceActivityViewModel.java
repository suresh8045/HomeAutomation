package com.sm.homeautomation.activityviewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sm.homeautomation.room.model.DeviceResponse;
import com.sm.homeautomation.room.repository.SMRepository;
import com.sm.homeautomation.room.utils.Resource;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class AddDeviceActivityViewModel extends ViewModel {

    private final SMRepository smRepository;
    private int flipperPosition;

    @Inject
    public AddDeviceActivityViewModel(SMRepository smRepository) {
        this.smRepository = smRepository;
    }

    public LiveData<Resource<DeviceResponse>> getDeviceInfo(){
        return smRepository.getDeviceInfo();
    }


    public LiveData<Resource<DeviceResponse>> configure(Map<String,String> requestMap){
        return smRepository.deviceConfigure(requestMap);
    }

    public int getFlipperPosition() {
        return flipperPosition;
    }

    public void setFlipperPosition(int flipperPosition) {
        this.flipperPosition = flipperPosition;
    }
}
