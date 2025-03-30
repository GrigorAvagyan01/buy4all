package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.buy4all4.databinding.FragmentAddSerOrSellBinding;

public class AddFragmentSerOrSell extends Fragment {

    private FragmentAddSerOrSellBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddSerOrSellBinding.inflate(inflater, container, false);

        // Handle button clicks
        binding.sellbut.setOnClickListener(v -> openFragment(new AddFragment()));
        binding.servicebut.setOnClickListener(v -> openFragment(new AddFragmentService()));

        return binding.getRoot();
    }

    // Function to replace fragment in the container
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
