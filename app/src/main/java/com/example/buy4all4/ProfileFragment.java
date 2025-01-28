package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.buy4all4.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
