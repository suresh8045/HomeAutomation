package com.sm.homeautomation.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.PendingAddDevice;
import com.sm.homeautomation.room.repository.SMRepository;
import com.sm.homeautomation.room.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<PendingAddDevice>> devicesLiveData=new MutableLiveData<>();


    SMRepository smRepository;

    @Inject
    public HomeViewModel(SMRepository smRepository) {
        this.smRepository = smRepository;
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setDevicesLiveData(List<PendingAddDevice> devicesLiveData) {
        this.devicesLiveData.setValue(devicesLiveData);
    }

    LiveData<Resource<List<DbaseDevice>>> getDevices(){
        return smRepository.getDevices();
    }
}