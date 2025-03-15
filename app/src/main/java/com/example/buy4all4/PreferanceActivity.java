package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.buy4all4.databinding.ActivityPreferanceBinding;


public class PreferanceActivity extends AppCompatActivity {

    private ActivityPreferanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        EdgeToEdge.enable(this);



        binding = ActivityPreferanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.signupbutper.setOnClickListener(v -> navigateToHome());

        binding.main.setOnClickListener(v -> navigateToHome());

        binding.skipbutper.setOnClickListener(v -> navigateToHome());

        binding.allbutper.setOnClickListener(v -> navigateToHome());

    }

    private void navigateToHome() {
        Intent homeIntent = new Intent(PreferanceActivity.this, HomePage.class);
        startActivity(homeIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish(); 
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; 
    }
}
