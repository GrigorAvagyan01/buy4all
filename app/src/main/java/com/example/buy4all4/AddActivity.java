package com.example.buy4all4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bottomNav = findViewById(R.id.bottom_navmenu);

        bottomNav.setSelectedItemId(R.id.add_nav);

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home_nav) {
                    startActivity(new Intent(AddActivity.this, HomePage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.add_nav) {
                    return true;
                } else if (itemId == R.id.service_nav) {
                    startActivity(new Intent(AddActivity.this, ServiceActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.profile_nav) {
                    startActivity(new Intent(AddActivity.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }else if (itemId == R.id.favorite_nav) {
                        startActivity(new Intent(AddActivity.this, FavoriteActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }

                return false;
            }
        });
    }
}
