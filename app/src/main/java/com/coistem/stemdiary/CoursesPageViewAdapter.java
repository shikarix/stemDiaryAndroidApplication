package com.coistem.stemdiary;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CoursesPageViewAdapter extends FragmentPagerAdapter {


    public static String teacherName = "";
    public static String teacherAvatarUrl = "";
    public static String courseName = "";
    private String[] homeworks = OurData.currentHomeworks;
    private String[] courseDates = OurData.currentLessonsDates;

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
