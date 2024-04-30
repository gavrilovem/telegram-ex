package com.example.telegram_ex;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.telegram_ex.activities.LoginActivity;
import com.example.telegram_ex.databinding.ActivityMainBinding;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.db.User;
import com.example.telegram_ex.utils.AppStates;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public Toolbar toolbar;
    public FloatingActionButton fab;
    public NavigationView navigationView;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseHelper.initUser();
        if (FirebaseHelper.AUTH.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else FirebaseHelper.initUser();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        toolbar = binding.appBarMain.toolbar;
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        fab = binding.appBarMain.fab;

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        if (FirebaseHelper.AUTH.getCurrentUser() != null) {
            FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS)
                .child(Objects.requireNonNull(FirebaseHelper.AUTH.getUid())).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            // TODO instantiate user hashmap somewhere
                            User user = task.getResult().getValue(User.class);
                            ((TextView) findViewById(R.id.user_name)).setText(user.name);
                            ((TextView) findViewById(R.id.user_credentials)).setText(user.email);
                        }
                    }
                });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppStates.updateState(AppStates.ONLINE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppStates.updateState(AppStates.OFFLINE);
    }
}