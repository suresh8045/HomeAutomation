package com.sm.homeautomation.room.model;

public class DeviceResponse {
    //{"err":"0","msg":"device configured","status":"success"}
    private String err;
    private String msg;
    private String status;
    private PendingAddDevice resp;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PendingAddDevice getResp() {
        return resp;
    }

    public void setResp(PendingAddDevice resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "DeviceResponse{" +
                "err='" + err + '\'' +
                ", msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                ", resp=" + resp.toString() +
                '}';
    }
}
