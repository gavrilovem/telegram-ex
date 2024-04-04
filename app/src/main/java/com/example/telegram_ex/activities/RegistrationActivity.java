package com.example.telegram_ex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telegram_ex.R;
import com.example.telegram_ex.databinding.ActivityRegistrationBinding;
import com.example.telegram_ex.ui.registration.RegistrationFragment;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RegistrationFragment.newInstance())
                    .commitNow();
        }
    }
    public void completeTask(String email, String password) {
        Intent data = new Intent();
        data.putExtra(LoginActivity.ACCESS_EMAIL, email);
        data.putExtra(LoginActivity.ACCESS_PASSWORD, password);
        setResult(RESULT_OK, data);
        finish();
    }
    public void failedTask() {
        Toast.makeText(this.getBaseContext(), "Registration failed.",
                Toast.LENGTH_SHORT).show();
    }
}