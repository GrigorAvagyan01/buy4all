package com.grish.buy4all4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grish.buy4all4.databinding.ItemPostModBinding;

import java.util.List;

public class PostAdapterMod extends RecyclerView.Adapter<PostAdapterMod.PostViewHolder> {
    private final Context context;
    private List<Post> posts;
    private final OnPostClickListener postClickListener;
    private final OnDeleteClickListener deleteClickListener;
    private final OnApproveClickListener approveClickListener;

    public PostAdapterMod(Context context,
                          List<Post> posts,
                          OnPostClickListener postClickListener,
                          OnDeleteClickListener deleteClickListener,
                          OnApproveClickListener approveClickListener) {
        this.context = context;
        this.posts = posts;
        this.postClickListener = postClickListener;
        this.deleteClickListener = deleteClickListener;
        this.approveClickListener = approveClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostModBinding binding = ItemPostModBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Title & Price
        holder.binding.postTitleMod.setText(
                post.getTitle() != null ? post.getTitle() : "No Title");
        holder.binding.postPriceMod.setText(
                post.getPrice() != null ? post.getPrice() : "N/A");

        // Load image with Glide
        String imageUrl = post.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.error_image)
                    .error(R.drawable.error_image)
                    .into(holder.binding.postImageMod);
        } else {
            holder.binding.postImageMod.setImageResource(R.drawable.error_image);
        }

        // Item click
        holder.itemView.setOnClickListener(v -> postClickListener.onPostClick(post));

        // Options menu (delete / approve)
        holder.binding.optionsMenuImageViewmod.setOnClickListener(v ->
                showPopupMenu(v, post));
    }

    private void showPopupMenu(View anchor, Post post) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(R.menu.moder_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_delete_post) {
                deleteClickListener.onDeleteClick(post);
                return true;
            } else if (id == R.id.action_approve_post) {
                approveClickListener.onApproveClick(post);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    /** If you need to refresh the list from outside: */
    public void updatePosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        final ItemPostModBinding binding;

        PostViewHolder(@NonNull ItemPostModBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Post post);
    }

    public interface OnApproveClickListener {
        void onApproveClick(Post post);
    }
}
