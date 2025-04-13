package com.example.buy4all4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.buy4all4.databinding.ActivityMyAnouncmentsHomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyAnouncmentsHomePage extends AppCompatActivity {

    ActivityMyAnouncmentsHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivityMyAnouncmentsHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MyAnnouncementsFragment())
                    .commit();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.home_nav) {
                    selectedFragment = new MyAnnouncementsFragment1();
                } else if (itemId == R.id.service_nav) {
                    selectedFragment = new MyAnnouncementsFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleHelper.setAppLanguage(this);
    }
}
