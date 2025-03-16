package com.example.buy4all4;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, priceEditText, phoneNoEditText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update2);

        titleEditText = findViewById(R.id.updateTitle);
        descriptionEditText = findViewById(R.id.updateDesc);
        priceEditText = findViewById(R.id.updatePrice);
        phoneNoEditText = findViewById(R.id.updatephoneno);

        db = FirebaseFirestore.getInstance();

        // Get the post data passed from HomeFragment
        String postId = getIntent().getStringExtra("postId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");
        String phoneNo = getIntent().getStringExtra("phoneNo");

        // Set the data in the EditText fields
        titleEditText.setText(title);
        descriptionEditText.setText(description);
        priceEditText.setText(price);
        phoneNoEditText.setText(phoneNo);

        findViewById(R.id.updateButton).setOnClickListener(v -> {
            String newTitle = titleEditText.getText().toString().trim();
            String newDescription = descriptionEditText.getText().toString().trim();
            String newPrice = priceEditText.getText().toString().trim();
            String newPhoneNo = phoneNoEditText.getText().toString().trim();

            if (!newTitle.isEmpty() && !newDescription.isEmpty()) {
                // Update post in Firestore
                db.collection("posts").document(postId)
                        .update("title", newTitle, "description", newDescription, "price", newPrice, "phoneNo", newPhoneNo)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Finish activity and return to the home fragment
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to update post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
