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

        // Handle back button click
        binding.backButton.setOnClickListener(v -> navigateToProfileFragment());

        // Add your history-related logic here
        // ...
    }

    private void navigateToProfileFragment() {
        Intent intent = new Intent(HistoryActivity.this, ProfileFragment.class);
        intent.putExtra("openProfileFragment", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}