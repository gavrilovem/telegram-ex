package com.example.telegram_ex.db;

import com.example.telegram_ex.models.Dialog;
import com.example.telegram_ex.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public final class FirebaseHelper {
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final DatabaseReference REF_DATABASE_ROOT = FirebaseDatabase.getInstance("https://tgex-70244-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    public static User USER = null;

    public static void initUser() {
        if (FirebaseHelper.AUTH.getCurrentUser() != null)
            FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_USERS)
                    .child(FirebaseHelper.AUTH.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            FirebaseHelper.setUSER(dataSnapshot.getValue(User.class));
                        }
                    });
    }

    public static void setUSER(User USER) {
        FirebaseHelper.USER = USER;
    }

    public static void sendMessage(String text, User receivingUser) {
        String refDialogUser = FirebaseValues.NODE_MESSAGES + "/" + FirebaseHelper.AUTH.getUid() + "/" + receivingUser.uid;
        String refDialogReceivingUser = FirebaseValues.NODE_MESSAGES + "/" + receivingUser.uid + "/" + FirebaseHelper.AUTH.getUid();
        String messageKey = FirebaseHelper.REF_DATABASE_ROOT.child(refDialogUser).push().getKey();

        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put(FirebaseValues.MESSAGES_TEXT, text);
        messageMap.put(FirebaseValues.MESSAGES_FROM, FirebaseHelper.AUTH.getUid());
        messageMap.put(FirebaseValues.MESSAGES_TIMESTAMP, ServerValue.TIMESTAMP);

        HashMap<String, Object> dialogMap = new HashMap<>();
        dialogMap.put(refDialogUser + "/" + messageKey, messageMap);
        dialogMap.put(refDialogReceivingUser + "/" + messageKey, messageMap);

        FirebaseHelper.REF_DATABASE_ROOT.updateChildren(dialogMap);
    }

    public static void createDialog(String uid1, String uid2) {
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_DIALOGS)
                .child(uid1).child(uid2).setValue(new Dialog(uid2));
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseValues.NODE_DIALOGS)
                .child(uid2).child(uid1).setValue(new Dialog(uid1));
    }
}