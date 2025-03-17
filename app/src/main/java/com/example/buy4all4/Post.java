package com.example.buy4all4;

public class Post {
    private String postId;
    private String title;
    private String description;
    private String price;
    private String phoneNo;
    private String userId;
    private String imageUrl;

    public Post() {}

    public Post(String postId, String title, String description, String price, String phoneNo, String userId, String imageUrl) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.phoneNo = phoneNo;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
