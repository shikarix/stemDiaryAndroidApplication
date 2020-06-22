package com.coistem.stemdiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ShopItemsListAdapter extends RecyclerView.Adapter {

    private AlertDialog haventEnoughMoney;
    private AlertDialog restartAppDialog;

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
        private TextView itemCostsView;
        private Button itemBuyButton;
        private int position;
        public ListViewHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView) itemView.findViewById(R.id.shopItemName);
            itemImageView = (ImageView) itemView.findViewById(R.id.shopItemIcon);
            itemCostsView = (TextView) itemView.findViewById(R.id.shopItemCost);
            itemBuyButton = (Button) itemView.findViewById(R.id.shopBuyButton);
            itemBuyButton.setOnClickListener(this);
        }
        public void bindView(int position) {
            itemTextView.setText(OurData.itemNames[position]);
            Picasso.with(itemView.getContext()).load(OurData.itemImageUrls[position]).error(R.drawable.ic_example_avatar).into(itemImageView);
            itemCostsView.setText("Цена: "+OurData.itemCosts[position]+"$");
            this.position = position;
        }

        public void onClick(View view) {
            makePurchase(OurData.itemIds[position], view.getContext());
        }
    }

    public void makePurchase(int itemId, Context context) {
        SocketConnect socketConnect = new SocketConnect();
        try {
            String execute = (String) socketConnect.execute(SocketConnect.MAKE_PURCHASE, itemId).get();
            String[] databases = execute.split("Андроид ");
            execute = databases[1];
            if(execute.equals("Good")) {
                Toast.makeText(context, "Предмет добавлен в вашу корзину.", Toast.LENGTH_SHORT).show();
            } else if(execute.equals("Not enough product!")) {
                Toast.makeText(context, "К сожалению, данного продукта нет в наличии.", Toast.LENGTH_SHORT).show();
            } else if(execute.equals("Not enough money!")) {
                Toast.makeText(context, "На вашем счету недостаточно стемкоинов для совершения покупки.", Toast.LENGTH_SHORT).show();
            } else if(execute.equals("Connection error")) {
                Toast.makeText(context, "Произошла ошибка при совершении покупки. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
