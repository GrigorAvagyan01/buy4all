package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityDeleteAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityDeleteAccountBinding binding; // View Binding

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Set click listeners using binding
        binding.deleteAccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            clearRememberMeFlag();

                            Toast.makeText(DeleteAccount.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DeleteAccount.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DeleteAccount.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearRememberMeFlag() {
        SharedPreferences sharedPref = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("rememberMe", false).apply();
    }
}