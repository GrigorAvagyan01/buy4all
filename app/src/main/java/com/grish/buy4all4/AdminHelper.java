package com.grish.buy4all4;


import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminHelper {
    public interface AdminCheckCallback {
        void onResult(boolean isAdmin);
    }

    public static void checkIfCurrentUserIsAdmin(AdminCheckCallback callback) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (currentUserId == null) {
            callback.onResult(false);
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                        callback.onResult(isAdmin != null && isAdmin);
                    } else {
                        callback.onResult(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminHelper", "Failed to fetch user document", e);
                    callback.onResult(false);
                });
    }
}
