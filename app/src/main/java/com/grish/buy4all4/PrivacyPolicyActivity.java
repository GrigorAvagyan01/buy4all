package com.grish.buy4all4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.grish.buy4all4.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ad Posting Rules");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
