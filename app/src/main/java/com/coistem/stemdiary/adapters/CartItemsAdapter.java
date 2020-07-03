package com.coistem.stemdiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coistem.stemdiary.OurData;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.SocketConnect;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

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

    public void updateCart() {

    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        public ListViewHolder(View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cartItemLogo);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemCost = itemView.findViewById(R.id.cartItemCost);
            cartAcceptButton = itemView.findViewById(R.id.buyCartButton);
            cartCancelButton = itemView.findViewById(R.id.cancelCartButton);

        }

        public void bindView(final int position) {
            cartItemName.setText(OurData.cartItemNames[position]);
            cartItemCost.setText("Цена: " + OurData.cartItemCosts[position] + "$");
            Picasso.with(itemView.getContext()).load(OurData.cartItemImageUrls[position]).error(R.drawable.stem_logo).into(cartItemImage);
            cartAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptProduct(OurData.cartItemIds[position],v.getContext());
                    updateCart();
                }
            });
            cartCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelProduct(OurData.cartItemIds[position], v.getContext());
                    updateCart();
                }
            });

        }

        private void acceptProduct(Integer itemId, Context context) {
            SocketConnect socketConnect = new SocketConnect();
            try {
                String execute = (String) socketConnect.execute(SocketConnect.ACCEPT_PURCHASE, itemId.toString()).get();
                String[] databases = execute.split("Андроид ");
                execute = databases[1];
                if (execute.equals("Good")) {
                    Toast.makeText(context, "Покупка подтверждена!", Toast.LENGTH_SHORT).show();
                } else if (execute.equals("Connection error")) {
                    Toast.makeText(context, "Произошла ошибка. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        private void cancelProduct(Integer itemId, Context context) {
            SocketConnect socketConnect = new SocketConnect();
            try {
                String execute = (String) socketConnect.execute(SocketConnect.CANCEL_PURCHASE, itemId.toString()).get();
                if (!execute.equals(SocketConnect.CONNECTION_ERROR)) {
                    String[] databases = execute.split("Андроид ");
                    execute = databases[1];
                    if (execute.equals("Good")) {
                        Toast.makeText(context, "Покупка отклонена!", Toast.LENGTH_SHORT).show();
                    } else if (execute.equals("Connection error")) {
                        Toast.makeText(context, "Произошла ошибка. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Произошла ошибка. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}
