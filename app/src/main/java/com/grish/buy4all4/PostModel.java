package com.grish.buy4all4;

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

