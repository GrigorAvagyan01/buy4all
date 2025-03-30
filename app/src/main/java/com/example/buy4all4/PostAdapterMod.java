package com.example.buy4all4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buy4all4.databinding.ItemPostModBinding;
import java.util.List;

public class PostAdapterMod extends RecyclerView.Adapter<PostAdapterMod.PostViewHolder> {
    private Context context;
    private List<Post> posts;
    private OnPostClickListener postClickListener;
    private OnImageButtonClickListener imageButtonClickListener;

    public PostAdapterMod(Context context, List<Post> posts, OnPostClickListener postClickListener, OnImageButtonClickListener imageButtonClickListener) {
        this.context = context;
        this.posts = posts;
        this.postClickListener = postClickListener;
        this.imageButtonClickListener = imageButtonClickListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPostModBinding binding = ItemPostModBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.binding.postTitleMod.setText(post.getTitle());
        holder.binding.postPriceMod.setText(post.getPrice());

        holder.itemView.setOnClickListener(v -> postClickListener.onPostClick(post));

        holder.binding.optionsMenuImageViewmod.setOnClickListener(v -> showPopupMenu(v, position));
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.moder_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                imageButtonClickListener.onImageButtonClick(posts.get(position));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ItemPostModBinding binding;

        public PostViewHolder(ItemPostModBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public interface OnImageButtonClickListener {
        void onImageButtonClick(Post post);
    }
}
