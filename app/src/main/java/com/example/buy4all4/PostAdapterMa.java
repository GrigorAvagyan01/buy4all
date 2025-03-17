package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ItemPostMaBinding;
import java.util.List;

public class PostAdapterMa extends RecyclerView.Adapter<PostAdapterMa.PostViewHolder> {

    private Context context;
    private List<Post> postList;
    private OnPostOptionsClickListener onPostOptionsClickListener;

    public PostAdapterMa(Context context, List<Post> postList, OnPostOptionsClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.onPostOptionsClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostMaBinding binding = ItemPostMaBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.binding.postTitleMa.setText(post.getTitle() != null ? post.getTitle() : "No Title");
        holder.binding.postPriceMa.setText(post.getPrice() != null ? post.getPrice() : "No Price");

        if (post.getImageUrl() != null) {
            Glide.with(context)
                    .load(post.getImageUrl())
                    .into(holder.binding.postImageMa);
        }

        // 🔥 Open PostDetailActivity when clicking on a post
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

        // 🛠 Show Popup Menu (Edit/Delete Options)
        holder.binding.optionsMenuImageViewMa.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                showPopupMenu(v, pos, post);
            }
        });
    }

    private void showPopupMenu(View view, int position, Post post) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.post_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (onPostOptionsClickListener != null) {
                if (item.getItemId() == R.id.action_edit) {
                    onPostOptionsClickListener.onEditPost(post.getPostId());
                } else if (item.getItemId() == R.id.action_delete) {
                    onPostOptionsClickListener.onDeletePost(post.getPostId());
                }
            }
            return true;
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnPostOptionsClickListener {
        void onEditPost(String postId);
        void onDeletePost(String postId);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostMaBinding binding;

        public PostViewHolder(ItemPostMaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
