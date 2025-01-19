package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    private ImageButton home2, profile2, service2, favorite2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        profile2 = findViewById(R.id.ProfileButadd2);
        home2 = findViewById(R.id.Homebutadd2);
        service2 = findViewById(R.id.serviceButadd2);
        favorite2 = findViewById(R.id.FavButadd2);

        profile2.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        home2.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        });

        service2.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, ServiceActivity.class);
            startActivity(intent);
            finish();
        });

        favorite2.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, FavoritesActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
