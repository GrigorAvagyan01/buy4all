package com.example.buy4all4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buy4all4.databinding.FragmentFavoriteBinding;
import java.util.List;

public class FavoriteFragment extends Fragment implements PostAdapterFv.OnFavoriteRemoveClickListener {

    private FragmentFavoriteBinding binding;
    private PostAdapterFv adapter;
    private List<Post> favoritePosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoritePosts = FavoriteManager.getInstance().getFavorites();
        adapter = new PostAdapterFv(requireContext(), favoritePosts, this);
        binding.favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.favoriteRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onFavoriteRemoveClick(Post post) {
        FavoriteManager.getInstance().removeFavorite(post);
        favoritePosts.remove(post);
        adapter.notifyDataSetChanged();
    }
}
