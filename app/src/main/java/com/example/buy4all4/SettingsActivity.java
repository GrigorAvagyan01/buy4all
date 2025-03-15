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
    private static final String LANGUAGE_KEY = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);  // Apply selected language on activity creation
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String language = LocaleHelper.getSelectedLanguageCode(this);  // Get the saved language

        setupLanguageSpinner(language);  // Initialize spinner with selected language
        setupButtons();  // Setup button click actions
    }

    private void setupLanguageSpinner(String savedLanguage) {
        Spinner languageSpinner = binding.languageSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set the initial selection based on saved language
        if ("hy".equals(savedLanguage)) {
            languageSpinner.setSelection(2);
        } else if ("ru".equals(savedLanguage)) {
            languageSpinner.setSelection(1);
        } else {
            languageSpinner.setSelection(0);
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = position == 0 ? "en" : (position == 1 ? "ru" : "hy");

                // Only save and apply the language if it's different from the current one
                String currentLanguage = LocaleHelper.getSelectedLanguageCode(SettingsActivity.this);
                if (!selectedLanguage.equals(currentLanguage)) {
                    // Save selected language
                    LocaleHelper.saveSelectedLanguage(SettingsActivity.this, selectedLanguage);
                    LocaleHelper.setAppLanguage(SettingsActivity.this);  // Apply the selected language

                    // Restart the activity only once to apply the change
                    Intent intent = getIntent();
                    finish();  // This will finish the SettingsActivity and return to ProfileFragment
                    startActivity(intent);
                }
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

        // Clear user session data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to MainActivity
        Intent logoutIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    // Override the back button behavior to ensure we navigate back to ProfileFragment
    @Override
    public void onBackPressed() {
        // Instead of just finishing the activity, we use finish() to go back to the previous fragment
        super.onBackPressed();
    }
}
