package com.grish.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.grish.buy4all4.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String language = LocaleHelper.getSelectedLanguageCode(this);

        setupLanguageSpinner(language);
        setupButtons();
    }

    private void setupLanguageSpinner(String savedLanguage) {
        Spinner languageSpinner = binding.languageSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

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
                String currentLanguage = LocaleHelper.getSelectedLanguageCode(SettingsActivity.this);
                if (!selectedLanguage.equals(currentLanguage)) {
                    LocaleHelper.saveSelectedLanguage(SettingsActivity.this, selectedLanguage);
                    LocaleHelper.setAppLanguage(SettingsActivity.this);

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void setupButtons() {
        binding.logouttext.setOnClickListener(v -> logoutUser());
        binding.deleteAccount.setOnClickListener(v -> goToDeleteAccountActivity());
        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.Aboutusection.setOnClickListener(v -> {
            Intent viewItemsIntent = new Intent(SettingsActivity.this, AboutUs.class);
            startActivity(viewItemsIntent);
        });
        binding.RulesSection.setOnClickListener(v -> {
            Intent viewItemsIntent = new Intent(SettingsActivity.this, RulesActivity.class);
            startActivity(viewItemsIntent);
        });
        binding.PrivacySection.setOnClickListener(v -> {
            Intent viewItemsIntent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(viewItemsIntent);
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent logoutIntent = new Intent(SettingsActivity.this, SignInActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    private void goToDeleteAccountActivity() {
        Intent intent = new Intent(SettingsActivity.this, DeleteAccount.class);
        startActivity(intent);
    }
}
