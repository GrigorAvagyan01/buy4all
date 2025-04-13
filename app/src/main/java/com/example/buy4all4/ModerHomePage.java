package com.example.buy4all4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.buy4all4.databinding.ActivityModerHomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ModerHomePage extends AppCompatActivity {

    ActivityModerHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivityModerHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ModerHomeFragment())
                    .commit();
        }

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