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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private final Context context;
    private List<Service> serviceList;
    private final OnItemClickListener onItemClickListener;
    private final OnActionClickListener onActionClickListener;

    public ServiceAdapter(Context context, List<Service> serviceList,
                          OnItemClickListener itemClickListener,
                          OnActionClickListener actionClickListener) {
        this.context = context;
        this.serviceList = serviceList;
        this.onItemClickListener = itemClickListener;
        this.onActionClickListener = actionClickListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostModBinding binding = ItemPostModBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void updateData(List<Service> newServices) {
        serviceList = newServices;
        notifyDataSetChanged();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostModBinding binding;

        public ServiceViewHolder(ItemPostModBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Service service) {
            binding.postTitleMod.setText(service.getTitle() != null ? service.getTitle() : "No Title");
            binding.postPriceMod.setText(formatPrice(service.getPrice(), service.getCurrency()));

            if (service.getImageUrl() != null && !service.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(service.getImageUrl())
                        .placeholder(R.drawable.error_image)
                        .into(binding.postImageMod);
            }

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(service);
                }
            });

            binding.optionsMenuImageViewmod.setOnClickListener(v ->
                    showActionMenu(v, service));
        }

        private void showActionMenu(View anchorView, Service service) {
            PopupMenu popupMenu = new PopupMenu(context, anchorView);
            popupMenu.getMenuInflater().inflate(R.menu.moder_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_delete) {
                    onActionClickListener.onDeleteClick(service);
                    return true;
                } else if (itemId == R.id.action_edit) {
                    onActionClickListener.onEditClick(service);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }
    }

    private String formatPrice(String price, String currency) {
        if (price == null || price.isEmpty()) return "N/A";

        if (currency == null) return price;

        switch (currency) {
            case "USD": return "$" + price;
            case "EUR": return "€" + price;
            case "AMD": return "֏" + price;
            default: return price + " " + currency;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Service service);
    }

    public interface OnActionClickListener {
        void onDeleteClick(Service service);
        void onEditClick(Service service);
    }
}
