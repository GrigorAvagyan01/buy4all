package com.grish.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.grish.buy4all4.databinding.ActivityChangeMailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeMailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityChangeMailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangeMailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.changeMailReally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        binding.gobackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToSettings();
            }
        });
    }

    private void changeEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String newEmail = binding.newMail.getText().toString().trim();
            String currentPassword = binding.currentPassword.getText().toString().trim(); // Add password field in XML

            if (TextUtils.isEmpty(newEmail)) {
                Toast.makeText(this, "Please enter a new email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }

            String currentEmail = user.getEmail();
            if (currentEmail == null) {
                Toast.makeText(this, "Error: Cannot retrieve current email", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangeMailActivity.this,
                                            "Email updated successfully.",
                                            Toast.LENGTH_SHORT).show();
                                    goBackToSettings();
                                } else {
                                    String errorMessage = task.getException().getMessage(); // Get error
                                    Toast.makeText(ChangeMailActivity.this,
                                            "Failed: " + errorMessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(ChangeMailActivity.this,
                                "Re-authentication failed: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void goBackToSettings() {
        Intent intent = new Intent(this, Update.class); // Change to your actual settings screen
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
