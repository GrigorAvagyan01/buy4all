package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String LANGUAGE_KEY = "language";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String savedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "en");
        setAppLocale(savedLanguage);

        Spinner languageSpinner = binding.languageSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        if ("hy".equals(savedLanguage)) {
            languageSpinner.setSelection(0);
        } else if ("ru".equals(savedLanguage)) {
            languageSpinner.setSelection(1);
        } else {
            languageSpinner.setSelection(2);
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = "en";
                switch (position) {
                    case 0:
                        selectedLanguage = "hy";
                        break;
                    case 1:
                        selectedLanguage = "ru";
                        break;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LANGUAGE_KEY, selectedLanguage);
                editor.apply();

                setAppLocale(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

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
        finish();
    }

    private void setAppLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }
}
