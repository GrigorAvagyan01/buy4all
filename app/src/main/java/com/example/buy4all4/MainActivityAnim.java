package com.example.buy4all4;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.example.buy4all4.databinding.ActivityMainAnimBinding; // Import the generated ViewBinding class

public class MainActivityAnim extends AppCompatActivity {

    private ActivityMainAnimBinding binding; // Declare the binding variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityMainAnimBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        // Apply window insets handling
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load animations
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        // Apply animations
        binding.buy4all.setAnimation(topAnim);

        long splashScreenDuration = 2500;

        // Handle delayed task after splash screen
        new Handler().postDelayed(() -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                // If the user is authenticated, go to the MainActivity
                Intent intent = new Intent(MainActivityAnim.this, MainActivity.class);
                startActivity(intent);
            } else {
                // If the user is not authenticated, create a scene transition
                Intent intent = new Intent(MainActivityAnim.this, MainActivity.class);

                // Define the pairs for the shared element transition
                Pair[] pairs = new Pair[1]; // Only one pair for this transition
                pairs[0] = new Pair<>(binding.buy4all, "logo_image"); // Shared element transition

                // Create the activity options with the shared element transition
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivityAnim.this, pairs);
                startActivity(intent, options.toBundle());
            }
            finish(); // Finish the current activity
        }, splashScreenDuration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Set binding to null when the activity is destroyed to avoid memory leaks
    }
}
