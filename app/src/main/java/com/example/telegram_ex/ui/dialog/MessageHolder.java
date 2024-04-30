package com.example.telegram_ex.ui.dialog;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telegram_ex.R;

public class MessageHolder extends RecyclerView.ViewHolder {
    public MessageHolder(View itemView) {
        super(itemView);
        userBlockMessage = itemView.findViewById(R.id.block_user_message);
        receivedBlockMessage = itemView.findViewById(R.id.block_received_message);

        userTextMessage = itemView.findViewById(R.id.chat_user_message);
        receivedTextMessage = itemView.findViewById(R.id.chat_received_message);

        userTimestamp = itemView.findViewById(R.id.chat_user_message_time);
        receivedTimestamp = itemView.findViewById(R.id.chat_received_message_time);
    }
    ConstraintLayout receivedBlockMessage;
    TextView receivedTextMessage;
    TextView receivedTimestamp;

    ConstraintLayout userBlockMessage;
    TextView userTextMessage;
    TextView userTimestamp;
    public void setMessage(String text) {
        receivedTextMessage.setText(text);
        userTextMessage.setText(text);
    }
    public void setTimestamp(String ts) {
        receivedTimestamp.setText(ts);
        userTimestamp.setText(ts);
    }
}
