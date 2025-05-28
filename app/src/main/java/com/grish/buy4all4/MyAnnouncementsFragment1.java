package com.grish.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.grish.buy4all4.databinding.FragmentMyAnnouncements1Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyAnnouncementsFragment1 extends Fragment {

    private FragmentMyAnnouncements1Binding binding;
    private PostAdapterMa adapter;
    private List<Post> postList;
    private List<Post> filteredPostList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyAnnouncements1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        postList = new ArrayList<>();
        filteredPostList = new ArrayList<>();

        setupRecyclerView();
        loadPosts();
        setupSearchView();

        binding.imageButtonback.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapterMa(getContext(), filteredPostList, new PostAdapterMa.OnPostOptionsClickListener() {
            @Override
            public void onEditPost(String postId) {
                Intent intent = new Intent(getContext(), UpdateActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }

            @Override
            public void onDeletePost(String postId) {
                db.collection("posts").document(postId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                            loadPosts();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to delete post", Toast.LENGTH_SHORT).show());
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadPosts() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
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
                    filterPosts(""); // Apply visibility logic for noResultsText
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load posts", Toast.LENGTH_SHORT).show());
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
                    .filter(post -> post.getTitle() != null &&
                            post.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        adapter.notifyDataSetChanged();

        // Show or hide the noResultsText
        if (binding != null) {
            binding.noResultsText.setVisibility(filteredPostList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }
}
