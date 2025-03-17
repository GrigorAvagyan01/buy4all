package com.example.buy4all4;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buy4all4.databinding.ActivityUpdate2Binding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateActivity extends AppCompatActivity {
    private ActivityUpdate2Binding binding;
    private FirebaseFirestore db;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdate2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        postId = getIntent().getStringExtra("postId");

        loadPostDetails();

        binding.updateButton.setOnClickListener(v -> updatePost());
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
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load post", Toast.LENGTH_SHORT).show());
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

        DocumentReference postRef = db.collection("posts").document(postId);
        postRef.update("title", title, "description", description, "price", price, "phoneNo", phoneNo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close UpdateActivity
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show());
    }
}
