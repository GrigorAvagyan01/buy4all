package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.buy4all4.databinding.FragmentFavoriteGuestBinding;

public class FavoriteFragmentGuest extends Fragment {

    private FragmentFavoriteGuestBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteGuestBinding.inflate(inflater, container, false);

        binding.signinguestfav.setOnClickListener(v -> {
            Intent signInIntent = new Intent(getActivity(), SignInActivity.class);
            startActivity(signInIntent);
        });

        binding.signupguestfav.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(signUpIntent);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
