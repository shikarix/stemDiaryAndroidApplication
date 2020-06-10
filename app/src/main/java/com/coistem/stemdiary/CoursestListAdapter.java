package com.coistem.stemdiary;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CoursestListAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_courses,viewGroup,false);
        return new CoursestListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((CoursestListAdapter.ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return OurData.courseNames.length;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView courseTextView;
        private ImageView courseImageView;
        private TextView courseDateView;
        private TextView courseTeacherTextView;
        private Button goToCourseButton;

        private int position;

        public ListViewHolder(View itemView) {
            super(itemView);
            courseTextView = (TextView) itemView.findViewById(R.id.courseName);
            courseImageView = (ImageView) itemView.findViewById(R.id.courseImageView);
            courseDateView = (TextView) itemView.findViewById(R.id.nextLessonText);
            courseTeacherTextView = (TextView) itemView.findViewById(R.id.teacherNameText);
            goToCourseButton = (Button) itemView.findViewById(R.id.goToTheCourseButton);

//            itemView.setOnClickListener(this);
//            itemImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), ImageActivity.class);
//                    ImageActivity.image = OurData.imgUrls[position];
//                    v.getContext().startActivity(intent);
//                }
//            });
        }
        public void bindView(int position) {
            courseTextView.setText(OurData.courseNames[position]);
            Picasso.with(itemView.getContext()).load(OurData.courseImageUrls[position]).error(R.drawable.ic_example_avatar).into(courseImageView);
            String date = OurData.courseDates[position];
            System.out.println();
            courseDateView.setText(date);
            courseTeacherTextView.setText(OurData.courseTeachers[position]);
            goToCourseButton.setOnClickListener(this);
            this.position = position;
        }

        public void onClick(View view) {
            Toast.makeText(view.getContext(), "URA!", Toast.LENGTH_SHORT).show();
        }
    }
}
