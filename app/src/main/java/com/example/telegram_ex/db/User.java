package com.example.telegram_ex.db;

public class User {
    public String uid;
    public String email;
    public String login;
    public String name;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String email, String login, String name) {
        this.uid = uid;
        this.email = email;
        this.login = login;
        this.name = name;
    }
}