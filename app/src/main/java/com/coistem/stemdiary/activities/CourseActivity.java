package com.coistem.stemdiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.coistem.stemdiary.R;
import com.coistem.stemdiary.adapters.CoursesPageViewAdapter;

public class CourseActivity extends AppCompatActivity {
    CoursesPageViewAdapter pageViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        pageViewAdapter = new CoursesPageViewAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.coursesPager);
        pager.setAdapter(pageViewAdapter);
        pager.setCurrentItem(1);
    }
}