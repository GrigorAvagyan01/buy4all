package com.example.buy4all4;

import android.content.Context;
import android.util.Log;
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

        if (post.getTitle() != null) {
            holder.binding.postTitle.setText(post.getTitle());
        } else {
            holder.binding.postTitle.setText("No Title");
        }

        if (post.getPrice() != null) {
            holder.binding.postPrice.setText(post.getPrice());
        } else {
            holder.binding.postPrice.setText("No Price");
        }

        if (post.getImageUrl() != null) {
            Glide.with(context)
                    .load(post.getImageUrl())
                    .into(holder.binding.postImage);
        }

        holder.binding.optionsMenuImageView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                onPostOptionsClickListener.onPostOptionsClicked(v, pos, post);
            }
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
