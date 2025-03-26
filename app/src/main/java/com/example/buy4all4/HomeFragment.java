package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
    private String selectedCategory = "All";
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        postAdapter = new PostAdapter(getContext(), filteredPosts, null, null);

        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPosts.setAdapter(postAdapter);

        // Set up the category spinner
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

        // Set up the search view
        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                        filterPosts("");
                    } else {
                        Log.e("FirestoreError", "Error fetching posts", task.getException());
                    }
                });
    }

    private void filterPosts() {
        filterPosts("");
    }

    private void filterPosts(String query) {
        filteredPosts.clear();

        // Filter posts based on category and search query
        for (Post post : allPosts) {
            boolean matchesCategory = selectedCategory.equals("All") || (post.getCategory() != null && post.getCategory().equals(selectedCategory));
            boolean matchesSearch = query.isEmpty() || (post.getTitle() != null && post.getTitle().toLowerCase().contains(query.toLowerCase())) ||
                    (post.getDescription() != null && post.getDescription().toLowerCase().contains(query.toLowerCase()));

            if (matchesCategory && matchesSearch) {
                filteredPosts.add(post);
            }
        }
        postAdapter.notifyDataSetChanged();
    }
}
