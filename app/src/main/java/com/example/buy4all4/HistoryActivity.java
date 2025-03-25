package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buy4all4.databinding.ActivityHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private PostAdapter adapter;
    private List<Post> historyList = new ArrayList<>();
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
        adapter = new PostAdapter(this, historyList, null, null);
        binding.recyclerViewPosts.setAdapter(adapter);

        if (mAuth.getCurrentUser() != null) {
            loadHistory();
        } else {
            Log.e("HistoryActivity", "User not logged in");
        }
    }

    private void loadHistory() {
        String userId = mAuth.getCurrentUser().getUid(); // Get current user's ID

        // Query the user's specific history collection
        db.collection("users")
                .document(userId) // Reference the current user's document
                .collection("history") // History collection under the user
                .orderBy("timestamp", Query.Direction.DESCENDING) // Show newest first
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    historyList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        historyList.add(post);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HistoryActivity", "Error loading history", e));
    }

    private void addPostToHistory(Post post) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid(); // Get the user ID

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Reference to the user's 'history' collection
        DocumentReference historyRef = db.collection("users")
                .document(userId) // Reference the current user's document
                .collection("history") // History subcollection
                .document(post.getPostId()); // Post ID as the document ID

        // Save the post to the history collection
        historyRef.set(post)
                .addOnSuccessListener(aVoid -> Log.d("HistoryActivity", "Post added to history"))
                .addOnFailureListener(e -> Log.e("HistoryActivity", "Error adding post to history", e));
    }
}
