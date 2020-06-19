package com.coistem.stemdiary;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsListAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_vknews,viewGroup,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return 20;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemTextView;
        private ImageView itemImageView;
        private TextView itemDateView;
        private int position;

        public ListViewHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView) itemView.findViewById(R.id.newsText);
            itemImageView = (ImageView) itemView.findViewById(R.id.newsImage);
            itemDateView = (TextView) itemView.findViewById(R.id.newsDate);
            itemView.setOnClickListener(this);
            itemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ImageActivity.class);
                    ImageActivity.image = OurData.imgUrls[position];
                    v.getContext().startActivity(intent);
                }
            });
        }
        public void bindView(int position) {
            itemTextView.setText(OurData.title[position]);
            Picasso.with(itemView.getContext()).load(OurData.imgUrls[position]).error(R.drawable.ic_example_avatar).into(itemImageView);
            String date = OurData.dates[position];
            System.out.println();
            itemDateView.setText(date);
            this.position = position;
        }

        public void onClick(View view) {
            TextView viewById = view.findViewById(R.id.newsText);
            String zalupa = viewById.getText().toString();
            if(zalupa.contains("Большей новостей в нашей группе ВКонтакте!")) {
                Uri address = Uri.parse("https://vk.com/coistem");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                view.getContext().startActivity(openLinkIntent);
            } else {
                Uri postUrl = Uri.parse(OurData.urlsForPost[position]);
                Intent openPostLink = new Intent(Intent.ACTION_VIEW, postUrl);
                view.getContext().startActivity(openPostLink);
            }
        }
    }

}
