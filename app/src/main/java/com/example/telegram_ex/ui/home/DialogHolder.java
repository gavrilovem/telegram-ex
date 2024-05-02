package com.example.telegram_ex.ui.home;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telegram_ex.R;

import de.hdodenhof.circleimageview.CircleImageView;

class DialogHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView status;
    private CircleImageView photo;
    public DialogHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.dialog_item_name);
        this.status = itemView.findViewById(R.id.dialog_item_status);
        this.photo = new CircleImageView(itemView.getContext());
        this.photo.setImageDrawable(itemView.getResources().getDrawable(R.drawable.default_photo));
    }
    public void setName(String name) {
        this.name.setText(name);
    }
    public void setStatus(String status) {
        this.status.setText(status);
    }
    public void setPhoto(Drawable photo) {
        CircleImageView civ = new CircleImageView(this.itemView.getContext());
        civ.setImageDrawable(photo);
        this.photo = civ;
    }
}