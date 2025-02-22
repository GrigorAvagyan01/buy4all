package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityMainBinding;
import com.example.buy4all4.databinding.ActivitySignInSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SIgnInSignUp extends AppCompatActivity {

    private ActivitySignInSignUpBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);

        binding = ActivitySignInSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, null);

        if (savedEmail != null && savedPassword != null) {
            Log.d("MainActivity", "Found saved credentials, attempting auto-login");
            autoLogin(savedEmail, savedPassword);
        } else {
            setupClickListeners();
        }
    }

    private void autoLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("MainActivity", "Auto-login successful, navigating to HomePage");
                        navigateToActivity(HomePage.class);
                    } else {
                        Log.d("MainActivity", "Auto-login failed, navigating to SignInActivity");
                        navigateToActivity(SignInActivity.class);
                    }
                });
    }

    private void setupClickListeners() {
        binding.signin.setOnClickListener(v -> {
            Log.d("SIgnInSignUp", "Sign In button clicked");
            navigateToActivity(SignInActivity.class);
        });

        binding.signup.setOnClickListener(v -> {
            Log.d("SIgnInSignUp", "Sign Up button clicked");
            navigateToActivity(SignUpActivity.class);
        });

        binding.guestbut.setOnClickListener(v -> {
            Log.d("SIgnInSignUp", "Guest button clicked");
            navigateToActivity(HomePageGuest.class);
        });

    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(SIgnInSignUp.this, targetActivity);
        startActivity(intent);
        finish();
    }
}
