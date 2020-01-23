package com.coistem.stemdiary;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ShopItemsListAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_shop,viewGroup,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        int length = OurData.itemNames.length;
        return length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemTextView;
        private ImageView itemImageView;
        private int position;
        public ListViewHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView) itemView.findViewById(R.id.text1);
            itemImageView = (ImageView) itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position) {
            itemTextView.setText(OurData.itemNames[position]);
            Picasso.with(itemView.getContext()).load(OurData.itemImageUrls[position]).error(R.drawable.ic_example_avatar).into(itemImageView);
            this.position = position;
        }

        public void onClick(View view) {
            Toast.makeText(view.getContext(), "YA DEBILLL YOU CLICK ON "+position, Toast.LENGTH_SHORT).show();
        }
    }
}
