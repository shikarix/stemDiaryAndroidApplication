package com.coistem.stemdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartItemStatusAdapter extends RecyclerView.Adapter {

    private TextView cartItemName;
    private TextView cartItemStatus;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_inworkitems,viewGroup,false);
        return new CartItemStatusAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((CartItemStatusAdapter.ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return OurData.inWorkItemNames.length;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder {

        public ListViewHolder(View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.inWorkItemName);
            cartItemStatus = itemView.findViewById(R.id.inWorkItemStatus);

        }

        public void bindView(int position) {
            cartItemName.setText(OurData.inWorkItemNames[position]);
            cartItemStatus.setText(OurData.inWorkItemStatuses[position]);
        }
    }
}
