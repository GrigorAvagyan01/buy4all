package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.Signinbut.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        binding.signupButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = Objects.requireNonNull(binding.username.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(binding.password.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.password2.getEditText()).getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            binding.username.setError("Username is required");
            return;
        } else {
            binding.username.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email is required");
            return;
        } else {
            binding.email.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is required");
            return;
        } else {
            binding.password.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            binding.password2.setError("Passwords do not match");
            return;
        } else {
            binding.password2.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToFirestore(firebaseUser.getUid(), username, email);
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed: " +
                                Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String username, String email) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", userId);
        userMap.put("username", username);
        userMap.put("email", email);

        db.collection("users").document(userId).set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
