package com.grish.buy4all4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.grish.buy4all4.databinding.FragmentServiceBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ServiceFragment extends Fragment implements PostAdapter.OnFavoriteClickListener {

    private FragmentServiceBinding binding;
    private FirebaseFirestore db;
    private PostAdapter adapter;
    private List<Post> allServicePosts = new ArrayList<>();
    private List<Post> filteredPosts = new ArrayList<>();

    private double filterMinPrice = Double.MIN_VALUE;
    private double filterMaxPrice = Double.MAX_VALUE;
    private String filterCurrency = null;
    private String currentSortOrder = "None"; // "Min", "Max", "None"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();

        binding.recyclerViewServicePosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PostAdapter(getActivity(), filteredPosts, null, this);
        binding.recyclerViewServicePosts.setAdapter(adapter);

        setupFilterButton();
        setupSortSpinner();

        fetchServicePosts();

        return binding.getRoot();
    }

    private void setupFilterButton() {
        binding.btnFilterService.setOnClickListener(v -> showFilterDialog());
    }

    private void setupSortSpinner() {
        List<String> sortOptions = Arrays.asList(getString(R.string.none), getString(R.string.price_low_to_high), getString(R.string.price_high_to_low));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSortService.setAdapter(adapter);

        binding.spinnerSortService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (sortOptions.get(position)) {
                    case "Price: Low to High":
                        currentSortOrder = "Min";
                        break;
                    case "Price: High to Low":
                        currentSortOrder = "Max";
                        break;
                    default:
                        currentSortOrder = "None";
                }
                filterPosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchServicePosts() {
        db.collection("service")
                .whereEqualTo("isVerified", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        allServicePosts.clear();
                        allServicePosts.addAll(queryDocumentSnapshots.toObjects(Post.class));
                        filterPosts();
                    } else {
                        Toast.makeText(getActivity(), "No service posts found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to load service posts.", Toast.LENGTH_SHORT).show();
                });
    }

    private void filterPosts() {
        filteredPosts.clear();

        for (Post post : allServicePosts) {
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
        binding.noServiceResultsText.setVisibility(filteredPosts.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private double parsePrice(String priceString) {
        if (priceString == null || priceString.isEmpty()) return 0.0;

        try {
            priceString = priceString.replaceAll("[^\\d.]", "");
            return Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
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
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, currencies);
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

            filterPosts();
        });

        builder.setNegativeButton(getString(R.string.clear), (dialog, which) -> {
            filterCurrency = null;
            filterMinPrice = Double.MIN_VALUE;
            filterMaxPrice = Double.MAX_VALUE;
            filterPosts();
        });

        builder.show();
    }

    @Override
    public void onFavoriteClick(Post post) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);

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
