package com.example.buy4all4;

public class Post {
    private String title;
    private String description;
    private String price;
    private String phoneNo;
    private String imagePath;

    // Default constructor
    public Post() {}

    // Constructor
    public Post(String title, String description, String price, String phoneNo, String imagePath) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.phoneNo = phoneNo;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
