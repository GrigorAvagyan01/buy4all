package com.example.buy4all4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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

    private String currentSearchQuery = "";
    private double filterMinPrice = Double.MIN_VALUE;
    private double filterMaxPrice = Double.MAX_VALUE;
    private String filterCurrency = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        postAdapter = new PostAdapter(getContext(), filteredPosts, null, null);
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPosts.setAdapter(postAdapter);

        setupCategorySpinner();
        setupSearchView();
        setupFilterButton();

        fetchPosts();

        return binding.getRoot();
    }

    private void setupCategorySpinner() {
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
                filterPosts(currentSearchQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "All";
                filterPosts(currentSearchQuery);
            }
        });
    }

    private void setupSearchView() {
        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                filterPosts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                filterPosts(newText);
                return false;
            }
        });
    }

    private void setupFilterButton() {
        binding.btnFilter.setOnClickListener(v -> showFilterDialog());
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
                        filterPosts(currentSearchQuery);
                    } else {
                        Log.e("FirestoreError", "Error fetching posts", task.getException());
                    }
                });
    }

    private void filterPosts(String query) {
        filteredPosts.clear();

        for (Post post : allPosts) {
            boolean matchesCategory = selectedCategory.equals("All") ||
                    (post.getCategory() != null && post.getCategory().equals(selectedCategory));

            boolean matchesSearch = query.isEmpty() ||
                    (post.getTitle() != null && post.getTitle().toLowerCase().contains(query.toLowerCase())) ||
                    (post.getDescription() != null && post.getDescription().toLowerCase().contains(query.toLowerCase()));

            boolean matchesPrice = true;

            if (filterCurrency != null) {
                String postCurrency = post.getCurrency();
                String postPriceStr = post.getPrice();

                if (postCurrency == null || postPriceStr == null || postPriceStr.isEmpty()) {
                    matchesPrice = false;
                } else {
                    try {
                        double price = parsePrice(postPriceStr);
                        matchesPrice = postCurrency.equals(filterCurrency)
                                && price >= filterMinPrice && price <= filterMaxPrice;
                    } catch (NumberFormatException e) {
                        Log.e("FilterError", "Invalid price: " + postPriceStr);
                        matchesPrice = false;
                    }
                }
            }

            Log.d("FilterCheck", "Post: " + post.getTitle() + ", matchesCategory: " + matchesCategory + ", matchesSearch: " + matchesSearch + ", matchesPrice: " + matchesPrice);

            if (matchesCategory && matchesSearch && matchesPrice) {
                filteredPosts.add(post);
            }
        }

        postAdapter.notifyDataSetChanged();
        Log.d("FilterDebug", "Filtered posts count: " + filteredPosts.size());

        if (filteredPosts.isEmpty()) {
            binding.noResultsText.setVisibility(View.VISIBLE);
        } else {
            binding.noResultsText.setVisibility(View.GONE);
        }
    }

    private double parsePrice(String priceString) {
        if (priceString == null || priceString.isEmpty()) {
            return 0.0;
        }

        try {
            priceString = priceString.replaceAll("[^\\d.]", "");
            return Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Log.e("PriceParseError", "Invalid price format: " + priceString);
            return 0.0;
        }
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Price");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText minPriceInput = new EditText(getContext());
        minPriceInput.setHint("Min Price");
        minPriceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(minPriceInput);

        EditText maxPriceInput = new EditText(getContext());
        maxPriceInput.setHint("Max Price");
        maxPriceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(maxPriceInput);

        Spinner currencySpinner = new Spinner(getContext());
        List<String> currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("EUR");
        currencies.add("AMD");
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currencies);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        layout.addView(currencySpinner);

        builder.setView(layout);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            try {
                filterMinPrice = Double.parseDouble(minPriceInput.getText().toString());
            } catch (NumberFormatException e) {
                filterMinPrice = Double.MIN_VALUE;
            }

            try {
                filterMaxPrice = Double.parseDouble(maxPriceInput.getText().toString());
            } catch (NumberFormatException e) {
                filterMaxPrice = Double.MAX_VALUE;
            }

            filterCurrency = currencySpinner.getSelectedItem().toString();

            filterPosts(currentSearchQuery);
        });

        builder.setNegativeButton("Clear", (dialog, which) -> {
            filterCurrency = null;
            filterMinPrice = Double.MIN_VALUE;
            filterMaxPrice = Double.MAX_VALUE;
            filterPosts(currentSearchQuery);
        });

        builder.show();
    }
}
