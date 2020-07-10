package com.coistem.stemdiary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.coistem.stemdiary.entities.GetUserInfo;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.activities.EditLessonActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseFragment extends Fragment {

    public static final String TEACHER_NAMES = "teacher_names";
    public static final String COURSE_DATES = "course_dates";
    public static final String TEACHER_AVATARS = "teacher_avatars";
    public static final String HOMEWORKS = "homeworks";
    public static final String COURSE_NAMES = "course_names";
    public static final String RATES = "rates";
    public static final String WHICH_LESSON = "which";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
//        RecyclerView recycler = view.findViewById(R.id.pupilsRecyclerView);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        recycler.setLayoutManager(layoutManager);
//        PupilVkPagesAdapter pupilVkPagesAdapter = new PupilVkPagesAdapter();
//        recycler.setAdapter(pupilVkPagesAdapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String teacherNames = arguments.getString(TEACHER_NAMES);
            String courseDates = arguments.getString(COURSE_DATES);
            String teacherAvatars = arguments.getString(TEACHER_AVATARS);
            String courseNames = arguments.getString(COURSE_NAMES);
            String homeworks = arguments.getString(HOMEWORKS);
            String rates = arguments.getString(RATES);
            String lesson = arguments.getString(WHICH_LESSON);
            displayValues(view, teacherNames, teacherAvatars, courseDates, courseNames, homeworks, rates, lesson);
        }
        return view;
    }

    private void displayValues(View v, String teacherName, String teacherAvatarUrl, final String date, final String courseName, final String homework, String rates, final String whichLesson) {
        TextView rateAText = v.findViewById(R.id.rateA);
        TextView rateBText = v.findViewById(R.id.rateB);
        TextView rateCText = v.findViewById(R.id.rateC);
        TextView notRatedText = v.findViewById(R.id.notRatedText);
        TextView courseNameText = v.findViewById(R.id.courseName);
        TextView courseDate = v.findViewById(R.id.dateOfLessonText);
        TextView teacherNameText = v.findViewById(R.id.teacherNameText);
        TextView homeworkText = v.findViewById(R.id.courseHomeWork);
        CircleImageView teacherAvatar = v.findViewById(R.id.teacherAvatarImage);
        CardView cv = v.findViewById(R.id.cardView5);
        Button btn = v.findViewById(R.id.editRateButton);
        if(rates == null) {
            cv.removeAllViews();
        } else {
            if (!rates.equals("Not rated")) {
                notRatedText.setVisibility(View.INVISIBLE);
                rateAText.setVisibility(View.VISIBLE);
                rateBText.setVisibility(View.VISIBLE);
                rateCText.setVisibility(View.VISIBLE);
                String[] rate = rates.split(",");
                String rateA = rate[0];
                String rateB = rate[1];
                String rateC = rate[2];
                rateAText.setText("Поведение и дисциплина: " + rateA);
                rateBText.setText("Работа на уроке: " + rateB);
                rateCText.setText("Домашнее задание: " + rateC);
            } else {
                notRatedText.setVisibility(View.VISIBLE);
                rateAText.setVisibility(View.INVISIBLE);
                rateBText.setVisibility(View.INVISIBLE);
                rateCText.setVisibility(View.INVISIBLE);
            }
        }
        courseNameText.setText(courseName);
        courseDate.setText(date);
        teacherNameText.setText(teacherName);
        homeworkText.setText(homework);
        Picasso.with(v.getContext()).load(teacherAvatarUrl).error(R.drawable.stem_logo).into(teacherAvatar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditLessonActivity.class);
                intent.putExtra(EditLessonActivity.EDIT_COURSENAME, courseName);
                intent.putExtra(EditLessonActivity.EDIT_DATE, date);
                intent.putExtra(EditLessonActivity.EDIT_HOMEWORK, homework);
                intent.putExtra(EditLessonActivity.WHICH_LESSON, whichLesson);
                getActivity().startActivityForResult(intent, 0);
            }
        });
        if (!(GetUserInfo.userAccessType.equals("ADMIN") || GetUserInfo.userAccessType.equals("TEACHER"))) {
            btn.setVisibility(View.INVISIBLE);
        }
    }

}