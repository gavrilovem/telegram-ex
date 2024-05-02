package com.example.telegram_ex.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telegram_ex.R;
import com.example.telegram_ex.db.FirebaseHelper;
import com.example.telegram_ex.db.FirebaseValues;
import com.example.telegram_ex.models.Message;
import com.example.telegram_ex.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogFragment extends Fragment {
    private DialogViewModel mViewModel;
    private View mToolbarInfo;
    private TextView user_name;
    private TextView user_status;
    private DatabaseReference mUserRef;
    private DatabaseReference refMessages;
    private ValueEventListener mListenerInfoToolbar;
    private ValueEventListener messagesValueListener;
    private EditText inputMessage;
    private ImageButton sendMessageButton;
    private RecyclerView messagesRecyclerView;
    private FirebaseRecyclerAdapter<Message, MessageHolder> mAdapter;
    private User user;
    List<Message> listMessages;

    public DialogFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        user = (User)getArguments().getSerializable("user");
        mViewModel = new ViewModelProvider(this).get(DialogViewModel.class);
        messagesRecyclerView = v.findViewById(R.id.chat_recycle_view);
        inputMessage = v.findViewById(R.id.chat_input_message);
        sendMessageButton = v.findViewById(R.id.imageButton);
        listMessages = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbarInfo = view.findViewById(R.id.toolbar);
        user_name = (TextView) mToolbarInfo.findViewById(R.id.toolbar_contact_name);
        user_status = (TextView) mToolbarInfo.findViewById(R.id.toolbar_contact_status);
    }

    @Override
    public void onResume() {
        super.onResume();

        initToolbar();
        initRecyclerView();

        // send message button init
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                if (!message.equals("")) {
                    FirebaseHelper.sendMessage(message, user);
                    FirebaseHelper.createDialog(FirebaseHelper.USER.uid, user.uid);
                    inputMessage.setText("");
                }
            }
        });
    }

    private void initToolbar() {
        mListenerInfoToolbar = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                // TODO photo update
                user_name.setText(user.name);
                user_status.setText(user.status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserRef = FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS).child(user.uid);
        mUserRef.addValueEventListener(mListenerInfoToolbar);
    }

    private void initRecyclerView() {
        refMessages = FirebaseHelper.REF_DATABASE_ROOT
                .child(FirebaseValues.NODE_MESSAGES)
                .child(FirebaseHelper.AUTH.getUid())
                .child(user.uid);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                    .setQuery(refMessages, Message.class)
                    .build();
        mAdapter = new FirebaseRecyclerAdapter<Message, MessageHolder>(options) {
            @NonNull
            @Override
            public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                return new MessageHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
                if (Objects.equals(model.getFrom(), FirebaseHelper.AUTH.getUid())) {
                    holder.receivedBlockMessage.setVisibility(View.GONE);
                    holder.userBlockMessage.setVisibility(View.VISIBLE);
                    holder.setMessage(model.getText());
                    Timestamp timestamp = new Timestamp(model.getTimestamp());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    holder.setTimestamp(simpleDateFormat.format(timestamp));
                } else {
                    holder.receivedBlockMessage.setVisibility(View.VISIBLE);
                    holder.userBlockMessage.setVisibility(View.GONE);
                    holder.setMessage(model.getText());
                    Timestamp timestamp = new Timestamp(model.getTimestamp());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    holder.setTimestamp(simpleDateFormat.format(timestamp));
                }
            }
        };
        messagesValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listMessages.add(ds.getValue(Message.class));
                }
                messagesRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        messagesRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
        refMessages.addValueEventListener(messagesValueListener);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mToolbarInfo.setVisibility(View.INVISIBLE);
//        fab.setVisibility(View.VISIBLE);
        mAdapter.stopListening();
        mUserRef.removeEventListener(mListenerInfoToolbar);
        refMessages.removeEventListener(messagesValueListener);
    }
}