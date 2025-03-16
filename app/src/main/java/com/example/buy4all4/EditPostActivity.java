package com.example.buy4all4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.buy4all4.databinding.ActivityEditPostBinding;

public class EditPostActivity extends AppCompatActivity {

    private ActivityEditPostBinding binding;
    private String postId;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the postId and other details passed from the previous activity
        postId = getIntent().getStringExtra("postId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");
        String phone = getIntent().getStringExtra("phone");

        // Set initial values in the EditText fields
        binding.etTitle.setText(title);
        binding.etDescription.setText(description);
        binding.etPrice.setText(price);
        binding.etPhone.setText(phone);

        // Set listeners
        binding.btnUpdate.setOnClickListener(v -> saveChanges());}

    private void saveChanges() {
        String newTitle = binding.etTitle.getText().toString().trim();
        String newDescription = binding.etDescription.getText().toString().trim();
        String newPrice = binding.etPrice.getText().toString().trim();
        String newPhone = binding.etPhone.getText().toString().trim();

        if (postId != null && currentUser != null) {
            firestore.collection("posts").document(postId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String postOwnerId = documentSnapshot.getString("userId");

                        if (postOwnerId != null && postOwnerId.equals(currentUser.getUid())) {
                            firestore.collection("posts").document(postId)
                                    .update("title", newTitle, "description", newDescription, "price", newPrice, "phone", newPhone)
                                    .addOnSuccessListener(aVoid -> {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("postId", postId);
                                        resultIntent.putExtra("title", newTitle);
                                        resultIntent.putExtra("description", newDescription);
                                        resultIntent.putExtra("price", newPrice);
                                        resultIntent.putExtra("phone", newPhone);
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EditPostActivity.this, "Failed to update post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(EditPostActivity.this, "You cannot edit this post.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
