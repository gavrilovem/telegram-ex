package com.example.telegram_ex.models;

import com.example.telegram_ex.utils.AppStates;

import java.io.Serializable;

public class User implements Serializable {
    public String uid = "default_uid";
    public String email = "default.email@example.com";
    public String login = "default_login";
    public String name = "default_name";
    public String status = AppStates.ONLINE.toString();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String email, String login, String name, String status) {
        this.uid = uid;
        this.email = email;
        this.login = login;
        this.name = name;
        this.status = status;
    }
}