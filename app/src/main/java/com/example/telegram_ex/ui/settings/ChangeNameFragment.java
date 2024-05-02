package com.example.telegram_ex.ui.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telegram_ex.R;
import com.example.telegram_ex.databinding.FragmentChangeLoginBinding;
import com.example.telegram_ex.databinding.FragmentChangeNameBinding;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeNameFragment extends Fragment {
    EditText input;
    Button submit;
    public ChangeNameFragment() {
        // Required empty public constructor
    }
    public static ChangeNameFragment newInstance() {
        return new ChangeNameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_name, container, false);
        input = v.findViewById(R.id.settings_input_name);
        submit = v.findViewById(R.id.settings_btn_name);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();

            }
        });
        return v;
    }

    private void change() {
        String newName = input.getText().toString();
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS)
                .child(FirebaseHelper.AUTH.getUid())
                .child(FirebaseValues.USERS_NAME)
                .setValue(newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseHelper.USER.name = newName;
                        Toast.makeText(getContext(), getResources().getText(R.string.change_name_success), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}