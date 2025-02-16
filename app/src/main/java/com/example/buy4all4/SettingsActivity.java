package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String LANGUAGE_KEY = "language";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Apply the saved language
        LocaleHelper.setAppLanguage(this);

        setupLanguageSpinner();
        setupButtons();
    }

    private void setupLanguageSpinner() {
        String savedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "en");

        Spinner languageSpinner = binding.languageSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set spinner selection based on saved language
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
                String selectedLanguage = position == 0 ? "hy" : (position == 1 ? "ru" : "en");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LANGUAGE_KEY, selectedLanguage);
                editor.apply();

                LocaleHelper.setAppLanguage(SettingsActivity.this, selectedLanguage);
                restartActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void setupButtons() {
        binding.logout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!rememberMe) {
            editor.clear();
        }

        editor.apply();

        Intent logoutIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
