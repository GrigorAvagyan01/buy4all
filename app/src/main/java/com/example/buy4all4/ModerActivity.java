package com.example.buy4all4;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private Post postToDelete;  // To keep track of the selected post for deletion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityModerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        // Set up RecyclerView and adapter
        postAdapter = new PostAdapterMod(this, filteredPosts, post -> {
            openPostDetailActivity(post);
        }, post -> {
            // Handle delete action when the "Delete" option is selected from PopupMenu
            postToDelete = post;  // Save the post to delete
            deletePost(post);
        });

        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.postsRecyclerView.setAdapter(postAdapter);

        // Fetch posts from Firestore
        fetchPostsFromFirestore();

        // Set up SearchView listener
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

                        // Initially display all posts
                        filteredPosts.clear();
                        filteredPosts.addAll(allPosts);
                        postAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore Error", "Error fetching posts", task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error fetching posts", e);
                });
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
            filteredPosts.addAll(allPosts); // Show all posts if the query is empty
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
        // Handle post click, open the post detail activity (if needed)
        Log.d("ModerActivity", "Post clicked: " + post.getTitle());
    }

    // Delete the post from Firestore and the RecyclerView
    private void deletePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference postRef = db.collection("posts").document(post.getPostId());

        postRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ModerActivity.this, "Post deleted", Toast.LENGTH_SHORT).show();
                    // Remove the post from the list and update the RecyclerView
                    allPosts.remove(post);
                    filteredPosts.remove(post);
                    postAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ModerActivity.this, "Error deleting post", Toast.LENGTH_SHORT).show();
                });
    }
}
