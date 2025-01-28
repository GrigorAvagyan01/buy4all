package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ChangePasswordBinding;

public class ChangePassword extends AppCompatActivity {
    private ChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.passchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangePassword.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassword.this, ProfileFragment.class);
                startActivity(intent);
            }
        });

        binding.BackButpass.setOnClickListener(new View.OnClickListener() {
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
