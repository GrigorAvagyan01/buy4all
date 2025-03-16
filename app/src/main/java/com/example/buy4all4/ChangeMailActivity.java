package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.buy4all4.databinding.ActivityChangeMailBinding;

import java.util.List;

public class ChangeMailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityChangeMailBinding binding; // View Binding object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the View Binding
        binding = ActivityChangeMailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Set the click listener for the change email button
        binding.changeMailReally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMail();
            }
        });

        // Set the click listener for going back to settings

    }

    private void changeMail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String newEmail = binding.newMail.getText().toString().trim();

            // Check if email is empty
            if (TextUtils.isEmpty(newEmail)) {
                Toast.makeText(this, "Please enter a new email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate the email format
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reauthenticate the user if needed
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), "yourPassword"); // Replace "yourPassword" with the actual password
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Check if the email is already in use
                        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                        if (task.isSuccessful()) {
                                            List<String> signInMethods = task.getResult().getSignInMethods();
                                            if (signInMethods != null && signInMethods.size() > 0) {
                                                Toast.makeText(ChangeMailActivity.this, "Email is already in use", Toast.LENGTH_SHORT).show();
                                            } else {
                                                updateEmailInFirebase(user, newEmail);
                                            }
                                        } else {
                                            Toast.makeText(ChangeMailActivity.this, "Failed to check email usage", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ChangeMailActivity.this, "Reauthentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateEmailInFirebase(FirebaseUser user, String newEmail) {
        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeMailActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                            goToProfile();
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                String errorMessage = exception.getMessage();
                                Toast.makeText(ChangeMailActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangeMailActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToProfile() {
        // Navigate to the profile screen or home screen after successful email change
        // Replace ProfileActivity with your actual profile activity or fragment
        Intent intent = new Intent(this, ProfileFragment.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up view binding when the activity is destroyed
        binding = null;
    }
}
