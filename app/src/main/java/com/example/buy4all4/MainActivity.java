package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.signin.setOnClickListener(v -> {
            Log.d("MainActivity", "Sign In button clicked");
            navigateToActivity(SignInActivity.class);
        });

        binding.signup.setOnClickListener(v -> {
            Log.d("MainActivity", "Sign Up button clicked");
            navigateToActivity(SignUpActivity.class);
        });

        binding.guestbut.setOnClickListener(v -> {
            Log.d("MainActivity", "Guest button clicked");
            navigateToActivity(HomePageGuest.class);
        });
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
    }
}
