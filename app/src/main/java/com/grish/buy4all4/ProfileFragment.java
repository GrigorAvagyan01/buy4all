package com.grish.buy4all4;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.grish.buy4all4.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String USERS_COLLECTION = "users";

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private CloudinaryManager cloudinaryManager;
    private ProgressBar progressBar;
    private View profileContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        LocaleHelper.setAppLanguage(requireContext());

        // Initialize views
        progressBar = binding.getRoot().findViewById(R.id.progressBar);
        profileContent = binding.getRoot().findViewById(R.id.profileContent);

        // Show only progress bar initially
        progressBar.setVisibility(View.VISIBLE);
        profileContent.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        cloudinaryManager = new CloudinaryManager();

        loadUserDataFromFirestore();

        binding.uploadImageButton.setOnClickListener(v -> openGallery());

        binding.UpdateBut.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), Update.class)));

        binding.MyAnouncmentsBut.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MyAnouncmentsHomePage.class)));

        binding.HistoryBut.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ServiceHomeHistory.class)));

        binding.SettingsBut.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SettingsActivity.class)));

        return binding.getRoot();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            uploadToCloudinary(selectedImageUri);
        }
    }

    private void uploadToCloudinary(Uri imageUri) {
        showProgress(true);
        binding.uploadImageButton.setEnabled(false);
        cloudinaryManager.uploadImage(requireContext(), imageUri, new CloudinaryManager.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                updateProfileImageUrl(imageUrl);
                Glide.with(requireContext())
                        .load(imageUrl)
                        .into(binding.uploadImageButton);
                binding.uploadImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                binding.uploadImageButton.setEnabled(true);
                showProgress(false);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.uploadImageButton.setEnabled(true);
                showProgress(false);
            }
        });
    }

    private void updateProfileImageUrl(String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        firestore.collection(USERS_COLLECTION)
                .document(userId)
                .update("profileImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Successfully saved image URL
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to update Firestore", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                });
    }

    private void loadUserDataFromFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection(USERS_COLLECTION).document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String email = mAuth.getCurrentUser().getEmail();
                String username = task.getResult().getString("username");
                String imageUrl = task.getResult().getString("profileImageUrl");

                binding.emailTextView.setText(email != null ? email : "Email not available");
                binding.usernameTextView1.setText(username != null ? username : "Username not available");

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.uploadImageButton);
                    binding.uploadImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }

                // Show content and hide progress bar when data is loaded
                profileContent.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                // Even if failed, show the content (with default/empty values)
                profileContent.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        profileContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}