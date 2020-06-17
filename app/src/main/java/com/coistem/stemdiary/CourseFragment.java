package com.coistem.stemdiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseFragment extends Fragment {

    public static final String TEACHER_NAMES = "teacher_names";
    public static final String COURSE_DATES = "course_dates";
    public static final String TEACHER_AVATARS = "teacher_avatars";
    public static final String HOMEWORKS = "homeworks";
    public static final String COURSE_NAMES = "course_names";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        RecyclerView recycler = view.findViewById(R.id.pupilsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recycler.setLayoutManager(layoutManager);
        PupilVkPagesAdapter pupilVkPagesAdapter = new PupilVkPagesAdapter();
        recycler.setAdapter(pupilVkPagesAdapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String teacherNames = arguments.getString(TEACHER_NAMES);
            String courseDates = arguments.getString(COURSE_DATES);
            String teacherAvatars = arguments.getString(TEACHER_AVATARS);
            String courseNames = arguments.getString(COURSE_NAMES);
            String homeworks = arguments.getString(HOMEWORKS);
            displayValues(view, teacherNames, teacherAvatars, courseDates, courseNames, homeworks);
        }
        return view;
    }

    private void displayValues(View v, String teacherName, String teacherAvatarUrl, String date, String courseName, String homework) {
        TextView courseNameText = v.findViewById(R.id.courseName);
        TextView courseDate = v.findViewById(R.id.dateOfLessonText);
        TextView teacherNameText = v.findViewById(R.id.teacherNameText);
        TextView homeworkText = v.findViewById(R.id.courseHomeWork);
        CircleImageView teacherAvatar = v.findViewById(R.id.teacherAvatarImage);

        courseNameText.setText(courseName);
        courseDate.setText(date);
        teacherNameText.setText(teacherName);
        homeworkText.setText(homework);
        Picasso.with(v.getContext()).load(teacherAvatarUrl).error(R.drawable.ic_warning).into(teacherAvatar);

    }

}