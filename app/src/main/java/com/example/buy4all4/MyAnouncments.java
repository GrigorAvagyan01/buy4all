package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buy4all4.databinding.ActivityMyAnouncmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MyAnouncments extends AppCompatActivity {

    private ActivityMyAnouncmentsBinding binding;
    private PostAdapterMa adapter;
    private List<Post> postList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAnouncmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        postList = new ArrayList<>();

        setupRecyclerView();
        loadPosts();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapterMa(this, postList, new PostAdapterMa.OnPostOptionsClickListener() {
            @Override
            public void onEditPost(String postId) {
                Intent intent = new Intent(MyAnouncments.this, UpdateActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }

            @Override
            public void onDeletePost(String postId) {
                db.collection("posts").document(postId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(MyAnouncments.this, "Post deleted", Toast.LENGTH_SHORT).show();
                            loadPosts(); // Refresh list after deletion
                        })
                        .addOnFailureListener(e -> Toast.makeText(MyAnouncments.this, "Failed to delete post", Toast.LENGTH_SHORT).show());
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadPosts() {
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("posts")
                .whereEqualTo("userId", userId)  // Filter posts by userId
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        post.setPostId(document.getId()); // Ensure postId is set
                        postList.add(post);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(MyAnouncments.this, "Failed to load posts", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts(); // Refresh list when returning from UpdateActivity
    }
}
