package com.grish.buy4all4;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {
    private String serviceId;
    private String title;
    private String description;
    private String price;
    private String currency;
    private String phoneNo;
    private String userId;
    private String imageUrl;

    public Service() {}

    public Service(String serviceId, String title, String description, String price, String currency,
                   String phoneNo, String userId, String imageUrl) {
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.phoneNo = phoneNo;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    protected Service(Parcel in) {
        serviceId = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        currency = in.readString();
        phoneNo = in.readString();
        userId = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(serviceId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(currency);
        parcel.writeString(phoneNo);
        parcel.writeString(userId);
        parcel.writeString(imageUrl);
    }
}
