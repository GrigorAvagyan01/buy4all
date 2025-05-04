package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;

import com.example.buy4all4.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private PostAdapter adapter;
    private List<Post> historyList = new ArrayList<>();
    private List<Post> filteredHistoryList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set up RecyclerView with adapter and layout manager
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(getContext(), filteredHistoryList, null, null);
        binding.recyclerViewPosts.setAdapter(adapter);

        // Load history if user is logged in
        if (mAuth.getCurrentUser() != null) {
            loadHistory();
        } else {
            Log.e("HistoryFragment", "User not logged in");
        }

        // Set up the search functionality
        setupSearchView();

        // Set up clear history button click listener
        binding.clearHistoryButton.setOnClickListener(v -> clearHistory());

        // Back button functionality
        binding.backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return binding.getRoot();
    }

    private void loadHistory() {
        String userId = mAuth.getCurrentUser().getUid();

        // Fetch history posts for the logged-in user
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
                    filteredHistoryList.clear();
                    filteredHistoryList.addAll(historyList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HistoryFragment", "Error loading history", e));
    }

    private void clearHistory() {
        String userId = mAuth.getCurrentUser().getUid();

        // Clear the user's history collection in Firestore
        db.collection("users")
                .document(userId)
                .collection("history")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().delete();
                    }
                    historyList.clear();
                    filteredHistoryList.clear();
                    adapter.notifyDataSetChanged();
                    Log.d("HistoryFragment", "History cleared");
                })
                .addOnFailureListener(e -> Log.e("HistoryFragment", "Error clearing history", e));
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

        // Add post to user's history in Firestore
        historyRef.set(post)
                .addOnSuccessListener(aVoid -> Log.d("HistoryFragment", "Post added to history"))
                .addOnFailureListener(e -> Log.e("HistoryFragment", "Error adding post to history", e));
    }
}
