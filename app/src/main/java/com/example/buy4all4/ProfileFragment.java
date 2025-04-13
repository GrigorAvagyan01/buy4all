package com.example.buy4all4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.buy4all4.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String IMAGE_FILE_NAME = "profile_image.jpg";
    private static final String USERS_COLLECTION = "users";

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        LocaleHelper.setAppLanguage(requireContext());
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loadSavedImage();
        loadUserDataFromFirestore();

        binding.uploadImageButton.setOnClickListener(v -> openGallery());

        binding.UpdateBut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Update.class);
            startActivity(intent);
        });

        binding.MyAnouncmentsBut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyAnouncmentsHomePage.class);
            startActivity(intent);
        });

        binding.HistoryBut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        binding.SettingsBut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            encodeAndSaveImage(selectedImageUri);
            loadSavedImage();
        }
    }

    private void encodeAndSaveImage(Uri imageUri) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            String encodedImage = encodeImage(bitmap);

            File file = new File(requireActivity().getFilesDir(), IMAGE_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(encodedImage.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void loadSavedImage() {
        File file = new File(requireActivity().getFilesDir(), IMAGE_FILE_NAME);
        if (file.exists()) {
            try {
                byte[] imageBytes = new byte[(int) file.length()];
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(Uri.fromFile(file));
                inputStream.read(imageBytes);
                String encodedImage = new String(imageBytes);
                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                binding.uploadImageButton.setImageBitmap(bitmap);
                binding.uploadImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserDataFromFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection(USERS_COLLECTION).document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String email = mAuth.getCurrentUser().getEmail();
                String username = task.getResult().getString("username");

                binding.emailTextView.setText(email != null ? email : "Email not available");
                binding.usernameTextView1.setText(username != null ? username : "Username not available");
            } else {
                Toast.makeText(getActivity(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
