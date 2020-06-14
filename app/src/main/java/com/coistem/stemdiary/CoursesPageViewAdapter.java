package com.coistem.stemdiary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CoursesPageViewAdapter extends FragmentPagerAdapter {

    String[] courseDates = new String[]{"27.06.20", "28.06.20","29.06.20"};
    String[] homeworks = new String[]{"Изучить среду разработки Lego EV-3", "Подготовка к контрольному тестированию по сборке робота \"Пятиминутка\"", "Ничего не задано."};
    String teacherName = "Елисеенко Юрий";
    String teacherAvatarUrl = "https://sun9-55.userapi.com/c639831/v639831860/5afc/nPV_f_uJFV0.jpg";
    String courseName = "Робототехника EV3. Первый год.";


    public CoursesPageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putString(CourseFragment.HOMEWORKS, homeworks[position]);
        arguments.putString(CourseFragment.COURSE_NAMES, courseName);
        arguments.putString(CourseFragment.TEACHER_AVATARS, teacherAvatarUrl);
        arguments.putString(CourseFragment.TEACHER_NAMES, teacherName);
        arguments.putString(CourseFragment.COURSE_DATES, courseDates[position]);

        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(arguments);

        return courseFragment;
    }

    @Override
    public int getCount() {
        return courseDates.length;
    }
}
