package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    private EditText editTextTitle, editTextDescription, editTextPrice, editTextPhoneNo;
    private Button buttonAddPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        editTextTitle = rootView.findViewById(R.id.edit_text_title);
        editTextDescription = rootView.findViewById(R.id.edit_text_description);
        editTextPrice = rootView.findViewById(R.id.edit_number_price);
        editTextPhoneNo = rootView.findViewById(R.id.edit_number_phone_no);
        buttonAddPost = rootView.findViewById(R.id.button_add_post);

        buttonAddPost.setOnClickListener(v -> addPostToFirestore());

        return rootView;
    }

    private void addPostToFirestore() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String phoneNo = editTextPhoneNo.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Title is required");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            editTextDescription.setError("Description is required");
            return;
        }

        if (TextUtils.isEmpty(price)) {
            editTextPrice.setError("Price is required");
            return;
        }

        if (TextUtils.isEmpty(phoneNo)) {
            editTextPhoneNo.setError("Phone number is required");
            return;
        }

        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            editTextPrice.setError("Price must be a valid number");
            return;
        }

        if (!Patterns.PHONE.matcher(phoneNo).matches()) {
            editTextPhoneNo.setError("Invalid phone number");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> post = new HashMap<>();
        post.put("title", title);
        post.put("description", description);
        post.put("price", price);
        post.put("phoneNo", phoneNo);

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Post added successfully", Toast.LENGTH_SHORT).show();
                    navigateToHomePage();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to add post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void navigateToHomePage() {
        // Navigate using Fragment
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view_tag, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Alternatively, navigate using Intent (if you are working with activities instead of fragments)
        Intent intent = new Intent(getActivity(), HomeFragment.class);
        startActivity(intent);
    }
}
