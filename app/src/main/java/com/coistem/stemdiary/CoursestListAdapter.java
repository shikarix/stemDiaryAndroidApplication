package com.coistem.stemdiary;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
            Picasso.with(itemView.getContext()).load(OurData.courseImageUrls[position]).error(R.drawable.stem_logo).into(courseImageView);
            String date = OurData.courseDates[position];
            courseDateView.setText("Ближайшее занятие: \n"+date);
            courseTeacherTextView.setText("Учитель: \n" +OurData.courseTeachers[position]);
            goToCourseButton.setOnClickListener(this);
            this.position = position;
        }

        public void onClick(View view) {
            OurData.currentHomeworks = (String[]) OurData.homeworks[position];
            OurData.currentLessonsDates = (String[]) OurData.lessonsDates[position];
            CoursesPageViewAdapter.courseName = OurData.courseNames[position];
            CoursesPageViewAdapter.teacherName = OurData.courseTeachers[position];
            CoursesPageViewAdapter.teacherAvatarUrl = OurData.courseTeachersAvatarUrls[position];
//            CoursesPageViewAdapter.teacherAvatarUrl = OurData.courseTeachersAvatarUrls[position];
            OurData.coursePupilPagesNames = new String[]{"Еремин Вадим", "Васильев Алексей", "Елисеенко Юрий", "Елисеенко Елена", "Санников Андрей","gfsdjsdk dsfsdf","test test"};
            OurData.coursePupilPagesAvatarUrls = new String[]{"https://sun9-66.userapi.com/c858024/v858024463/1f87bb/NlhZRSdDYhs.jpg", "https://sun9-9.userapi.com/eH5iDosQUdDq3G9n_XGkUUtJC8NY-8hH07WhOQ/vVfUWo-I0-E.jpg", "https://sun9-55.userapi.com/c639831/v639831860/5afc/nPV_f_uJFV0.jpg" ,"https://sun9-24.userapi.com/c857732/v857732187/c15d9/lWGWrXTYE3A.jpg", "https://sun3-11.userapi.com/K8UjK12Ee6jYkQnTszcYDlbZHoByaJQeds7VfQ/UY5uxtinD-8.jpg","some","some"};
            OurData.coursePupilPagesUrls = new String[]{"https://vk.com/vadim.eryomin", "https://vk.com/45gaz", "https://vk.com/yeliseenko", "https://vk.com/eue24","https://vk.com/nazipozhizni","страница тест челика", "страница тест челика2"};
            Intent intent = new Intent(view.getContext(), CourseActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
