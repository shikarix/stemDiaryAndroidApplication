package com.coistem.stemdiary;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CartItemsAdapter extends RecyclerView.Adapter {

    private ImageView cartItemImage;
    private TextView cartItemName;
    private TextView cartItemCost;
    private Button cartAcceptButton;
    private Button cartCancelButton;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_cart,viewGroup,false);
        return new CartItemsAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((CartItemsAdapter.ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return OurData.cartItemNames.length;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder {

        private int position;

        public ListViewHolder(View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cartItemLogo);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemCost = itemView.findViewById(R.id.cartItemCost);
            cartAcceptButton = itemView.findViewById(R.id.buyCartButton);
            cartCancelButton = itemView.findViewById(R.id.cancelCartButton);

        }

        public void bindView(int position) {
            cartItemName.setText(OurData.cartItemNames[position]);
            cartItemCost.setText("Цена: " + OurData.cartItemCosts[position] + "$");
            Picasso.with(itemView.getContext()).load(OurData.cartItemImageUrls[position]).error(R.drawable.stem_logo).into(cartItemImage);
            cartAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Ю прессед аццепт баттон", Toast.LENGTH_SHORT).show();
                }
            });
            cartCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Ю прессед канкел баттон", Toast.LENGTH_SHORT).show();
                }
            });
            this.position = position;
        }
    }
}
