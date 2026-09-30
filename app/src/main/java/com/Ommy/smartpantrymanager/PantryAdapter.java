package com.Ommy.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Ommy.smartpantrymanager.data.PantryItem;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    // Lets the screen decide what happens when a row is tapped
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    private List<PantryItem> items;
    private OnItemClickListener listener;

    public PantryAdapter(List<PantryItem> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Replaces the list and tells the RecyclerView to redraw
    public void setItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // Holds the views of one row so they can be reused when scrolling
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDetails = itemView.findViewById(R.id.textDetails);
        }
    }

    // Creates a new empty row from item_pantry.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    // Fills a row with the data of the item at this position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.textName.setText(item.name);

        String quantity;
        if (item.quantity == (long) item.quantity) {
            quantity = String.valueOf((long) item.quantity);   // show 2 instead of 2.0
        } else {
            quantity = String.valueOf(item.quantity);
        }

        String details = quantity + " " + item.unit;
        if (item.expiryDate != null && !item.expiryDate.isEmpty()) {
            details += "  |  Expires: " + item.expiryDate;
        }
        holder.textDetails.setText(details);

        // Tell the screen which item was tapped
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    // Tells the RecyclerView how many rows there are
    @Override
    public int getItemCount() {
        return items.size();
    }
}