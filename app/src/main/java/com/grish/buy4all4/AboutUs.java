package com.grish.buy4all4;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setTitle("About Us");

        // Back button click listener
        ImageView backButton = findViewById(R.id.imageViewBack);
        backButton.setOnClickListener(v -> finish());
    }
}
