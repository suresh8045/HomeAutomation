package com.sm.homeautomation.room.model;

//{"err":"0","msg":"success","status":"success","resp":{"id":"a0:20:a6:05:e6:95","mac_sta":"dc:4f:22:f9:87:1d","type":"SWITCH","mfr":"S&M","mdl":"basic","hw_v":"100","fw_v":"100","state":{"sw":{"pwr":1,"time":1568717822,"trig":1}}}}

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sm.homeautomation.room.database.Converters;

@Entity(tableName = "pending_devices", indices = {@Index(value = "id",unique = true)})
@TypeConverters({Converters.class})
public class PendingAddDevice {

    @NonNull
    @PrimaryKey
    private String id;
    private String mac_sta;
    private String type;
    private String mfr;
    private String mdl;
    private String hw_v;
    private String fw_v;
    private boolean isConfigured;
    @Embedded
    private DeviceState state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMac_sta() {
        return mac_sta;
    }

    public void setMac_sta(String mac_sta) {
        this.mac_sta = mac_sta;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMfr() {
        return mfr;
    }

    public void setMfr(String mfr) {
        this.mfr = mfr;
    }

    public String getMdl() {
        return mdl;
    }

    public void setMdl(String mdl) {
        this.mdl = mdl;
    }

    public String getHw_v() {
        return hw_v;
    }

    public void setHw_v(String hw_v) {
        this.hw_v = hw_v;
    }

    public String getFw_v() {
        return fw_v;
    }

    public void setFw_v(String fw_v) {
        this.fw_v = fw_v;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void setConfigured(boolean configured) {
        isConfigured = configured;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PendingAddDevice{" +
                "id='" + id + '\'' +
                ", mac_sta='" + mac_sta + '\'' +
                ", type='" + type + '\'' +
                ", mfr='" + mfr + '\'' +
                ", mdl='" + mdl + '\'' +
                ", hw_v='" + hw_v + '\'' +
                ", fw_v='" + fw_v + '\'' +
                ", state=" + state +
                '}';
    }
}
