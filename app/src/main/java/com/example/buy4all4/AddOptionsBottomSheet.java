package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddOptionsBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_add_options, container, false);

        TextView sellProduct = view.findViewById(R.id.sell_product);
        TextView offerService = view.findViewById(R.id.offer_service);

        sellProduct.setOnClickListener(v -> {
            dismiss();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddFragment())
                    .commit();
        });

        offerService.setOnClickListener(v -> {
            dismiss();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddFragmentService())
                    .commit();
        });

        return view;
    }
}
