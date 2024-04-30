package com.example.telegram_ex.utils;

import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;

public enum AppStates {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    String state;

    AppStates(String state) {
        this.state = state;
    }

    public static void updateState(AppStates appStates){
        /*Функция принимает состояние и записывает в базу данных*/
        if (FirebaseHelper.AUTH.getCurrentUser()!=null){
            FirebaseHelper.REF_DATABASE_ROOT
                    .child(FirebaseValues.NODE_USERS)
                    .child(FirebaseHelper.AUTH.getUid())
                    .child(FirebaseValues.USERS_STATUS)
                    .setValue(appStates.state);
        }
    }
}
