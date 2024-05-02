package com.example.telegram_ex.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telegram_ex.R;
import com.example.telegram_ex.databinding.ActivityLoginBinding;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.ui.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    public static final String ACCESS_EMAIL = "ACCESS_EMAIL";
    public static final String ACCESS_PASSWORD = "ACCESS_PASSWORD";
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseHelper.AUTH.getCurrentUser() == null) {
            setContentView(R.layout.activity_login);
        } else finish();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
    }
}