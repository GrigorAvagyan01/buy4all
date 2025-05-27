package com.grish.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.grish.buy4all4.databinding.ActivityMyAnouncmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;  // Import stream filter

public class MyAnouncments extends AppCompatActivity {

    private ActivityMyAnouncmentsBinding binding;
    private PostAdapterMa adapter;
    private List<Post> postList;
    private List<Post> filteredPostList;
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
        filteredPostList = new ArrayList<>();

        setupRecyclerView();
        loadPosts();

        setupSearchView();

        ImageButton backButton = findViewById(R.id.imageButtonback);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapterMa(this, filteredPostList, new PostAdapterMa.OnPostOptionsClickListener() {
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
                            loadPosts();
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
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        post.setPostId(document.getId());
                        postList.add(post);
                    }
                    filteredPostList.clear();
                    filteredPostList.addAll(postList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(MyAnouncments.this, "Failed to load posts", Toast.LENGTH_SHORT).show());
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPosts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPosts(newText);
                return true;
            }
        });
    }

    private void filterPosts(String query) {
        filteredPostList.clear();
        if (query.isEmpty()) {
            filteredPostList.addAll(postList);
        } else {
            filteredPostList.addAll(postList.stream()
                    .filter(post -> post.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
