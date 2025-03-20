package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView postImage;
    private TextView postTitle, postPrice, postDescription, postPhone;
    private FirebaseFirestore db;
    private String postId; // Store post ID for history tracking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Initialize views
        postImage = findViewById(R.id.postImage);
        postTitle = findViewById(R.id.postTitle);
        postPrice = findViewById(R.id.postPrice);
        postDescription = findViewById(R.id.postDescription);
        postPhone = findViewById(R.id.postPhone);

        // Get post data from Intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String phone = intent.getStringExtra("phone");

        // Set post data to views
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(postImage);
        }
        postTitle.setText(title != null ? title : "No Title");
        postPrice.setText(price != null ? price : "No Price");
        postDescription.setText(description != null ? description : "No Description");
        postPhone.setText(phone != null ? phone : "No Phone");

        // Save to history (only if user is authenticated)
        if (mAuth.getCurrentUser() != null && postId != null) {
            String userId = mAuth.getCurrentUser().getUid(); // Get the current user's ID
            saveToHistory(userId, postId, title, description, price, phone, imageUrl);
        }
    }

    private void saveToHistory(String userId, String postId, String title, String description, String price, String phone, String imageUrl) {
        Map<String, Object> historyPost = new HashMap<>();
        historyPost.put("postId", postId);
        historyPost.put("title", title);
        historyPost.put("description", description);
        historyPost.put("price", price);
        historyPost.put("phone", phone);
        historyPost.put("imageUrl", imageUrl);
        historyPost.put("timestamp", System.currentTimeMillis()); // Store timestamp for sorting

        // Reference to the user's history collection
        db.collection("users")
                .document(userId) // Reference the current user's document
                .collection("history") // History subcollection under the user
                .document(postId) // Use the postId as the document ID to avoid duplicates
                .set(historyPost) // Set the post data in history
                .addOnSuccessListener(aVoid -> {
                    // Successfully saved to history
                    // You can log this or show a message
                })
                .addOnFailureListener(e -> {
                    // Log failure
                    // You can show an error message to the user if needed
                });
    }
}
