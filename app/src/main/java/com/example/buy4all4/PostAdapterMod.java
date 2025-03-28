package com.example.buy4all4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.buy4all4.databinding.ItemPostModBinding;
import java.util.List;

public class PostAdapterMod extends RecyclerView.Adapter<PostAdapterMod.PostViewHolder> {

    private Context context;
    private List<Post> postList;
    private OnItemClickListener onItemClickListener;
    private OnOptionsClickListener onOptionsClickListener;

    public PostAdapterMod(Context context, List<Post> postList, OnItemClickListener onItemClickListener, OnOptionsClickListener onOptionsClickListener) {
        this.context = context;
        this.postList = postList;
        this.onItemClickListener = onItemClickListener;
        this.onOptionsClickListener = onOptionsClickListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_mod, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.binding.postTitleMod.setText(post.getTitle());
        holder.binding.postPriceMod.setText(post.getPrice());

        if (post.getImageUrl() != null) {
            Glide.with(context).load(post.getImageUrl()).into(holder.binding.postImageMod);
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

        // Set up options menu (3 dots button)
        holder.binding.optionsMenuImageViewmod.setOnClickListener(v -> {
            // Create a PopupMenu when the options button is clicked
            PopupMenu popupMenu = new PopupMenu(context, holder.binding.optionsMenuImageViewmod);
            popupMenu.getMenuInflater().inflate(R.menu.moder_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete_post) {
                    onOptionsClickListener.onDeleteClick(post);  // Pass the post to ModerActivity for deletion
                    return true;
                }
                return false;
            });

            popupMenu.show();  // Show the menu
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // Interface for item click (to view post details)
    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    // Interface for options menu click (to handle delete or other actions)
    public interface OnOptionsClickListener {
        void onDeleteClick(Post post);  // Delete the post
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostModBinding binding;

        public PostViewHolder(View itemView) {
            super(itemView);
            binding = ItemPostModBinding.bind(itemView);
        }
    }
}
