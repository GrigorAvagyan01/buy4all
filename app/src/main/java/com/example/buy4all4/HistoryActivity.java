package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backhis.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, ProfileFragment.class);
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