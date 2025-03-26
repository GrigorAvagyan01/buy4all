package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView; // Correct import

import com.example.buy4all4.databinding.FragmentServiceBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceFragment extends Fragment implements PostAdapter.OnFavoriteClickListener {

    private FragmentServiceBinding binding;
    private FirebaseFirestore db;
    private PostAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private List<Post> filteredPostList = new ArrayList<>(); // List for filtered posts

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceBinding.inflate(inflater, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        binding.recyclerViewServicePosts.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Adapter with empty filtered list
        adapter = new PostAdapter(getActivity(), filteredPostList, null, this);
        binding.recyclerViewServicePosts.setAdapter(adapter);

        // Fetch posts from Firestore
        fetchServicePosts();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ensure searchView is not null
        if (binding.searchView != null) {
            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        } else {
            System.out.println("Error: searchView is NULL!");
        }
    }

    private void fetchServicePosts() {
        db.collection("service")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        // Convert Firestore documents to Post objects
                        List<Post> fetchedPosts = queryDocumentSnapshots.toObjects(Post.class);
                        postList.clear();
                        postList.addAll(fetchedPosts);

                        // Update filtered list with all posts initially
                        filteredPostList.clear();
                        filteredPostList.addAll(postList);
                        adapter.notifyDataSetChanged();

                        System.out.println("Fetched " + postList.size() + " service posts.");
                    } else {
                        System.out.println("No posts found in 'service' collection.");
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to load service posts.", Toast.LENGTH_SHORT).show();
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
    public void onFavoriteClick(Post post) {
        // Add the post to the FavoriteFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", post);

        FavoriteFragment favoriteFragment = new FavoriteFragment();
        favoriteFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, favoriteFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
