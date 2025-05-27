package com.grish.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grish.buy4all4.databinding.ActivityModerHomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ModerHomePage extends AppCompatActivity {

    ActivityModerHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivityModerHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ModerHomeFragment())
                    .commit();
        }

        // Bottom navigation item selection
        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.home_nav) {
                    selectedFragment = new ModerHomeFragment();
                } else if (itemId == R.id.service_nav) {
                    selectedFragment = new ModerServiceFragment();
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
