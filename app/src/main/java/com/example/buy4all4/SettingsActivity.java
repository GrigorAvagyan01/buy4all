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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
