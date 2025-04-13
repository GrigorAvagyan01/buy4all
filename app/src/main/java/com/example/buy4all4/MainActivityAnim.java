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

    private ActivityMainAnimBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainAnimBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        binding.buy4all.setAnimation(topAnim);

        long splashScreenDuration = 2500;

        new Handler().postDelayed(() -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivityAnim.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivityAnim.this, MainActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<>(binding.buy4all, "logo_image");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivityAnim.this, pairs);
                startActivity(intent, options.toBundle());
            }
            finish();
        }, splashScreenDuration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
