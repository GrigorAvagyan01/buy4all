package com.example.buy4all4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buy4all4.databinding.FragmentFavoriteBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FavoriteFragment extends Fragment implements PostAdapterFv.OnFavoriteRemoveClickListener {
    private FragmentFavoriteBinding binding;
    private PostAdapterFv adapter;
    private List<Post> favoritePosts;
    private List<Post> filteredPosts;

    private double filterMinPrice = Double.MIN_VALUE;
    private double filterMaxPrice = Double.MAX_VALUE;
    private String filterCurrency = null;
    private String currentSortOrder = "None"; // "Min", "Max", "None"

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);

        favoritePosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        adapter = new PostAdapterFv(requireContext(), filteredPosts, this);
        binding.favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.favoriteRecyclerView.setAdapter(adapter);

        setupSortSpinner();
        setupFilterButton();

        loadFavoritePostsFromFirestore();
        return binding.getRoot();
    }

    private void loadFavoritePostsFromFirestore() {
        List<String> favoriteIds = new ArrayList<>(FavoriteManager.getInstance().getFavoritePostIds());
        if (favoriteIds.isEmpty()) {
            favoritePosts.clear();
            filteredPosts.clear();
            adapter.notifyDataSetChanged();
            binding.noResultsTextFavorites.setVisibility(View.VISIBLE);
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String postId : favoriteIds) {
            System.out.println("Post id: " + postId);
            db.collection("posts")
                    .document(postId)
                    .get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            System.out.println("PRINTEDDDDD");
                            Post post = document.toObject(Post.class);
                            favoritePosts.add(post);
                            System.out.println("SZZ:: " + favoritePosts.size());

                            // When all documents are fetched, apply filters
                            if (favoritePosts.size() == favoriteIds.size()) {
                                filteredPosts.clear();
                                filteredPosts.addAll(favoritePosts);
                                applyFilters();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FavoriteFragment", "Error fetching post: " + postId, e);
                    });
        }

    }

    private void setupSortSpinner() {
        List<String> sortOptions = Arrays.asList("None", "Price: Low to High", "Price: High to Low");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSortFavorites.setAdapter(spinnerAdapter);

        binding.spinnerSortFavorites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = sortOptions.get(position);
                switch (selectedOption) {
                    case "Price: Low to High":
                        currentSortOrder = "Min";
                        break;
                    case "Price: High to Low":
                        currentSortOrder = "Max";
                        break;
                    default:
                        currentSortOrder = "None";
                        break;
                }
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFilterButton() {
        binding.btnFilterFavorites.setOnClickListener(v -> showFilterDialog());
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
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, currencies);
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
            applyFilters();
        });

        builder.setNegativeButton("Clear", (dialog, which) -> {
            filterMinPrice = Double.MIN_VALUE;
            filterMaxPrice = Double.MAX_VALUE;
            filterCurrency = null;
            applyFilters();
        });

        builder.show();
    }

    private void applyFilters() {
        filteredPosts.clear();

        for (Post post : favoritePosts) {
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

            if (matchesPrice) {
                filteredPosts.add(post);
            }
        }

        if (currentSortOrder.equals("Min")) {
            filteredPosts.sort(Comparator.comparingDouble(p -> parsePrice(p.getPrice())));
        } else if (currentSortOrder.equals("Max")) {
            filteredPosts.sort((p1, p2) -> Double.compare(parsePrice(p2.getPrice()), parsePrice(p1.getPrice())));
        }

        adapter.notifyDataSetChanged();
        binding.noResultsTextFavorites.setVisibility(filteredPosts.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private double parsePrice(String priceString) {
        if (priceString == null || priceString.isEmpty()) return 0.0;
        try {
            priceString = priceString.replaceAll("[^\\d.]", "");
            return Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Log.e("PriceParseError", "Invalid price format: " + priceString);
            return 0.0;
        }
    }

    private double parseSafeDouble(String input, double fallback) {
        try {
            return Double.parseDouble(input.replace(",", "."));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    @Override
    public void onFavoriteRemoveClick(Post post) {
        FavoriteManager.getInstance().removeFavorite(post);
        favoritePosts.remove(post);
        applyFilters();
    }
}
