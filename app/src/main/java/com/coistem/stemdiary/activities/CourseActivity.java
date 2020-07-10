package com.coistem.stemdiary.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.coistem.stemdiary.R;
import com.coistem.stemdiary.adapters.CoursesPageViewAdapter;

public class CourseActivity extends AppCompatActivity {
    CoursesPageViewAdapter pageViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        pageViewAdapter = new CoursesPageViewAdapter(getSupportFragmentManager()){
            @Override
            public void returnToHome() {
                finish();
            }
        };
        ViewPager pager = findViewById(R.id.coursesPager);
        pager.setAdapter(pageViewAdapter);
        pager.setCurrentItem(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == 1) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}