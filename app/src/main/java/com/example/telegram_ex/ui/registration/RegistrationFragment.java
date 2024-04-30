package com.example.telegram_ex.ui.registration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.telegram_ex.R;
import com.example.telegram_ex.activities.RegistrationActivity;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.db.User;
import com.example.telegram_ex.utils.AppStates;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Objects;

public class RegistrationFragment extends Fragment {
    private RegistrationViewModel mViewModel;
    private EditText email;
    private EditText name;
    private EditText login;
    private EditText password;
    private EditText password_v;
    final static String TAG = "REGISTRATION_STATE";
    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.field_email);
        name = view.findViewById(R.id.field_name);
        login = view.findViewById(R.id.field_login);
        password = view.findViewById(R.id.field_password);
        password_v = view.findViewById(R.id.field_password_verify);

        view.findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser() {
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        String _password_v = password_v.getText().toString();
        if (!_password.equals(_password_v)) {
            Toast.makeText(this.requireActivity().getBaseContext(), "Passwords must be equal", Toast.LENGTH_SHORT).show(); // TODO string value
        }
        FirebaseHelper.AUTH.createUserWithEmailAndPassword(_email, _password)
            .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        String uid = Objects.requireNonNull(FirebaseHelper.AUTH.getCurrentUser()).getUid();
                        User user = new User(uid, email.getText().toString(), login.getText().toString(), name.getText().toString(), AppStates.ONLINE.toString());
                        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS).child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) ((RegistrationActivity)requireActivity()).completeTask(_email, _password);
                                else Toast.makeText(requireActivity().getBaseContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        ((RegistrationActivity)requireActivity()).failedTask();
                    }
                }
            });
    }
}