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

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String LANGUAGE_KEY = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocaleHelper.setAppLanguage(this);

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

        binding.backsettings.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileFragment.class);
            intent.putExtra("navigate_to", "ProfileFragment");
            startActivity(intent);
            finish();
        });

        binding.roompreference.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PreferanceActivitySettings.class);
            intent.putExtra("navigate_to", "PreferanceActivitySettings");
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });

        binding.logout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("email");
            editor.remove("password");
            editor.apply();

            Intent logoutIntent = new Intent(SettingsActivity.this, MainActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
            finish();
        });
    }

    private void setAppLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
