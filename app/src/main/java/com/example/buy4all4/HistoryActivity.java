package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView; // Import correct SearchView
import com.example.buy4all4.databinding.ActivityHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private PostAdapter adapter;
    private List<Post> historyList = new ArrayList<>();
    private List<Post> filteredHistoryList = new ArrayList<>(); // List for search filtering
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter with filtered list
        adapter = new PostAdapter(this, filteredHistoryList, null, null);
        binding.recyclerViewPosts.setAdapter(adapter);

        if (mAuth.getCurrentUser() != null) {
            loadHistory();
        } else {
            Log.e("HistoryActivity", "User not logged in");
        }

        setupSearchView(); // Call method to set up search
    }

    private void loadHistory() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("history")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    historyList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        historyList.add(post);
                    }
                    // Copy all history to filtered list initially
                    filteredHistoryList.clear();
                    filteredHistoryList.addAll(historyList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HistoryActivity", "Error loading history", e));
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterHistory(newText);
                return true;
            }
        });
    }

    private void filterHistory(String query) {
        filteredHistoryList.clear();
        if (query.isEmpty()) {
            filteredHistoryList.addAll(historyList);
        } else {
            filteredHistoryList.addAll(historyList.stream()
                    .filter(post -> post.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        adapter.notifyDataSetChanged();
    }

    private void addPostToHistory(Post post) {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference historyRef = db.collection("users")
                .document(userId)
                .collection("history")
                .document(post.getPostId());

        historyRef.set(post)
                .addOnSuccessListener(aVoid -> Log.d("HistoryActivity", "Post added to history"))
                .addOnFailureListener(e -> Log.e("HistoryActivity", "Error adding post to history", e));
    }
}
