package com.sm.homeautomation.data.model;

import java.util.Date;

public class User {

    private int id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String gender;
    Date sessionExpiryDate;



    public User(int id, String name, String email, String gender){
        this.id = id;
        this.username = name;
        this.email = email;
        this.gender = gender;
    }

    public User(String name, String email, String password, String gender) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
