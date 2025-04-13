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
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        postImage = findViewById(R.id.postImage);
        postTitle = findViewById(R.id.postTitle);
        postPrice = findViewById(R.id.postPrice);
        postDescription = findViewById(R.id.postDescription);
        postPhone = findViewById(R.id.postPhone);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String phone = intent.getStringExtra("phone");

        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(postImage);
        }
        postTitle.setText(title != null ? title : "No Title");
        postPrice.setText(price != null ? price : "No Price");
        postDescription.setText(description != null ? description : "No Description");
        postPhone.setText(phone != null ? phone : "No Phone");

        if (mAuth.getCurrentUser() != null && postId != null) {
            String userId = mAuth.getCurrentUser().getUid();
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
        historyPost.put("timestamp", System.currentTimeMillis());

        db.collection("users")
                .document(userId)
                .collection("history")
                .document(postId)
                .set(historyPost)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }
}
