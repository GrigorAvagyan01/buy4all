package com.grish.buy4all4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.grish.buy4all4.databinding.ActivityUpdateBinding;

public class Update extends AppCompatActivity {
    private ActivityUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mail.setOnClickListener(v -> navigateTo(ChangeMailActivity.class));
        binding.username.setOnClickListener(v -> navigateTo(ChangeUsernameActivity.class));
        binding.password.setOnClickListener(v -> navigateTo(ChangePasswordActivity.class));

        binding.backupd.setOnClickListener(v -> navigateToProfileFragment());
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(Update.this, destination);
        startActivity(intent);
        finish();
    }

    private void navigateToProfileFragment() {
        Intent intent = new Intent(Update.this, ProfileFragment.class);
        intent.putExtra("openProfileFragment", true); // Add an extra to indicate ProfileFragment should be opened.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); //Clear activities on top and reuse main activity if exists.
        startActivity(intent);
        finish();
    }
}