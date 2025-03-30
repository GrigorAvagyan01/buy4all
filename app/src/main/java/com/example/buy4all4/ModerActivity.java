package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.example.buy4all4.databinding.ActivityModerBinding;
import java.util.ArrayList;
import java.util.List;

public class ModerActivity extends AppCompatActivity {

    private ActivityModerBinding binding;
    private PostAdapterMod postAdapter;
    private List<Post> allPosts;
    private List<Post> filteredPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        // Set up RecyclerView and adapter
        postAdapter = new PostAdapterMod(this, filteredPosts,
                post -> openPostDetailActivity(post),
                post -> deletePost(post)
        );

        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.postsRecyclerView.setAdapter(postAdapter);

        fetchPostsFromFirestore();
        setupSearchView();
    }

    private void fetchPostsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        allPosts.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
                            post.setPostId(document.getId());
                            allPosts.add(post);
                        }
                        updateRecyclerView();
                    } else {
                        Log.e("Firestore Error", "Error fetching posts", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore Error", "Error fetching posts", e));
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPosts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPosts(newText);
                return false;
            }
        });
    }

    private void filterPosts(String query) {
        filteredPosts.clear();
        if (query.isEmpty()) {
            filteredPosts.addAll(allPosts);
        } else {
            for (Post post : allPosts) {
                if (post.getTitle() != null && post.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredPosts.add(post);
                }
            }
        }
        postAdapter.notifyDataSetChanged();
    }

    private void openPostDetailActivity(Post post) {
        Log.d("ModerActivity", "Post clicked: " + post.getTitle());
    }

    private void deletePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (post.getPostId() == null) {
            Toast.makeText(this, "Error: Post ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference postRef = db.collection("posts").document(post.getPostId());
        DocumentReference historyRef = db.collection("history").document(post.getPostId());
        DocumentReference serviceRef = db.collection("service").document(post.getPostId());

        db.runTransaction(transaction -> {
                    transaction.delete(postRef);
                    transaction.delete(historyRef);
                    transaction.delete(serviceRef);
                    return null;
                })
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    removePostFromList(post);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting post", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore Error", "Error deleting post from collections", e);
                });
    }

    private void removePostFromList(Post post) {
        allPosts.remove(post);
        filteredPosts.remove(post);
        postAdapter.notifyDataSetChanged();
    }

    private void updateRecyclerView() {
        filteredPosts.clear();
        filteredPosts.addAll(allPosts);
        postAdapter.notifyDataSetChanged();
    }
}
