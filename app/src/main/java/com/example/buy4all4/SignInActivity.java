package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                if (email.equals("moder2@gmail.com")) {
                    startActivity(new Intent(SignInActivity.this, HomePageModer.class));
                } else {
                    startActivity(new Intent(SignInActivity.this, HomePage.class));
                }
                finish();
            }
        }

        binding.SignUpBut.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        binding.signInButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.emailEditText.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(binding.passwordEditText.getEditText()).getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignInActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignInActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            signInUser(email, password);
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            if (email.equals("moder2@gmail.com") && password.equals("Moder2")) {
                                Toast.makeText(SignInActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, HomePageModer.class));
                            } else {
                                Toast.makeText(SignInActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, HomePage.class));
                            }
                            finish();
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e("SignInActivity", "Authentication failed", e);
                        Toast.makeText(SignInActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
