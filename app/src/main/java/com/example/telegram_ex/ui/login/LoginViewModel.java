package com.example.telegram_ex.ui.login;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public LoginViewModel() {

    }
    public boolean validateFields(EditText email, EditText password) {
        return false;
    }
}