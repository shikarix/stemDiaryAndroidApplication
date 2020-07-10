package com.coistem.stemdiary.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coistem.stemdiary.entities.GetUserInfo;
import com.coistem.stemdiary.fragments.CourseFragment;
import com.coistem.stemdiary.OurData;

public class CoursesPageViewAdapter extends FragmentPagerAdapter {


    public static String teacherName = "";
    public static String teacherAvatarUrl = "";
    public static String courseName = "";
    private String[] homeworks = OurData.currentHomeworks;
    private String[] courseDates = OurData.currentLessonsDates;
    private String[] rates = OurData.currentRates;

    public CoursesPageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    public void returnToHome() {

    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putString(CourseFragment.HOMEWORKS, homeworks[position]);
        arguments.putString(CourseFragment.COURSE_NAMES, courseName);
        arguments.putString(CourseFragment.TEACHER_AVATARS, teacherAvatarUrl);
        arguments.putString(CourseFragment.TEACHER_NAMES, teacherName);
        arguments.putString(CourseFragment.COURSE_DATES, courseDates[position]);
        if (!(GetUserInfo.userAccessType.equals("ADMIN") || GetUserInfo.userAccessType.equals("TEACHER"))) {
            arguments.putString(CourseFragment.RATES, rates[position]);
        } else {
            arguments.putString(CourseFragment.RATES, null);
        }
        switch (position) {
            case 0: {
                arguments.putString(CourseFragment.WHICH_LESSON, "pre");
                break;
            }
            case 1: {
                arguments.putString(CourseFragment.WHICH_LESSON, "now");
                break;
            }
            case 2: {
                arguments.putString(CourseFragment.WHICH_LESSON, "post");
                break;
            }
        }
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(arguments);

        return courseFragment;
    }

    @Override
    public int getCount() {
        return courseDates.length;
    }
}
