package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;

import com.example.buy4all4.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PostAdapter postAdapter;
    private List<Post> allPosts;
    private List<Post> filteredPosts;
    private Spinner categorySpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String selectedCategory = "All"; // Default selection

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        if (getContext() != null) {
            postAdapter = new PostAdapter(getContext(), filteredPosts, null, null);
        } else {
            return null;
        }

        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPosts.setAdapter(postAdapter);

        SearchView searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPostsBySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPostsBySearch(newText);
                return true;
            }
        });

        // Setup Category Spinner
        categorySpinner = binding.categorySpinner;
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Sport");
        categories.add("Gadgets");
        categories.add("Car");
        categories.add("Realty");
        categories.add("Electronics");
        categories.add("Health");
        categories.add("Other");

        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(spinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                filterPosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "All";
                filterPosts();
            }
        });

        fetchPosts();

        return binding.getRoot();
    }

    private void fetchPosts() {
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
                        filterPosts(); // Initial filtering after fetching
                    } else {
                        if (task.getException() != null) {
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    private void filterPosts() {
        filteredPosts.clear();
        for (Post post : allPosts) {
            if (selectedCategory.equals("All") || (post.getCategory() != null && post.getCategory().equals(selectedCategory))) {
                filteredPosts.add(post);
            }
        }
        postAdapter.notifyDataSetChanged();
    }

    private void filterPostsBySearch(String query) {
        String lowerQuery = query.toLowerCase();
        filteredPosts.clear();
        for (Post post : allPosts) {
            boolean matchesCategory = selectedCategory.equals("All") || (post.getCategory() != null && post.getCategory().equals(selectedCategory));
            boolean matchesSearch = post.getTitle().toLowerCase().contains(lowerQuery) || post.getDescription().toLowerCase().contains(lowerQuery);
            if (matchesCategory && matchesSearch) {
                filteredPosts.add(post);
            }
        }
        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}