package com.grish.buy4all4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.grish.buy4all4.databinding.ActivityPostDetailBinding;

public class ServiceDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String serviceId = intent.getStringExtra("serviceId");
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String phone = intent.getStringExtra("phone");

        binding.postTitle.setText(title);
        binding.postPrice.setText(formatPrice(price));
        binding.postDescription.setText(description);
        binding.postPhone.setText(phone);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(binding.postImage);
        }

    }

    private String formatPrice(String price) {
        if (price == null || price.isEmpty()) {
            return "No Price";
        }
        return "$ " + price;
    }

    private void initiateCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(android.net.Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
