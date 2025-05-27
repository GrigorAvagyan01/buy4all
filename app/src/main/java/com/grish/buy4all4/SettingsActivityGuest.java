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

import com.grish.buy4all4.databinding.ActivitySettingsGuestBinding;

public class SettingsActivityGuest extends AppCompatActivity {

    private ActivitySettingsGuestBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivitySettingsGuestBinding.inflate(getLayoutInflater());
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
                String currentLanguage = LocaleHelper.getSelectedLanguageCode(SettingsActivityGuest.this);
                if (!selectedLanguage.equals(currentLanguage)) {
                    LocaleHelper.saveSelectedLanguage(SettingsActivityGuest.this, selectedLanguage);
                    LocaleHelper.setAppLanguage(SettingsActivityGuest.this);

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
        binding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivityGuest.this, ProfileFragmentGuest.class);
            intent.putExtra("navigate_to", "ProfileFragmentGuest");
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
        binding.Aboutusection.setOnClickListener(v -> {
            Intent viewItemsIntent = new Intent(SettingsActivityGuest.this, AboutUs.class);
            startActivity(viewItemsIntent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
