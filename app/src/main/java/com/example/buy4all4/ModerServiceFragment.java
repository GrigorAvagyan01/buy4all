package com.example.buy4all4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buy4all4.databinding.FragmentModerServicePageBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ModerServiceFragment extends Fragment {
    private FragmentModerServicePageBinding binding;
    private ServiceAdapter serviceAdapter;
    private final List<Service> allServices = new ArrayList<>();
    private final List<Service> filteredServices = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentModerServicePageBinding.inflate(inflater, container, false);

        initializeAdapter();
        setupRecyclerView();
        fetchServicesFromFirestore();

        return binding.getRoot();
    }

    private void initializeAdapter() {
        serviceAdapter = new ServiceAdapter(requireContext(), filteredServices,
                this::openServiceDetail,
                new ServiceAdapter.OnActionClickListener() {
                    @Override
                    public void onDeleteClick(Service service) {
                        deleteService(service);
                    }

                    @Override
                    public void onEditClick(Service service) {
                        editService(service);
                    }
                }
        );
    }

    private void setupRecyclerView() {
        binding.servicesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.servicesRecyclerView.setAdapter(serviceAdapter);
    }

    private void fetchServicesFromFirestore() {
        FirebaseFirestore.getInstance().collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        allServices.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Service service = document.toObject(Service.class);
                            service.setServiceId(document.getId());
                            allServices.add(service);
                        }
                        updateFilteredServices("");
                    } else {
                        Log.e("FirestoreError", "Error loading services", task.getException());
                    }
                });
    }

    private void updateFilteredServices(String query) {
        filteredServices.clear();
        if (query.isEmpty()) {
            filteredServices.addAll(allServices);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Service service : allServices) {
                if (service.getTitle() != null &&
                        service.getTitle().toLowerCase().contains(lowerQuery)) {
                    filteredServices.add(service);
                }
            }
        }
        serviceAdapter.updateData(filteredServices);
    }

    private void openServiceDetail(Service service) {
        Log.d("ServiceDetail", "Opening: " + service.getTitle());
    }

    private void deleteService(Service service) {
        if (service.getServiceId() == null) {
            Toast.makeText(requireContext(), "Invalid service ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("services")
                .document(service.getServiceId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    allServices.remove(service);
                    updateFilteredServices("");
                    Toast.makeText(requireContext(), "Service deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Delete failed", e);
                });
    }

    private void editService(Service service) {
        Log.d("ServiceEdit", "Editing: " + service.getTitle());
    }
}