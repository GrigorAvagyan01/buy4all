package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buy4all4.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.changePasswordReally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        binding.gobackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToSettings();
            }
        });
    }

    private void changePassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String currentPassword = binding.currentPassword.getText().toString().trim();
            String newPassword = binding.newPassword.getText().toString().trim();

            if (TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(newPassword)) {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = user.getEmail();
            if (userEmail == null) {
                Toast.makeText(this, "Error: Cannot retrieve email", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Step 2: Update password after successful re-authentication
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePasswordActivity.this,
                                            "Password updated successfully",
                                            Toast.LENGTH_SHORT).show();
                                    goToProfile();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(ChangePasswordActivity.this,
                                            "Failed: " + errorMessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(ChangePasswordActivity.this,
                                "Re-authentication failed: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void goToProfile() {
        Intent intent = new Intent(this, ProfileFragment.class);
        finish();
    }

    private void goBackToSettings() {
        Intent intent = new Intent(this, Update.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
