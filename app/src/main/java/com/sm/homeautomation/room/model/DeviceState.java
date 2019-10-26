package com.sm.homeautomation.room.model;

import androidx.room.Embedded;

public class DeviceState {
    @Embedded
    private DeviceSwitch sw;

    public DeviceSwitch getSw() {
        return sw;
    }

    public void setSw(DeviceSwitch sw) {
        this.sw = sw;
    }

}
