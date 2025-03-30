package com.example.buy4all4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ItemPostBinding;
import java.util.List;

public class PostAdapterFv extends RecyclerView.Adapter<PostAdapterFv.PostViewHolder> {

    private Context context;
    private List<Post> favoritePosts;
    private OnFavoriteRemoveClickListener onFavoriteRemoveClickListener;

    public PostAdapterFv(Context context, List<Post> favoritePosts, OnFavoriteRemoveClickListener listener) {
        this.context = context;
        this.favoritePosts = favoritePosts;
        this.onFavoriteRemoveClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = favoritePosts.get(position);

        holder.binding.postTitle.setText(post.getTitle());
        holder.binding.postPrice.setText(post.getPrice());
        Glide.with(context).load(post.getImageUrl()).into(holder.binding.postImage);

        holder.binding.favoriteButton.setOnClickListener(v -> {
            FavoriteManager.getInstance().removeFavorite(post);
            if (onFavoriteRemoveClickListener != null) {
                onFavoriteRemoveClickListener.onFavoriteRemoveClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritePosts.size();
    }

    public interface OnFavoriteRemoveClickListener {
        void onFavoriteRemoveClick(Post post);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostBinding binding;

        public PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}