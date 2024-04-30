package com.example.telegram_ex.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telegram_ex.R;
import com.example.telegram_ex.db.User;
import com.example.telegram_ex.ui.dialog.DialogFragment;
import com.example.telegram_ex.databinding.ActivityDialogBinding;

public class DialogActivity extends AppCompatActivity {
    ActivityDialogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {

            DialogFragment dialogFragment = new DialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            dialogFragment.setArguments(bundle);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, dialogFragment)
                        .commitNow();
            }

            binding = ActivityDialogBinding.inflate(getLayoutInflater());
        }
    }
}