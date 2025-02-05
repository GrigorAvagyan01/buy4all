package com.example.buy4all4;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostModel {
    private String imageUrl, title, price, description, phone;

        public PostModel() {}

        public PostModel(String imageUrl, String title, String price, String description, String phone) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.price = price;
            this.description = description;
            this.phone = phone;
        }

        public String getImageUrl() { return imageUrl; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getDescription() { return description; }
        public String getPhone() { return phone; }
    }

