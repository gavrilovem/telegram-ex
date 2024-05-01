package com.example.telegram_ex.db;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class FirebaseStorageHelper {
    StorageReference REF_FIREBASE_STORAGE = FirebaseStorage.getInstance().getReference();
}
