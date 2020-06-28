package com.coistem.stemdiary;

import android.content.Context;
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

import java.util.concurrent.ExecutionException;

public class CartItemsAdapter extends RecyclerView.Adapter {

    private ImageView cartItemImage;
    private TextView cartItemName;
    private TextView cartItemCost;
    private Button cartAcceptButton;
    private Button cartCancelButton;

    public static int position;

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


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int position;
        private int whichOperaion = -1;

        public ListViewHolder(View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cartItemLogo);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemCost = itemView.findViewById(R.id.cartItemCost);
            cartAcceptButton = itemView.findViewById(R.id.buyCartButton);
            cartCancelButton = itemView.findViewById(R.id.cancelCartButton);

        }

        public void bindView(int position) {
            CartItemsAdapter.position = position;
            cartItemName.setText(OurData.cartItemNames[position]);
            cartItemCost.setText("Цена: " + OurData.cartItemCosts[position] + "$");
            Picasso.with(itemView.getContext()).load(OurData.cartItemImageUrls[position]).error(R.drawable.stem_logo).into(cartItemImage);
            cartAcceptButton.setOnClickListener(this);
            cartCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Ю прессед канкел баттон", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void acceptProduct(Integer itemId, Context context) {
            SocketConnect socketConnect = new SocketConnect();
            try {
                String execute = (String) socketConnect.execute(SocketConnect.MAKE_PURCHASE, itemId.toString()).get();
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

        @Override
        public void onClick(View v) {
            if(whichOperaion == 0) {
                acceptProduct(OurData.cartItemIds[position], v.getContext());
            } else if (whichOperaion == 1) {
//                Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
