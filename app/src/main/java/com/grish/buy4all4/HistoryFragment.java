package com.grish.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;

import com.grish.buy4all4.databinding.FragmentHistoryBinding;
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set up RecyclerView
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(getContext(), filteredHistoryList, null, null);
        binding.recyclerViewPosts.setAdapter(adapter);

        // Load history data
        if (mAuth.getCurrentUser() != null) {
            loadHistory();
        } else {
            Log.e("HistoryFragment", "User not logged in");
        }

        // Set up search functionality
        setupSearchView();

        // Clear history button
        binding.clearHistoryButton.setOnClickListener(v -> clearHistory());

        // Back button
        binding.backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return binding.getRoot();
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
                    filteredHistoryList.clear();
                    filteredHistoryList.addAll(historyList);
                    adapter.notifyDataSetChanged();

                    // Show or hide no results message
                    if (filteredHistoryList.isEmpty()) {
                        binding.noResultsText.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResultsText.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> Log.e("HistoryFragment", "Error loading history", e));
    }

    private void clearHistory() {
        String userId = mAuth.getCurrentUser().getUid();

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
                    binding.noResultsText.setVisibility(View.VISIBLE);
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

        // Show or hide no results message
        if (filteredHistoryList.isEmpty()) {
            binding.noResultsText.setVisibility(View.VISIBLE);
        } else {
            binding.noResultsText.setVisibility(View.GONE);
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
                .addOnSuccessListener(aVoid -> Log.d("HistoryFragment", "Post added to history"))
                .addOnFailureListener(e -> Log.e("HistoryFragment", "Error adding post to history", e));
    }
}
