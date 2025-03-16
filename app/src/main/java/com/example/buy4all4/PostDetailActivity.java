package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView postImage;
    private TextView postTitle, postPrice, postDescription, postPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postImage = findViewById(R.id.postImage);
        postTitle = findViewById(R.id.postTitle);
        postPrice = findViewById(R.id.postPrice);
        postDescription = findViewById(R.id.postDescription);
        postPhone = findViewById(R.id.postPhone);

        // Get post data from Intent
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String phone = intent.getStringExtra("phone");

        // Set post data to the views
        Glide.with(this).load(imageUrl).into(postImage);
        postTitle.setText(title);
        postPrice.setText(price);
        postDescription.setText(description);
        postPhone.setText(phone);
    }
}
