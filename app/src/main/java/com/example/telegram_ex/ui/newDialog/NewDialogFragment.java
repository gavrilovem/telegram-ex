package com.example.telegram_ex.ui.newDialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.telegram_ex.R;
import com.example.telegram_ex.activities.DialogActivity;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.models.User;
import com.example.telegram_ex.ui.home.HomeFragmentDirections;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDialogFragment extends Fragment {
    EditText input;
    Query queryUsers;
    RecyclerView foundUsersRecyclerView;

    public NewDialogFragment() {
        // Required empty public constructor
    }
    public static NewDialogFragment newInstance() {
        return new NewDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_dialog, container, false);
        input = v.findViewById(R.id.new_dialog_input);
        foundUsersRecyclerView = (RecyclerView) v.findViewById(R.id.new_dialog_recycler_view);
        foundUsersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= FirebaseValues.MIN_CHARS_LOGIN) {
                    searchForUser(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void searchForUser(String login) {
        queryUsers = FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS)
                .orderByChild(FirebaseValues.USERS_LOGIN)
                .startAt(login)
                .endAt(login + "\uf8ff");
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                    .setQuery(queryUsers, User.class)
                    .build();

        FirebaseRecyclerAdapter<User, UserHolder> mAdapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
                holder.setName(model.name);
                holder.setStatus(model.login);
                holder.setPhoto(holder.itemView.getResources().getDrawable(R.drawable.default_photo));
                holder.itemView.setOnClickListener(v -> {
                    NewDialogFragmentDirections.ActionNavNewDialogToNavDialog action = NewDialogFragmentDirections.actionNavNewDialogToNavDialog(model);
                    Navigation.findNavController(v).navigate(action);
                });
            }

            @NonNull
            @Override
            public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
                return new UserHolder(v);
            }
        };

        foundUsersRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }
}