package com.coistem.stemdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class PupilVkPagesAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_pupilspages,viewGroup,false);
        return new PupilVkPagesAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((PupilVkPagesAdapter.ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return OurData.coursePupilPagesNames.length;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView pupilTextView;
        private ImageView pupilImageView;

        private int position;

        public ListViewHolder(View itemView) {
            super(itemView);
            pupilTextView = (TextView) itemView.findViewById(R.id.vkPupilName);
            pupilImageView = (ImageView) itemView.findViewById(R.id.vkAvatar);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position) {
            pupilTextView.setText(OurData.coursePupilPagesNames[position].toString());
            Picasso.with(itemView.getContext()).load(OurData.coursePupilPagesAvatarUrls[position]).error(R.drawable.ic_example_avatar).into(pupilImageView);
            this.position = position;
        }

        public void onClick(View view) {
            Toast.makeText(view.getContext(), OurData.coursePupilPagesUrls[position], Toast.LENGTH_SHORT).show();
        }
    }
}
