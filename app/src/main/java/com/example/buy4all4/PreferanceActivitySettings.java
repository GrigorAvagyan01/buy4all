package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.buy4all4.databinding.ActivityPreferanceBinding;
import com.example.buy4all4.databinding.ActivityPreferanceSettingsBinding;


public class PreferanceActivitySettings extends AppCompatActivity {

    private ActivityPreferanceSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        LocaleHelper.setAppLanguage(this);



        binding = ActivityPreferanceSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.updatebutperset.setOnClickListener(v -> navigateToHome());

        binding.main.setOnClickListener(v -> navigateToHome());

        binding.skipbutperset.setOnClickListener(v -> navigateToHome());

        binding.allbutperset.setOnClickListener(v -> navigateToHome());

    }

    private void navigateToHome() {
        Intent homeIntent = new Intent(PreferanceActivitySettings.this, SettingsActivity.class);
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
