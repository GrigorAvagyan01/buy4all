package com.example.buy4all4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private Button update, history, myanouncment;
    BottomNavigationView bottomNav;

    private ImageButton home, service, favorite;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        history = findViewById(R.id.HistoryBut);
        update = findViewById(R.id.UpdateBut);
        myanouncment = findViewById(R.id.MyAnouncmentsBut);
        bottomNav = findViewById(R.id.bottom_navmenu);

        bottomNav.setSelectedItemId(R.id.profile_nav);

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home_nav) {
                    return true;
                } else if (itemId == R.id.add_nav) {
                    startActivity(new Intent(ProfileActivity.this, AddActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.service_nav) {
                    startActivity(new Intent(ProfileActivity.this, ServiceActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.profile_nav) {
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.favorite_nav) {
                    startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Update.class);
                startActivity(intent);
            }
        });

        myanouncment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyAnouncments.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
