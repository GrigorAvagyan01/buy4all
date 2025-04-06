package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ItemPostBinding;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;
    private OnItemClickListener onItemClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;

    public PostAdapter(Context context, List<Post> postList, OnItemClickListener itemClickListener, OnFavoriteClickListener favoriteListener) {
        this.context = context;
        this.postList = postList;
        this.onItemClickListener = itemClickListener;
        this.onFavoriteClickListener = favoriteListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Bind title
        holder.binding.postTitle.setText(post.getTitle() != null ? post.getTitle() : "No Title");

        // Format price with currency symbol
        holder.binding.postPrice.setText(formatPrice(post.getPrice(), post.getCurrency()));

        // Load image
        if (post.getImageUrl() != null) {
            Glide.with(context).load(post.getImageUrl()).into(holder.binding.postImage);
        }

        // Item click listener to open PostDetailActivity
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("imageUrl", post.getImageUrl());
            intent.putExtra("title", post.getTitle());
            intent.putExtra("price", post.getPrice());
            intent.putExtra("description", post.getDescription());
            intent.putExtra("phone", post.getPhoneNo());
            context.startActivity(intent);
        });

        // Favorite button click
        holder.binding.favoriteButton.setOnClickListener(v -> {
            FavoriteManager.getInstance().addFavorite(post);
            if (onFavoriteClickListener != null) {
                onFavoriteClickListener.onFavoriteClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Post post);
    }

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostBinding binding;

        public PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    // Method to format price with correct symbol
    private String formatPrice(String price, String currency) {
        if (price == null || price.isEmpty()) {
            return "No Price";
        }

        // Trim and check for the currency code to append the symbol
        String trimmedPrice = price.trim();

        if ("EUR".equals(currency)) {
            return "€" + trimmedPrice;
        } else if ("USD".equals(currency)) {
            return "$ " + trimmedPrice;
        } else if ("AMD".equals(currency)) {
            return "֏" + trimmedPrice;  // Add the Armenian Dram symbol for AMD
        }

        return price; // If no currency is detected, return as is
    }
}
