package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.buy4all4.databinding.FragmentAddGuestBinding;

public class AddFragmentGuest extends Fragment {

    private FragmentAddGuestBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddGuestBinding.inflate(inflater, container, false);

        binding.signinguestadd.setOnClickListener(v -> {
            Intent addItemIntent = new Intent(getActivity(), SignInActivity.class);
            startActivity(addItemIntent);
        });

        binding.signupguestadd.setOnClickListener(v -> {
            Intent viewItemsIntent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(viewItemsIntent);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
