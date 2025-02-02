package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ChangeUsernameBinding;

public class ChangeUsername extends AppCompatActivity {
    private ChangeUsernameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChangeUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocaleHelper.setAppLanguage(this);


        binding.uschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeUsername.this, "Username updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeUsername.this, ProfileFragment.class);
                startActivity(intent);
            }
        });

        binding.backbutuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
