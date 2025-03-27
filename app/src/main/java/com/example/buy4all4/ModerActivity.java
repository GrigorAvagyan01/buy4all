package com.example.buy4all4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ModerActivity extends AppCompatActivity {

    private ImageButton profileImageButton;
    private List<Post> allPosts;  // List to hold posts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moder); // Ensure activity_moder.xml exists

        profileImageButton = findViewById(R.id.imageButton);

        if (profileImageButton == null) {
            Log.e("ModerActivity", "ImageButton is null. Check your layout file.");
            return;
        }

        allPosts = new ArrayList<>();
        fetchPostsFromFirestore();

        // Set click listener to navigate to ProfileFragmentModer
        profileImageButton.setOnClickListener(v -> openProfileFragmentModer());
    }

    private void fetchPostsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        allPosts.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
                            post.setPostId(document.getId());
                            allPosts.add(post);
                        }
                    } else {
                        Log.e("Firestore Error", "Error fetching posts", task.getException());
                    }
                });
    }

    private void openProfileFragmentModer() {
        ProfileFragmentModer profileFragmentModer = new ProfileFragmentModer();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("posts", new ArrayList<>(allPosts));
        profileFragmentModer.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profileFragmentModer); // Ensure activity_moder.xml has a FrameLayout with this ID
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Open AddFragment to add a new post
    private void openAddFragment() {
        AddFragment addFragment = new AddFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Handle result from AddFragment (to refresh posts)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            fetchPostsFromFirestore(); // Refresh posts list when returning from AddFragment
        }
    }
}
