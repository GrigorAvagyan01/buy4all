package com.example.buy4all4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ItemPostFvBinding;

import java.util.List;

public class PostAdapterFv extends RecyclerView.Adapter<PostAdapterFv.PostViewHolderFv> {

    private List<Post> favoritePosts;

    public PostAdapterFv(List<Post> favoritePosts) {
        this.favoritePosts = favoritePosts;
    }

    @NonNull
    @Override
    public PostViewHolderFv onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostFvBinding binding = ItemPostFvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostViewHolderFv(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolderFv holder, int position) {
        Post post = favoritePosts.get(position);

        holder.binding.postTitle.setText(post.getTitle() != null ? post.getTitle() : "No Title");
        holder.binding.postPrice.setText(post.getPrice() != null ? post.getPrice() : "No Price");

        if (post.getImageUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getImageUrl())
                    .into(holder.binding.postImage);
        }
    }

    @Override
    public int getItemCount() {
        return favoritePosts.size();
    }

    public static class PostViewHolderFv extends RecyclerView.ViewHolder {
        private final ItemPostFvBinding binding;

        public PostViewHolderFv(ItemPostFvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}