package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;

import com.example.buy4all4.databinding.FragmentModerHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ModerServiceFragment extends Fragment {

    private FragmentModerHomeBinding binding;
    private PostAdapterMod postAdapter;
    private List<Post> allPosts;
    private List<Post> filteredPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentModerHomeBinding.inflate(inflater, container, false);
        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        postAdapter = new PostAdapterMod(
                getContext(), filteredPosts,
                post -> openPostDetailActivity(post),
                post -> deletePost(post),
                post -> approvePost(post)
        );

        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.postsRecyclerView.setAdapter(postAdapter);

        fetchPostsFromFirestore();
        setupSearchView();

        return binding.getRoot();
    }

    private void fetchPostsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("service")
                .whereEqualTo("isVerified", false)
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
        Log.d("ModerFragment", "Post clicked: " + post.getTitle());
    }

    private void deletePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (post.getPostId() == null) {
            Toast.makeText(getContext(), "Error: Post ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("service").document(post.getPostId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                    removePostFromList(post);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting post", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore Error", "Delete failed", e);
                });
    }

    private void approvePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (post.getPostId() == null) {
            Toast.makeText(getContext(), "Error: Post ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("service").document(post.getPostId())
                .update("isVerified", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Post approved", Toast.LENGTH_SHORT).show();
                    removePostFromList(post);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error approving post", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore Error", "Approve failed", e);
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

