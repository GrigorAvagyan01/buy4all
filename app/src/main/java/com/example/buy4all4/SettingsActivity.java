package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backsettings.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileFragment.class);
            intent.putExtra("navigate_to", "ProfileFragment");
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
            Intent logoutIntent = new Intent(SettingsActivity.this, MainActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
