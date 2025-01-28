package com.example.buy4all4;

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

        binding.SignUpBut.setOnClickListener(v -> {
            Log.d("SignInActivity", "Sign Up button clicked");
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.signInButton.setOnClickListener(v -> {
            Log.d("SignInActivity", "Sign In button clicked");
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
                            Log.d("SignInActivity", "Sign-in successful, transitioning to HomePage");
                            Toast.makeText(SignInActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e("SignInActivity", "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(SignInActivity.this, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
