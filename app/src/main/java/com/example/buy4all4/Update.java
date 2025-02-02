package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ActivityUpdateBinding;

public class Update extends AppCompatActivity {
    private ActivityUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocaleHelper.setAppLanguage(this);


        binding.mail.setOnClickListener(v -> navigateTo(ChangeMail.class));
        binding.username.setOnClickListener(v -> navigateTo(ChangeUsername.class));
        binding.password.setOnClickListener(v -> navigateTo(ChangePassword.class));
        binding.backupd.setOnClickListener(v -> {
            navigateTo(ProfileFragment.class);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(Update.this, destination);
        startActivity(intent);
        finish();
    }
}
