package com.coistem.stemdiary.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
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
import com.coistem.stemdiary.activities.MainActivity;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
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

    public void showPleaseWaitDialog() {

    }

    public void showStatusToast(int purchaseType, boolean isSuccess) {

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
            Picasso.with(itemView.getContext()).load(OurData.cartItemImageUrls[position]).error(R.drawable.stem_logo).placeholder(R.drawable.stem_logo).into(cartItemImage);
            cartAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PurchaseConnection purchaseConnection = new PurchaseConnection();
                    purchaseConnection.execute("accept", position, v.getContext());
                }
            });
            cartCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PurchaseConnection purchaseConnection = new PurchaseConnection();
                    purchaseConnection.execute("decline", position, v.getContext());
                }
            });

        }

    }

    private class PurchaseConnection extends AsyncTask {
        @Override
        protected void onPreExecute() {
            showPleaseWaitDialog();
        }

        @Override
        protected void onPostExecute(Object o) {
            updateCart();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            System.out.println(objects[1]);
            int position = (int) objects[1];
            if(objects[0].equals("accept")) {
                acceptProduct(OurData.cartItemIds[position]);
            } else if (objects[0].equals("decline")) {
                cancelProduct(OurData.cartItemIds[position]);
            }
            return objects;
        }

        private String cancelPurchase(String basketId) {
            try {
                Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/decline/")
                        .data("login", MainActivity.userLogin,
                                "password",MainActivity.userPassword,
                                "basketId", basketId).post();
                String text = document.text();
                System.out.println(text);
                if(text.equals("Я хз!")) {
                    return "hz";
                } else if (text.contains("Логин")) {
                    return SocketConnect.CONNECTION_ERROR;
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

        private String acceptPurchase(String basketId) {
            try {
                Document document = Jsoup.connect("http://"+ MainActivity.serverIp+"/confirm/")
                        .data("login", MainActivity.userLogin,
                                "password",MainActivity.userPassword,
                                "basketId", basketId).post();
                String text = document.text();
                System.out.println(text);
                if(text.equals("Я хз!")) {
                    return "hz";
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

        private void acceptProduct(Integer itemId) {
            String execute = acceptPurchase(itemId.toString());
            if (!execute.equals(SocketConnect.CONNECTION_ERROR)) {
                String[] databases = execute.split("Андроид ");
                execute = databases[1];
                System.out.println(execute);
                if (execute.equals("Good")) {
                    showStatusToast(0, true);
                } else if (execute.equals("Connection error")) {
                    showStatusToast(0, false);
                }
            } else {
                showStatusToast(0, false);
            }
        }

        private void cancelProduct(Integer itemId) {
            System.out.println("WE ARE IN CANCEL");
            String execute = cancelPurchase(itemId.toString());
            System.out.println(execute);
            if (!execute.equals(SocketConnect.CONNECTION_ERROR)) {
                String[] databases = execute.split("Андроид ");
                execute = databases[1];
                if (execute.equals("Good")) {
                    showStatusToast(1, true);
                } else if (execute.equals("Connection error")) {
                    showStatusToast(1, false);
                }
            }else {
                showStatusToast(1, false);
            }
        }
    }
}
