package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.buy4all4.databinding.ActivityChangeUsernameBinding;

public class ChangeUsernameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityChangeUsernameBinding binding;  // Declare the View Binding object

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the View Binding
        binding = ActivityChangeUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Set the click listener for the change username button
        binding.changeUsernameReally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });

        // Set the click listener for going back to settings
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
            String newUsername = binding.newUsername.getText().toString().trim(); // Use View Binding to get the text
            if (TextUtils.isEmpty(newUsername)) {
                Toast.makeText(this, "Please enter a new username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newUsername.length() < 4) {
                Toast.makeText(this, "Username must be at least 4 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            // Step 1: Update the Firebase Authentication profile
            user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(newUsername)
                            .build())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Step 2: Update Firestore
                                updateUsernameInFirestore(user.getUid(), newUsername);
                            } else {
                                Toast.makeText(ChangeUsernameActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateUsernameInFirestore(String userId, String newUsername) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Assuming you have a `users` collection and the document ID is the user's UID
        db.collection("users").document(userId)
                .update("username", newUsername)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeUsernameActivity.this, "Username updated in Firestore", Toast.LENGTH_SHORT).show();
                            // Step 3: Go to the profile screen
                            goToProfile();
                        } else {
                            Toast.makeText(ChangeUsernameActivity.this, "Failed to update username in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToProfile() {
        Intent intent = new Intent(this, ProfileFragment.class); // Change ProfileActivity to your actual profile screen
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
        // Clean up view binding when the activity is destroyed
        binding = null;
    }
}
