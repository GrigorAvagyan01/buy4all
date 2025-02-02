package com.example.buy4all4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.buy4all4.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String IMAGE_FILE_NAME = "profile_image.jpg";

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        loadSavedImage();

        binding.uploadImageButton.setOnClickListener(v -> openGallery());

        binding.UpdateBut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Update.class);
            startActivity(intent);
        });

        binding.MyAnouncmentsBut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyAnouncments.class);
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
            saveImageToInternalStorage(selectedImageUri);
            loadSavedImage();
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Save bitmap to internal storage
            File file = new File(requireActivity().getFilesDir(), IMAGE_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedImage() {
        File file = new File(requireActivity().getFilesDir(), IMAGE_FILE_NAME);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            binding.uploadImageButton.setImageBitmap(bitmap);

            binding.uploadImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
