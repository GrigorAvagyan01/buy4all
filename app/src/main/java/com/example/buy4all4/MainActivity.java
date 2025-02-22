package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.splashText.setText("BUY4ALL");

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, SIgnInSignUp.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
