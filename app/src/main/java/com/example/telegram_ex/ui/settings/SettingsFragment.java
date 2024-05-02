package com.example.telegram_ex.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.telegram_ex.R;
import com.example.telegram_ex.activities.LoginActivity;
import com.example.telegram_ex.databinding.FragmentSettingsBinding;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        binding.settingsFullName.setText(FirebaseHelper.USER.name);
        binding.settingsStatus.setText(FirebaseHelper.USER.status);

        binding.settingsBtnChangeName.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_nav_settings_to_settings_change_name)
        );

        binding.settingsBtnChangeLogin.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_nav_settings_to_settings_change_login)
        );

        binding.settingsBtnLogOut.setOnClickListener(v -> {
            FirebaseHelper.AUTH.signOut();
            FirebaseHelper.USER = null;
            updateUI();
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