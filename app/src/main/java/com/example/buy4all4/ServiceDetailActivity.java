package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ActivityPostDetailBinding;

public class ServiceDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the data passed from the adapter
        Intent intent = getIntent();
        String serviceId = intent.getStringExtra("serviceId");
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String phone = intent.getStringExtra("phone");

        // Set the data to views
        binding.postTitle.setText(title);
        binding.postPrice.setText(formatPrice(price));
        binding.postDescription.setText(description);
        binding.postPhone.setText(phone);

        // Load the service image using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(binding.postImage);
        }

    }

    private String formatPrice(String price) {
        if (price == null || price.isEmpty()) {
            return "No Price";
        }
        return "$ " + price; // Assuming default as USD. Modify as needed.
    }

    private void initiateCall(String phoneNumber) {
        // Code to initiate a call
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(android.net.Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
