package com.coistem.stemdiary;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AddingTimetableActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_timetable);




        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursePreviewDialog cpd = new CoursePreviewDialog();
                cpd.show(getSupportFragmentManager(), "fdk");
            }
        });

    }

}
