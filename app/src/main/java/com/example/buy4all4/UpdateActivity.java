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
    private String newImageUrl;
    private Uri selectedImageUri;
    private boolean isImageUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdate2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        postId = getIntent().getStringExtra("postId");

        loadPostDetails();

        binding.updateButton.setOnClickListener(v -> updatePost());
        binding.updateImage.setOnClickListener(v -> chooseImage());
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
                            Glide.with(this).load(imageUrl).into(binding.updateImage);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load post", Toast.LENGTH_SHORT).show());
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            binding.updateImage.setImageURI(selectedImageUri);
            isImageUpdated = true;
        }
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        Cloudinary cloudinary = MediaManager.get().getCloudinary();

        MediaManager.get().upload(imageUri).option("public_id", "post_images/" + postId)
                .callback(new com.cloudinary.android.callback.UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        newImageUrl = (String) resultData.get("secure_url");
                        isImageUpdated = true;
                        Toast.makeText(UpdateActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(UpdateActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
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

        if (isImageUpdated && selectedImageUri != null) {
            uploadImageToCloudinary(selectedImageUri);
        }

        if (isImageUpdated && newImageUrl != null) {
            DocumentReference postRef = db.collection("posts").document(postId);
            HashMap<String, Object> postMap = new HashMap<>();
            postMap.put("title", title);
            postMap.put("description", description);
            postMap.put("price", price);
            postMap.put("phoneNo", phoneNo);

            postMap.put("imageUrl", newImageUrl);

            postRef.update(postMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UpdateActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show());
        } else {
            DocumentReference postRef = db.collection("posts").document(postId);
            HashMap<String, Object> postMap = new HashMap<>();
            postMap.put("title", title);
            postMap.put("description", description);
            postMap.put("price", price);
            postMap.put("phoneNo", phoneNo);

            postRef.update(postMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UpdateActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show());
        }
    }
}
