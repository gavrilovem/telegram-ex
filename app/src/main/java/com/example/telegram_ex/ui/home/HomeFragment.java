package com.example.telegram_ex.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telegram_ex.R;
import com.example.telegram_ex.activities.DialogActivity;
import com.example.telegram_ex.databinding.FragmentHomeBinding;
import com.example.telegram_ex.db.Dialog;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.db.User;
import com.example.telegram_ex.ui.dialog.DialogFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private HomeViewModel mViewModel;
    private DatabaseReference refDialogs;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Dialog, DialogHolder> mAdapter;
    private ValueEventListener dialogEventListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.dialogs_recycle_view);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView() {
        refDialogs = FirebaseHelper.REF_DATABASE_ROOT
                .child(FirebaseValues.NODE_DIALOGS)
                .child(FirebaseHelper.AUTH.getUid());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        FirebaseRecyclerOptions<Dialog> options =
                new FirebaseRecyclerOptions.Builder<Dialog>()
                        .setQuery(refDialogs, Dialog.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<Dialog, DialogHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DialogHolder holder, int position, @NonNull Dialog model) {
                DatabaseReference mRefUsers = FirebaseHelper.REF_DATABASE_ROOT
                        .child(FirebaseValues.NODE_USERS)
                        .child(model.getUid());
                ValueEventListener mRefUsersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        holder.setName(user.name);
                        holder.setStatus(user.login);
                        CircleImageView civ = new CircleImageView(holder.itemView.getContext());
                        civ.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.default_photo));
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog(user);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                mRefUsers.addValueEventListener(mRefUsersListener);
            }

            @NonNull
            @Override
            public DialogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dialogs_item, parent, false);

                return new DialogHolder(view);
            }
        };
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    private void openDialog(User user) {
        Intent intent = new Intent(this.requireActivity(), DialogActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.stopListening();
    }
}