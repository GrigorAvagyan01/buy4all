package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.buy4all4.databinding.FragmentServiceBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment implements PostAdapter.OnFavoriteClickListener {

    private FragmentServiceBinding binding;
    private FirebaseFirestore db;
    private PostAdapter adapter;
    private List<Post> postList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceBinding.inflate(inflater, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        binding.recyclerViewServicePosts.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Adapter with an empty list
        adapter = new PostAdapter(getActivity(), postList, null, this);
        binding.recyclerViewServicePosts.setAdapter(adapter);

        // Fetch posts
        fetchServicePosts();

        return binding.getRoot();
    }

    private void fetchServicePosts() {
        db.collection("service")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        // Convert Firestore documents to Post objects
                        List<Post> fetchedPosts = queryDocumentSnapshots.toObjects(Post.class);
                        postList.clear();  // Clear the previous list
                        postList.addAll(fetchedPosts);
                        adapter.notifyDataSetChanged();  // Notify adapter to update UI

                        System.out.println("Fetched " + postList.size() + " service posts.");
                    } else {
                        System.out.println("No posts found in 'service' collection.");
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to load service posts.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onFavoriteClick(Post post) {
        // Add the post to the FavoriteFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);  // Use putParcelable instead of putSerializable

        // Instantiate the FavoriteFragment and pass the bundle
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        favoriteFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, favoriteFragment)  // Use the correct container ID
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
