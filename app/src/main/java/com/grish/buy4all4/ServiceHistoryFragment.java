package com.grish.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.grish.buy4all4.databinding.FragmentServiceHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServiceHistoryFragment extends Fragment {

    private FragmentServiceHistoryBinding binding;
    private ServiceAdapter adapter;
    private List<Service> serviceHistoryList = new ArrayList<>();
    private List<Service> filteredServiceHistoryList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ServiceHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceHistoryBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set up RecyclerView with adapter
        binding.recyclerViewServices.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServiceAdapter(getContext(), filteredServiceHistoryList,
                service -> {
                    // Handle item click
                    Log.d("ServiceHistoryFragment", "Service clicked: " + service.getTitle());
                },
                new ServiceAdapter.OnActionClickListener() {
                    @Override
                    public void onDeleteClick(Service service) {
                        deleteService(service);
                    }

                    @Override
                    public void onEditClick(Service service) {
                        editService(service);
                    }
                });
        binding.recyclerViewServices.setAdapter(adapter);

        if (mAuth.getCurrentUser() != null) {
            loadServiceHistory();
        } else {
            Log.e("ServiceHistoryFragment", "User not logged in");
        }

        return binding.getRoot();
    }

    private void loadServiceHistory() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("serviceHistory")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    serviceHistoryList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Service service = document.toObject(Service.class);
                        serviceHistoryList.add(service);
                    }
                    filteredServiceHistoryList.clear();
                    filteredServiceHistoryList.addAll(serviceHistoryList);
                    adapter.updateData(filteredServiceHistoryList);

                    // Show or hide the "no results" message
                    if (filteredServiceHistoryList.isEmpty()) {
                        binding.noResultsText.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResultsText.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> Log.e("ServiceHistoryFragment", "Error loading service history", e));
    }

    private void deleteService(Service service) {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("serviceHistory")
                .document(service.getServiceId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    serviceHistoryList.remove(service);
                    filteredServiceHistoryList.remove(service);
                    adapter.updateData(filteredServiceHistoryList);

                    // Show or hide the "no results" message
                    if (filteredServiceHistoryList.isEmpty()) {
                        binding.noResultsText.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResultsText.setVisibility(View.GONE);
                    }

                    Log.d("ServiceHistoryFragment", "Service deleted");
                })
                .addOnFailureListener(e -> Log.e("ServiceHistoryFragment", "Error deleting service", e));
    }

    private void editService(Service service) {
        Log.d("ServiceHistoryFragment", "Edit service: " + service.getTitle());
        // Navigate to edit screen or show dialog
    }
}
