package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ActivityPostDetailBinding;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String phone = intent.getStringExtra("phone");

        Glide.with(this).load(imageUrl).into(binding.postImage);
        binding.postTitle.setText(title);
        binding.postPrice.setText("Price: " + price + "֏");
        binding.postDescription.setText("Description: " + description);
        binding.postPhone.setText("Phone No: " + phone);
        binding.backbutproffrag.setOnClickListener(v -> {
            Intent intent1 = new Intent(PostDetailActivity.this, HomeFragment.class);
            startActivity(intent1);
            finish();
        });

    }
}