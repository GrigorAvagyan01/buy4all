package com.example.buy4all4;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String postId;
    private String title;
    private String description;
    private String price;
    private String currency;
    private String phoneNo;
    private String userId;
    private String imageUrl;
    private boolean isFavorite;
    private String category;

    public Post() {}

    public Post(String postId, String title, String description, String price, String currency,
                String phoneNo, String userId, String imageUrl, boolean isFavorite, String category) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.phoneNo = phoneNo;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
        this.category = category;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        currency = in.readString();
        phoneNo = in.readString();
        userId = in.readString();
        imageUrl = in.readString();
        isFavorite = in.readByte() != 0;
        category = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(currency);
        parcel.writeString(phoneNo);
        parcel.writeString(userId);
        parcel.writeString(imageUrl);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeString(category);
    }
}
