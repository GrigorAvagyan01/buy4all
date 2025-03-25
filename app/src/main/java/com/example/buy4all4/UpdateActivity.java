package com.example.buy4all4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.callback.ErrorInfo;
import com.example.buy4all4.databinding.ActivityUpdate2Binding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    private ActivityUpdate2Binding binding;
    private FirebaseFirestore db;
    private String postId;
    private String newImageUrl; // Variable to store the new image URL
    private Uri selectedImageUri; // To store the selected image URI
    private boolean isImageUpdated = false; // Flag to track image upload status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdate2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        postId = getIntent().getStringExtra("postId");

        loadPostDetails();

        binding.updateButton.setOnClickListener(v -> updatePost());
        binding.updateImage.setOnClickListener(v -> chooseImage()); // Button to select image
    }

    private void loadPostDetails() {
        if (postId == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("posts").document(postId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        binding.updateTitle.setText(documentSnapshot.getString("title"));
                        binding.updateDesc.setText(documentSnapshot.getString("description"));
                        binding.updatePrice.setText(documentSnapshot.getString("price"));
                        binding.updatephoneno.setText(documentSnapshot.getString("phoneNo"));
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            // Load the existing image if present
                            Glide.with(this).load(imageUrl).into(binding.updateImage);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load post", Toast.LENGTH_SHORT).show());
    }

    private void chooseImage() {
        // Open an image picker or gallery to let the user select an image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1); // 1 is a request code for image selection
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            selectedImageUri = data.getData();
            // Update the ImageView with the selected image
            binding.updateImage.setImageURI(selectedImageUri);
            isImageUpdated = true; // Set the flag to true when the image is selected
        }
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        // Initialize Cloudinary
        Cloudinary cloudinary = MediaManager.get().getCloudinary();

        // Upload the image to Cloudinary
        MediaManager.get().upload(imageUri).option("public_id", "post_images/" + postId)
                .callback(new com.cloudinary.android.callback.UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // Upload started
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Upload in progress
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Image upload successful, get the URL
                        newImageUrl = (String) resultData.get("secure_url");
                        isImageUpdated = true; // Set the flag to true when the image upload is successful
                        Toast.makeText(UpdateActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(UpdateActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Retry logic can be added here
                    }
                }).dispatch();
    }

    private void updatePost() {
        String title = binding.updateTitle.getText().toString().trim();
        String description = binding.updateDesc.getText().toString().trim();
        String price = binding.updatePrice.getText().toString().trim();
        String phoneNo = binding.updatephoneno.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price) || TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // If a new image was selected, upload it first
        if (isImageUpdated && selectedImageUri != null) {
            uploadImageToCloudinary(selectedImageUri);
        }

        // Proceed to update the post in Firestore once image is uploaded (if necessary)
        if (isImageUpdated && newImageUrl != null) {
            DocumentReference postRef = db.collection("posts").document(postId);
            HashMap<String, Object> postMap = new HashMap<>();
            postMap.put("title", title);
            postMap.put("description", description);
            postMap.put("price", price);
            postMap.put("phoneNo", phoneNo);

            // If there is a new image URL, update the image field in Firestore
            postMap.put("imageUrl", newImageUrl);

            postRef.update(postMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UpdateActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close UpdateActivity
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show());
        } else {
            // If no image is updated, just update the other fields
            DocumentReference postRef = db.collection("posts").document(postId);
            HashMap<String, Object> postMap = new HashMap<>();
            postMap.put("title", title);
            postMap.put("description", description);
            postMap.put("price", price);
            postMap.put("phoneNo", phoneNo);

            postRef.update(postMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UpdateActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close UpdateActivity
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show());
        }
    }
}
