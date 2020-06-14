package com.coistem.stemdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        private int position;
        public ListViewHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView) itemView.findViewById(R.id.text1);
            itemImageView = (ImageView) itemView.findViewById(R.id.icon);
            itemCostsView = (TextView) itemView.findViewById(R.id.cost);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position) {
            itemTextView.setText(OurData.itemNames[position]);
            Picasso.with(itemView.getContext()).load(OurData.itemImageUrls[position]).error(R.drawable.ic_example_avatar).into(itemImageView);
            itemCostsView.setText("Цена: "+OurData.itemCosts[position]+"$");
            this.position = position;
        }

        public void onClick(View view) {
            SocketConnect socketConnect = new SocketConnect();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()).setMessage("Недостаточно средств для покупки.").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    haventEnoughMoney.cancel();
                }
            });
            haventEnoughMoney = builder.create();
            AlertDialog.Builder restBuild = new AlertDialog.Builder(view.getContext())
                    .setMessage("Чтобы ваша аватарка отображалась, необходимо перезапустить приложение.\n Перезапустить сейчас?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restartAppDialog.cancel();
                        }
                    });
            restartAppDialog = restBuild.create();
            try {
                String purchase = (String) socketConnect.execute("purchase", position).get();
                String[] databases = purchase.split("Database");
                purchase = databases[1];
                JSONObject jsonObject = new JSONObject(purchase);
                boolean isBuy = jsonObject.getBoolean("isBuy");
                if(isBuy) {
                    int balance = jsonObject.getInt("balance");
                    TextView balanceTxt = ShopFragment.balanceTxt;
                    GetUserInfo.userCounterCoins = balance;
                    balanceTxt.setText("Ваш баланс: "+GetUserInfo.userCounterCoins+" коинов");
                    if(position==3) {
                        restartAppDialog.show();
                    }
                } else {
                    haventEnoughMoney.show();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
