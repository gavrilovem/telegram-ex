package com.example.telegram_ex.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseHelper {
    public final FirebaseAuth AUTH;
    public final DatabaseReference REF_DATABASE_ROOT;

    private FirebaseHelper() {
        AUTH = FirebaseAuth.getInstance();
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance("https://tgex-70244-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference();
    }

    private static FirebaseHelper instance;
    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }
}