package com.grish.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.grish.buy4all4.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        binding.Signinbut.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, SignInActivity.class)));

        binding.signupButton.setOnClickListener(v -> registerUser());

        // Guest login
        binding.guestbut.setOnClickListener(v -> {
            Log.d("SignUpActivity", "Guest button clicked");
            startActivity(new Intent(SignUpActivity.this, HomePageGuest.class));
            finish();
        });

        // Test user auto-login
        binding.testUserButton.setOnClickListener(v -> {
            Log.d("SignUpActivity", "Test User button clicked");
            autoLogin("testuser@gmail.com", "Testuser");
        });
    }

    private void registerUser() {
        String username = Objects.requireNonNull(binding.username.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(binding.emailEditText.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(binding.passwordEditText.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.passwordEditTextc.getEditText()).getText().toString().trim();

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
            binding.emailEditText.setError("Email is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.setError("Password is required");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            binding.passwordEditTextc.setError("Passwords do not match");
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
                    startActivity(new Intent(SignUpActivity.this, EmailVerify.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void autoLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "Test User login successful");
                        saveCredentials(email, password);
                        startActivity(new Intent(SignUpActivity.this, HomePage.class));
                        finish();
                    } else {
                        Log.e("SignUpActivity", "Test User login failed", task.getException());
                        Toast.makeText(this, "Test user login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }
}
