package com.example.buy4all4;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {

    // Replace these with your Cloudinary credentials
    private static final String CLOUD_NAME = "dya4h3ime"; // Replace with your Cloudinary cloud name
    private static final String API_KEY = "249752166732665"; // Replace with your Cloudinary API key
    private static final String API_SECRET = "-QtH6X4F7Fo8ABowSQN1NkUDNHs"; // Replace with your Cloudinary API secret

    private Cloudinary cloudinary;

    public CloudinaryConfig() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }
}

