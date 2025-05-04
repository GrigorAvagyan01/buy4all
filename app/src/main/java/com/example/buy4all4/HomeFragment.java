package com.example.buy4all4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PostAdapter postAdapter;
    private List<Post> allPosts;
    private List<Post> filteredPosts;
    private String selectedCategory = "All";
    private String currentSearchQuery = "";
    private double filterMinPrice = Double.MIN_VALUE;
    private double filterMaxPrice = Double.MAX_VALUE;
    private String filterCurrency = null;
    private String currentSortOrder = "None"; // "Min", "Max", "None"

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        postAdapter = new PostAdapter(getContext(), filteredPosts, null, null);
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPosts.setAdapter(postAdapter);

        setupCategoryRecyclerView();
        setupSearchView();
        setupFilterButton();
        setupSortSpinner(); // NEW

        fetchPosts();

        return binding.getRoot();
    }

    private void setupCategoryRecyclerView() {
        List<String> categories = Arrays.asList("All", "Sport", "Gadgets", "Car", "Realty", "Electronics", "Health", "Other");

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, category -> {
            selectedCategory = category;
            filterPosts(currentSearchQuery);
        });

        binding.categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void setupSortSpinner() {
        List<String> sortOptions = Arrays.asList("None", "Price: Low to High", "Price: High to Low");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(adapter);

        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = sortOptions.get(position);
                switch (selected) {
                    case "Price: Low to High":
                        currentSortOrder = "Min";
                        break;
                    case "Price: High to Low":
                        currentSortOrder = "Max";
                        break;
                    default:
                        currentSortOrder = "None";
                }
                filterPosts(currentSearchQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchPosts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .whereEqualTo("isVerified", true)
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
                        binding.progressBar.setVisibility(View.GONE);
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

            if (matchesCategory && matchesSearch && matchesPrice) {
                filteredPosts.add(post);
            }
        }

        // Sort logic
        if (currentSortOrder.equals("Min")) {
            filteredPosts.sort(Comparator.comparingDouble(p -> parsePrice(p.getPrice())));
        } else if (currentSortOrder.equals("Max")) {
            filteredPosts.sort((p1, p2) -> Double.compare(parsePrice(p2.getPrice()), parsePrice(p1.getPrice())));
        }

        postAdapter.notifyDataSetChanged();
        binding.noResultsText.setVisibility(filteredPosts.isEmpty() ? View.VISIBLE : View.GONE);
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
        minPriceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(minPriceInput);

        EditText maxPriceInput = new EditText(getContext());
        maxPriceInput.setHint("Max Price");
        maxPriceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(maxPriceInput);

        Spinner currencySpinner = new Spinner(getContext());
        List<String> currencies = Arrays.asList("USD", "EUR", "AMD");
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, currencies);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        layout.addView(currencySpinner);

        builder.setView(layout);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            String minPriceStr = minPriceInput.getText().toString().trim();
            String maxPriceStr = maxPriceInput.getText().toString().trim();

            filterMinPrice = minPriceStr.isEmpty() ? Double.MIN_VALUE : parseSafeDouble(minPriceStr, Double.MIN_VALUE);
            filterMaxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : parseSafeDouble(maxPriceStr, Double.MAX_VALUE);

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

    private double parseSafeDouble(String input, double fallback) {
        try {
            return Double.parseDouble(input.replace(",", "."));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
