package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ChangeMailBinding;

public class ChangeMail extends AppCompatActivity {
    private ChangeMailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChangeMailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mailchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeMail.this, "Mail updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeMail.this, ProfileFragment.class);
                startActivity(intent);
            }
        });

        binding.BackButmail.setOnClickListener(new View.OnClickListener() {
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
