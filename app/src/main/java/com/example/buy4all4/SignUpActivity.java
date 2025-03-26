package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.Signinbut.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, SignInActivity.class)));

        binding.signupButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = Objects.requireNonNull(binding.username.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(binding.password.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.password2.getEditText()).getText().toString().trim();

        if (validateInputs(username, email, password, confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                saveUserToFirestore(firebaseUser.getUid(), username, email);
                            }
                        } else {
                            Log.e("SignUpActivity", "Registration failed", task.getException());
                            Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        boolean isValid = true;
        if (TextUtils.isEmpty(username)) {
            binding.username.setError("Username is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is required");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            binding.password2.setError("Passwords do not match");
            isValid = false;
        }
        return isValid;
    }

    private void saveUserToFirestore(String userId, String username, String email) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("username", username);
        userMap.put("email", email);

        db.collection("users").document(userId).set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, HomePage.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
