package com.example.buy4all4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeletePostActivity extends AppCompatActivity {

    private String postId;
    private String userId;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post);

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get postId and userId from Intent
        postId = getIntent().getStringExtra("postId");
        userId = getIntent().getStringExtra("userId");

        // Set up the delete button
        Button btnDeletePost = findViewById(R.id.btnDeletePost);
        btnDeletePost.setOnClickListener(v -> deletePost());
    }

    private void deletePost() {
        if (postId != null && userId != null && currentUser != null) {
            if (userId.equals(currentUser.getUid())) {  // Check if the current user is the owner
                firestore.collection("posts").document(postId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DeletePostActivity.this, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DeletePostActivity.this, "Failed to delete post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(DeletePostActivity.this, "You cannot delete this post.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
