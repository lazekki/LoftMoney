package com.loftschool.ozaharenko.loftmoney;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemsList = new ArrayList<Item>();

    private final int colorId;

    public ItemsAdapter(int colorId) {
        this.colorId = colorId;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);

        return new ItemViewHolder(itemView, colorId);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindItem(mItemsList.get(position));
    }

    public void addItem(Item item) {
        mItemsList.add(0, item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;

        public ItemViewHolder(@NonNull View itemView, final int colorId) {
            super(itemView);

            mNameView = itemView.findViewById(R.id.name_view);
            mPriceView = itemView.findViewById(R.id.price_view);
            final Context context = mPriceView.getContext();
            mPriceView.setTextColor(ContextCompat.getColor(context, colorId));
            };

        public void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(
                    mPriceView.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(item.getPrice())));
        }
    }
}
