package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityMyAnouncmentsBinding;

public class MyAnouncments extends AppCompatActivity {

    private ActivityMyAnouncmentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAnouncmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyAnouncments.this, ProfileFragment.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
