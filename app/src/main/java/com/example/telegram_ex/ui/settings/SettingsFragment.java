package com.example.telegram_ex.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.telegram_ex.R;
import com.example.telegram_ex.activities.LoginActivity;
import com.example.telegram_ex.databinding.FragmentSettingsBinding;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.db.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS).child(Objects.requireNonNull(FirebaseHelper.AUTH.getUid())).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            binding.settingsFullName.setText(task.getResult().getValue(User.class).name);
                            binding.settingsStatus.setText(task.getResult().getValue(User.class).status);
                        }
                    }
                });

        binding.settingsBtnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requireActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment_content_main, new ChangeNameFragment())
//                        .commit();
            }
        });

        binding.settingsBtnChangeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requireActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment_content_main, new ChangeLoginFragment())
//                        .commit();
            }
        });

        binding.settingsBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.AUTH.signOut();
                FirebaseHelper.USER = null;
                updateUI();
            }
        });
        return root;
    }

    private void updateUI() {
        if (FirebaseHelper.AUTH.getCurrentUser() == null) {
            Intent intent = new Intent(this.getContext(), LoginActivity.class);
            startActivity(intent);
        }
        // TODO вынести в отдельный класс
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}