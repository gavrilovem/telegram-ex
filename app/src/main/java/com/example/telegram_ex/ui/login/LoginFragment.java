package com.example.telegram_ex.ui.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telegram_ex.activities.LoginActivity;
import com.example.telegram_ex.MainActivity;
import com.example.telegram_ex.R;
import com.example.telegram_ex.activities.RegistrationActivity;
import com.example.telegram_ex.db.Dialog;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.db.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    final static String TAG = "LOGIN_STATE";
    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String email = data.getStringExtra(LoginActivity.ACCESS_EMAIL);
                        String password = data.getStringExtra(LoginActivity.ACCESS_PASSWORD);
                        loginUser(email, password);

                    }
                }
            });
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        view.findViewById(R.id.button_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = view.findViewById(R.id.field_email);
                EditText password = view.findViewById(R.id.field_password);
                loginUser(email.getText().toString(), password.getText().toString());
            }
        });
        view.findViewById(R.id.button_goto_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });
    }
    public void loginUser(String email, String password) {
        FirebaseHelper.AUTH.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseHelper.initUser();
                            gotoMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void register(View view) {
        Intent intent = new Intent(this.getContext(), RegistrationActivity.class);
        activityResultLaunch.launch(intent);
    }
    public void gotoMain() {
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        activityResultLaunch.launch(intent);
    }
}