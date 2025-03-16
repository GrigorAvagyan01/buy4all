package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.buy4all4.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PostAdapter.OnPostOptionsClickListener {

    private FragmentHomeBinding binding;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        postList = new ArrayList<>();

        // Check if the fragment is attached to an activity
        if (getContext() != null) {
            postAdapter = new PostAdapter(getContext(), postList, this);  // Pass 'this' to the constructor
        } else {
            return null; // Return null if context is unavailable
        }

        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPosts.setAdapter(postAdapter);

        fetchPosts();

        return binding.getRoot();
    }

    private void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
                            post.setPostId(document.getId()); // Set Firestore document ID as postId
                            postList.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onPostOptionsClicked(View view, int position, Post post) {
        showPostOptionsMenu(view, post);
    }

    private void showPostOptionsMenu(View view, Post post) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.post_menu);  // Assume post_options_menu.xml exists
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                Intent intent = new Intent(getContext(), UpdateActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("title", post.getTitle());
                intent.putExtra("description", post.getDescription());
                intent.putExtra("price", post.getPrice());
                intent.putExtra("phoneNo", post.getPhoneNo());
                startActivity(intent);
                return true;
            } else if (itemId == R.id.action_delete) {
                deletePost(post);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void deletePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String postId = post.getPostId();
        if (postId != null) {
            db.collection("posts").document(postId).delete()
                    .addOnSuccessListener(aVoid -> {
                        postList.remove(post);
                        postAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to delete post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
