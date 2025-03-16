package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityPasswordForUpdateBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordForUpdate extends AppCompatActivity {

    private ActivityPasswordForUpdateBinding binding;  // View binding object
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityPasswordForUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up onClickListener for changeNext button
        binding.changeNext.setOnClickListener(v -> {
            String password = binding.passwordForupdated.getText().toString().trim();

            // Check if password is empty
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(PasswordForUpdate.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            } else {
                // Check password from Firestore
                checkPasswordFromFirestore(password);
            }
        });

        // Set up onClickListener for gobackprofile button
        binding.gobackprofile.setOnClickListener(v -> {
            // Navigate to ProfileFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment()) // Replace with your container's ID and the fragment
                    .addToBackStack(null)  // Optional: add the transaction to the back stack to enable back navigation
                    .commit();

            // Optional: Add a transition animation
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void checkPasswordFromFirestore(String password) {
        firestore.collection("passwords").document("passwordDoc")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String storedPassword = documentSnapshot.getString("password");

                        // If password matches
                        if (storedPassword != null && storedPassword.equals(password)) {
                            // Navigate to Update Activity
                            Intent intent = new Intent(PasswordForUpdate.this, Update.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PasswordForUpdate.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PasswordForUpdate.this, "No password found in database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PasswordForUpdate.this, "Error fetching password", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up view binding
        binding = null;
    }

    @Override
    public void onBackPressed() {
        // Handle back button press (navigates to the previous activity)
        super.onBackPressed();
    }
}
