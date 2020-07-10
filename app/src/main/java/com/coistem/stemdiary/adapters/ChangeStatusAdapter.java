package com.coistem.stemdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coistem.stemdiary.OurData;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.entities.AdminCartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeStatusAdapter extends RecyclerView.Adapter {

    private TextView adminCartItemName;
    private Spinner statusSpinner;
    private List<AdminCartItem> itemsList;
    private ArrayList<AdminCartItem> changedList;

    public ChangeStatusAdapter(ArrayList<AdminCartItem> list) {
        itemsList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_changestatus,viewGroup,false);
        return new ChangeStatusAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ChangeStatusAdapter.ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public ArrayList<AdminCartItem> getChangedList() {
        return changedList;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {

        private View itemView;
        private int position;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            changedList = new ArrayList<>();
            adminCartItemName = itemView.findViewById(R.id.adminCartName);
            statusSpinner = itemView.findViewById(R.id.status_spinner);

        }

        public void bindView(int position) {
            this.position = position;
            changedList.clear();
            AdminCartItem adminCartItem = itemsList.get(position);
            adminCartItemName.setText(adminCartItem.name);
            String[] stringArray = itemView.getResources().getStringArray(R.array.item_statuses);
            List<String> strings = Arrays.asList(stringArray);
            int index = strings.indexOf(adminCartItem.status);
            statusSpinner.setSelection(index);
            statusSpinner.setOnItemSelectedListener(this);
        }
        int count=0;
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(count >= 1) {
                AdminCartItem changedItem = new AdminCartItem();
                AdminCartItem adminCartItem = itemsList.get(this.position);
                String[] stringArray = itemView.getResources().getStringArray(R.array.item_statuses);
                changedItem.name = adminCartItem.name;
                changedItem.status = stringArray[position];
                changedItem.id = adminCartItem.id;
                changedList.add(changedItem);
            }
            count++;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}