package com.coistem.stemdiary.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coistem.stemdiary.OurData;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.SocketConnect;
import com.coistem.stemdiary.activities.MainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
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
        private TextView itemCountsView;
        private int position;
        public ListViewHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView) itemView.findViewById(R.id.shopItemName);
            itemImageView = (ImageView) itemView.findViewById(R.id.shopItemIcon);
            itemCostsView = (TextView) itemView.findViewById(R.id.shopItemCost);
            itemCountsView = (TextView) itemView.findViewById(R.id.shopCountText);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position) {
            itemTextView.setText(OurData.itemNames[position]);
            Picasso.with(itemView.getContext()).load(OurData.itemImageUrls[position]).error(R.drawable.ic_example_avatar).into(itemImageView);
            itemCostsView.setText("Цена: "+OurData.itemCosts[position]+"$");
            itemCountsView.setText("Остаток: "+OurData.itemCounts[position].toString()+" шт.");
            this.position = position;
        }
        
        public void onClick(View view) {
            MakePurchaseConnection makePurchaseConnection = new MakePurchaseConnection();
            makePurchaseConnection.execute(OurData.itemIds[position]);
        }
    }

    public void openPleaseWait() {

    }

    public void updateShop() {

    }

    public void showToast(String text) {

    }

    public void makePurchase(int itemId) {
        String execute = purchaseSomething(itemId);
        if(!execute.equals(SocketConnect.CONNECTION_ERROR)) {
            String[] databases = execute.split("Андроид ");
            execute = databases[1];
            switch (execute) {
                case "Good":
                    showToast("Предмет добавлен в вашу корзину!");
                    break;
                case "Not enough product!":
                    showToast("К сожалению, данного продукта нет в наличии");
                    break;
                case "Not enough money!":
                    showToast("На вашем счету недостаточно стемкоинов для совершения покупки.");
                    break;
            }
        } else {
            showToast("Произошла ошибка при совершении покупки. Повторите попытку позже.");
        }
    }

    private String purchaseSomething(Integer id) {
        try {
            Document document = Jsoup.connect("http://"+ MainActivity.serverIp+"/buy/")
                    .data("login", MainActivity.userLogin,
                            "password",MainActivity.userPassword,
                            "productId", id.toString()).post();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Not enough product!")) {
                return "Not enough product!";
            } else if(text.equals("Not enough money!")) {
                return "Not enough money!";
            } else if(text.equals("Good")) {
                return "Good";
            } else if(text.equals("Go daleko!")) {
                return "Connection error";
            } else {
                return text;
            }
        } catch (IOException e) {
            return "Connection error";
        }
    }

    private class MakePurchaseConnection extends AsyncTask {

        @Override
        protected void onPreExecute() {
            openPleaseWait();
        }

        @Override
        protected void onPostExecute(Object o) {
            updateShop();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            makePurchase((Integer) objects[0]);
            return objects;
        }
    }
}
