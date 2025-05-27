package com.grish.buy4all4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.grish.buy4all4.databinding.FragmentFavoriteBinding;
import com.google.firebase.firestore.FirebaseFirestore;

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
        List<String> sortOptions = Arrays.asList(getString(R.string.none), getString(R.string.price_low_to_high), getString(R.string.price_high_to_low));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSortFavorites.setAdapter(spinnerAdapter);

        binding.spinnerSortFavorites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = sortOptions.get(position);
                switch (selectedOption) {
                    case "Price: Low to High":
                        currentSortOrder = getString(R.string.min);
                        break;
                    case "Price: High to Low":
                        currentSortOrder = getString(R.string.max);
                        break;
                    default:
                        currentSortOrder = getString(R.string.none);
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
        minPriceInput.setHint(getString(R.string.min_price));
        minPriceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(minPriceInput);

        EditText maxPriceInput = new EditText(getContext());
        maxPriceInput.setHint(getString(R.string.max_price));
        maxPriceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(maxPriceInput);

        Spinner currencySpinner = new Spinner(getContext());
        List<String> currencies = Arrays.asList("USD", "EUR", "AMD");
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, currencies);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        layout.addView(currencySpinner);

        builder.setView(layout);

        builder.setPositiveButton(getString(R.string.apply), (dialog, which) -> {
            String minPriceStr = minPriceInput.getText().toString().trim();
            String maxPriceStr = maxPriceInput.getText().toString().trim();

            filterMinPrice = minPriceStr.isEmpty() ? Double.MIN_VALUE : parseSafeDouble(minPriceStr, Double.MIN_VALUE);
            filterMaxPrice = maxPriceStr.isEmpty() ? Double.MAX_VALUE : parseSafeDouble(maxPriceStr, Double.MAX_VALUE);

            filterCurrency = currencySpinner.getSelectedItem().toString();
            applyFilters();
        });

        builder.setNegativeButton(getString(R.string.clear), (dialog, which) -> {
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

        if (currentSortOrder.equals(getString(R.string.min))) {
            filteredPosts.sort(Comparator.comparingDouble(p -> parsePrice(p.getPrice())));
        } else if (currentSortOrder.equals(getString(R.string.max))) {
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
