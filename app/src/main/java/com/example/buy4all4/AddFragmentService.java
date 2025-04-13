package com.example.buy4all4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.buy4all4.databinding.FragmentAddServiceBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddFragmentService extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentAddServiceBinding binding;
    private Uri imageUri;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private CloudinaryManager cloudinaryManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddServiceBinding.inflate(inflater, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedPhoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, "");
        binding.editNumberPhoneNo.setText(savedPhoneNumber);

        cloudinaryManager = new CloudinaryManager();

        setupSpinners();
        setupClickListeners();

        return binding.getRoot();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.currency_array,
                android.R.layout.simple_spinner_item
        );
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCurrency.setAdapter(currencyAdapter);

        ArrayAdapter<CharSequence> serviceAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Per Hour", "For the Service"}
        );
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerService.setAdapter(serviceAdapter);
    }

    private void setupClickListeners() {
        binding.uploadImageButtonadd.setOnClickListener(v -> openGallery());
        binding.buttonAddPost.setOnClickListener(v -> addPostToFirestore());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.uploadImageButtonadd.setImageURI(imageUri);
        }
    }

    private void addPostToFirestore() {
        String title = binding.editTextTitle.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();
        String price = binding.editNumberPrice.getText().toString().trim();
        String phoneNo = binding.editNumberPhoneNo.getText().toString().trim();
        String currency = binding.spinnerCurrency.getSelectedItem().toString();
        String serviceType = binding.spinnerService.getSelectedItem().toString();

        if (!validateInput(title, description, price, phoneNo)) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PHONE_NUMBER, phoneNo);
        editor.apply();

        uploadImageToCloudinary(title, description, price + " " + currency + " (" + serviceType + ")", phoneNo);
    }

    private boolean validateInput(String title, String description, String price, String phoneNo) {
        if (TextUtils.isEmpty(title)) {
            binding.editTextTitle.setError("Title is required");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            binding.editTextDescription.setError("Description is required");
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            binding.editNumberPrice.setError("Price is required");
            return false;
        }
        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            binding.editNumberPrice.setError("Price must be a valid number");
            return false;
        }
        if (!Patterns.PHONE.matcher(phoneNo).matches()) {
            binding.editNumberPhoneNo.setError("Invalid phone number");
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadImageToCloudinary(String title, String description, String price, String phoneNo) {
        cloudinaryManager.uploadImage(getActivity(), imageUri, new CloudinaryManager.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                savePostToFirestore(title, description, price, phoneNo, imageUrl);
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void savePostToFirestore(String title, String description, String price, String phoneNo, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> post = new HashMap<>();
        post.put("title", title);
        post.put("description", description);
        post.put("price", price);
        post.put("phoneNo", phoneNo);
        post.put("imageUrl", imageUrl);
        post.put("userId", user.getUid());

        db.collection("service").add(post).addOnSuccessListener(documentReference -> {
            Toast.makeText(getActivity(), "Service added successfully", Toast.LENGTH_SHORT).show();
            navigateToServiceFragment();
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Failed to add service: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void navigateToServiceFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ServiceFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
