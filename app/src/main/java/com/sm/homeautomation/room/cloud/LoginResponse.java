package com.sm.homeautomation.room.cloud;

import com.sm.homeautomation.data.model.User;

public class LoginResponse {
    private int error;
    private String message;
    private User data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
