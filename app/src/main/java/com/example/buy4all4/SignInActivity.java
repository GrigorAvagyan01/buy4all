package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocaleHelper.setAppLanguage(this);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadSavedCredentials();

        binding.SignUpBut.setOnClickListener(v -> {
            Log.d("SignInActivity", "Sign Up button clicked");
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
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

            if (binding.RememberMeSignIns.isChecked()) {
                saveCredentials(email, password, true);
            } else {
                clearSavedCredentials();
            }

            signInUser(email, password);
        });
    }

    private void loadSavedCredentials() {
        boolean remember = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (remember) {
            String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

            binding.emailEditText.getEditText().setText(savedEmail);
            binding.passwordEditText.getEditText().setText(savedPassword);
            binding.RememberMeSignIns.setChecked(true);
        }
    }

    private void saveCredentials(String email, String password, boolean remember) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, remember);
        editor.apply();
    }

    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_REMEMBER);
        editor.apply();
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("SignInActivity", "Sign-in successful, transitioning to HomePage");
                            Toast.makeText(SignInActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, HomePage.class));
                            finish();
                        }
                    } else {
                        Log.e("SignInActivity", "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(SignInActivity.this, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
