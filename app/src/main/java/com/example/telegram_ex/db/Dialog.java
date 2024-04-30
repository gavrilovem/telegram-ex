package com.example.telegram_ex.db;

import java.io.Serializable;

public class Dialog implements Serializable {
    private String uid;
    public Dialog() {}
    public Dialog(String uid) {
        this.uid = uid;
    }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
}
