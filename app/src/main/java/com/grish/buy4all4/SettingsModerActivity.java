package com.grish.buy4all4;

import static android.app.PendingIntent.getActivity;

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

import com.grish.buy4all4.databinding.ActivitySettingsModerBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsModerActivity extends AppCompatActivity {

    private ActivitySettingsModerBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivitySettingsModerBinding.inflate(getLayoutInflater());
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
                String currentLanguage = LocaleHelper.getSelectedLanguageCode(SettingsModerActivity.this);
                if (!selectedLanguage.equals(currentLanguage)) {
                    LocaleHelper.saveSelectedLanguage(SettingsModerActivity.this, selectedLanguage);
                    LocaleHelper.setAppLanguage(SettingsModerActivity.this);

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
        binding.logout.setOnClickListener(v -> logoutUser());
        binding.deleteAccount.setOnClickListener(v -> goToDeleteAccountActivity());
        binding.backButton.setOnClickListener(v -> goToProfileFragment());
        binding.Aboutusection.setOnClickListener(v -> {
            Intent viewItemsIntent = new Intent(SettingsModerActivity.this, AboutUs.class);
            startActivity(viewItemsIntent);
        });


        binding.moder.setOnClickListener(v -> {
            Log.d("SettingsModerActivity", "Moder button clicked");
            Intent intent = new Intent(SettingsModerActivity.this, ModerHomePage.class);
            startActivity(intent);
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent logoutIntent = new Intent(SettingsModerActivity.this, SignInActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    private void goToDeleteAccountActivity() {
        Intent intent = new Intent(SettingsModerActivity.this, DeleteAccount.class);
        startActivity(intent);
    }

    private void goToProfileFragment() {
        Intent intent = new Intent(SettingsModerActivity.this, ProfileFragmentModer.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
