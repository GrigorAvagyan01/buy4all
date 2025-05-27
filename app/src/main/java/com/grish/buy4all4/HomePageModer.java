package com.grish.buy4all4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.grish.buy4all4.databinding.ActivityHomePageBinding;
import com.grish.buy4all4.databinding.ActivityHomePageModerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageModer extends AppCompatActivity {

    ActivityHomePageModerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setAppLanguage(this);
        binding = ActivityHomePageModerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }



        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.home_nav:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.service_nav:
                        selectedFragment = new ServiceFragment();
                        break;
                    case R.id.add_nav:
                        new AddOptionsBottomSheet().show(getSupportFragmentManager(), "AddOptionsBottomSheet");
                        return true;
                    case R.id.favorite_nav:
                        selectedFragment = new FavoriteFragment();
                        break;
                    case R.id.profile_nav:
                        selectedFragment = new ProfileFragmentModer();
                        break;
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
        System.out.println("jfnvj");
        LocaleHelper.setAppLanguage(this);
    }
}