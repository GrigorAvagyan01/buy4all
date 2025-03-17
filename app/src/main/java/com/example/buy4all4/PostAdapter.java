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
    private OnPostOptionsClickListener onPostOptionsClickListener;

    public PostAdapter(Context context, List<Post> postList, OnPostOptionsClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.onPostOptionsClickListener = listener;
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

        holder.binding.postTitle.setText(post.getTitle() != null ? post.getTitle() : "No Title");
        holder.binding.postPrice.setText(post.getPrice() != null ? post.getPrice() : "No Price");

        if (post.getImageUrl() != null) {
            Glide.with(context)
                    .load(post.getImageUrl())
                    .into(holder.binding.postImage);
        }

        // ✅ Short click opens PostDetailActivity
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

        // ✅ Long press shows edit/delete menu
        holder.binding.getRoot().setOnLongClickListener(v -> {
            if (onPostOptionsClickListener != null) {
                onPostOptionsClickListener.onPostOptionsClicked(v, holder.getAdapterPosition(), post);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnPostOptionsClickListener {
        void onPostOptionsClicked(View view, int position, Post post);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostBinding binding;

        public PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
