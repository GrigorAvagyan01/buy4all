package com.example.buy4all4;

public class Post {
    private String title;
    private String price;
    private String imageUrl;

    public Post() {
    }

    public Post(String title, String price, String imageUrl) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
