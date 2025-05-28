package com.grish.buy4all4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.grish.buy4all4.databinding.ActivityRulesBinding;

public class RulesActivity extends AppCompatActivity {

    private ActivityRulesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRulesBinding.inflate(getLayoutInflater());
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
