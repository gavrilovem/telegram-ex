package com.example.telegram_ex.db;

public final class FirebaseValues {
    public final String NODE_USERS = "users";
    public final String USERS_ID = "uid";
    public final String USERS_LOGIN = "login";
    public final String USERS_EMAIL = "email";
    public final String USERS_NAME = "name";

    private FirebaseValues(){}

    private static FirebaseValues instance;
    public static synchronized FirebaseValues getInstance() {
        if (instance == null) {
            instance = new FirebaseValues();
        }
        return instance;
    }
}