package com.example.telegram_ex.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telegram_ex.R;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeLoginFragment extends Fragment {
    EditText input;
    Button submit;
    public ChangeLoginFragment() {
        // Required empty public constructor
    }
    public static ChangeLoginFragment newInstance() {
        return new ChangeLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_login, container, false);
        input = v.findViewById(R.id.settings_input_login);
        submit = v.findViewById(R.id.settings_btn_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run(input.getText().toString());
            }
        });
        return v;
    }

    private void run(String newLogin) {
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS)
                .orderByChild(FirebaseValues.USERS_LOGIN)
                .equalTo(newLogin)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {
                            change(newLogin);
                        } else {
                            Toast.makeText(requireContext(), getResources().getText(R.string.user_already_exists), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void change(String newLogin) {
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS)
                .child(FirebaseHelper.AUTH.getUid())
                .child(FirebaseValues.USERS_LOGIN)
                .setValue(newLogin)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseHelper.USER.login = newLogin;
                        Toast.makeText(getContext(), getResources().getText(R.string.change_login_success), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}