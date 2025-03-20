package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityChangeUsernameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeUsernameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityChangeUsernameBinding binding;  // View Binding object

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityChangeUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Click listener for the change username button
        binding.changeUsernameReally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });

        // Click listener for going back to settings
        binding.gobackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToSettings();
            }
        });
    }

    private void changeUsername() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String currentPassword = binding.currentPassword.getText().toString().trim();
            String newUsername = binding.newUsername.getText().toString().trim();

            if (TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(newUsername)) {
                Toast.makeText(this, "Please enter a new username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newUsername.length() < 4) {
                Toast.makeText(this, "Username must be at least 4 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            // Re-authenticate the user before updating the username
            reAuthenticateAndChangeUsername(user, currentPassword, newUsername);
        }
    }

    private void reAuthenticateAndChangeUsername(FirebaseUser user, String currentPassword, String newUsername) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            Toast.makeText(this, "Error: Cannot retrieve email", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Step 1: Update the Firebase Authentication profile
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUsername)
                            .build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Step 2: Update Firestore
                                updateUsernameInFirestore(user.getUid(), newUsername);
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(ChangeUsernameActivity.this,
                                        "Failed: " + errorMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(ChangeUsernameActivity.this,
                            "Re-authentication failed: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUsernameInFirestore(String userId, String newUsername) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .update("username", newUsername)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeUsernameActivity.this,
                                    "Username updated successfully!",
                                    Toast.LENGTH_SHORT).show();
                            goToProfile();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(ChangeUsernameActivity.this,
                                    "Firestore update failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void goToProfile() {
        Intent intent = new Intent(this, ProfileFragment.class); // Change to your actual profile screen
        startActivity(intent);
        finish();
    }

    private void goBackToSettings() {
        Intent intent = new Intent(this, Update.class); // Change to your actual settings screen
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Clean up view binding
    }
}
