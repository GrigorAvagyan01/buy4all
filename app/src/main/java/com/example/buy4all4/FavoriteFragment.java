package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements PostAdapter.OnFavoriteClickListener {

    private RecyclerView favoriteRecyclerView;
    private PostAdapterFv postAdapterFv;
    private List<Post> favoritePosts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapterFv = new PostAdapterFv(favoritePosts);
        favoriteRecyclerView.setAdapter(postAdapterFv);

        return view;
    }

    @Override
    public void onFavoriteClick(Post post) {
        if (!favoritePosts.contains(post)) {
            favoritePosts.add(post);
            postAdapterFv.notifyDataSetChanged();
        }
    }
}