package com.sm.homeautomation.room.model;

import java.util.List;

public class DevicesResponse {
    private String error;
    private String msg;
    private List<DbaseDevice> dbaseDevices;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DbaseDevice> getDbaseDevices() {
        return dbaseDevices;
    }

    public void setDbaseDevices(List<DbaseDevice> dbaseDevices) {
        this.dbaseDevices = dbaseDevices;
    }
}
