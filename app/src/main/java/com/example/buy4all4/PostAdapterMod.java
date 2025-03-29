package com.example.buy4all4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.buy4all4.databinding.ItemPostModBinding;

import java.util.List;

public class PostAdapterMod extends RecyclerView.Adapter<PostAdapterMod.PostViewHolder> {
    private Context context;
    private List<Post> posts;
    private OnPostClickListener postClickListener;
    private OnImageButtonClickListener imageButtonClickListener;

    // Constructor for the adapter
    public PostAdapterMod(Context context, List<Post> posts, OnPostClickListener postClickListener, OnImageButtonClickListener imageButtonClickListener) {
        this.context = context;
        this.posts = posts;
        this.postClickListener = postClickListener;
        this.imageButtonClickListener = imageButtonClickListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Use View Binding to inflate the layout
        ItemPostModBinding binding = ItemPostModBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.binding.postTitleMod.setText(post.getTitle());
        holder.binding.postPriceMod.setText("$" + post.getPrice()); // Assuming 'price' is a String or double attribute

        // Handle post click event
        holder.itemView.setOnClickListener(v -> postClickListener.onPostClick(post));

        // Handle image button click event
        holder.binding.optionsMenuImageViewmod.setOnClickListener(v -> imageButtonClickListener.onImageButtonClick(post));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // ViewHolder class for each post item
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ItemPostModBinding binding;

        public PostViewHolder(ItemPostModBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    // Interface for post click handling
    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    // Interface for image button click handling
    public interface OnImageButtonClickListener {
        void onImageButtonClick(Post post);
    }
}
