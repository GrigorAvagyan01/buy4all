package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ActivitySettingsGuestBinding;

public class SettingsActivityGuest extends AppCompatActivity {
    private ActivitySettingsGuestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsGuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backsettingsguest.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivityGuest.this, ProfileFragmentGuest.class);
            intent.putExtra("navigate_to", "ProfileFragmentGuest");
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
