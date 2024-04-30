package com.example.telegram_ex.db;

import java.io.Serializable;

public class User implements Serializable {
    public String uid;
    public String email;
    public String login;
    public String name;
    public String status;

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