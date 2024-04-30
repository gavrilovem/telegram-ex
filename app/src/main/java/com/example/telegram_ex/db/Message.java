package com.example.telegram_ex.db;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.telegram_ex.R;
import com.example.telegram_ex.databinding.MessageItemBinding;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    String from;
    String text;
    Long timestamp;
    public Message() {}
    public Message(String from, String text, Long timestamp) {
        this.from = from;
        this.text = text;
        this.timestamp = timestamp;
    }
    public String getFrom() {
        return from;
    }
    public String getText() {
        return text;
    }
    public Long getTimestamp() {
        return timestamp;
    }
}
