package com.coistem.stemdiary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class TimetableFragment extends Fragment {

    private ArrayList<String> courseNames = new ArrayList<>();
    private ArrayList<String> courseImages = new ArrayList<>();
    private ArrayList<String> courseDates = new ArrayList<>();
    private ArrayList<String> courseTeachers = new ArrayList<>();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        recyclerView = view.findViewById(R.id.coursesList);
        progressBar = view.findViewById(R.id.timetableProgressBar);
        fab = view.findViewById(R.id.timetableFab);
        fab.hide();
        if (GetUserInfo.userAccessType.equals("ADMIN") || GetUserInfo.userAccessType.equals("TEACHER")) {
            fab.show();
        }
        return view;
    }

    private void takeCourses(String log, String pass) {
        SocketConnect socketConnect = new SocketConnect();
        String courses = "";
        try {
            courses = (String)socketConnect.execute(SocketConnect.GET_COURSES,log, pass).get();
            String[] databases = courses.split("Андроид ");
            courses = databases[1];
            System.out.println(courses);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        parseCourses(courses);
    }

    @Override
    public void onResume() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                takeCourses(MainActivity.userLogin, MainActivity.userPassword);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        CoursestListAdapter coursestListAdapter = new CoursestListAdapter();
                        recyclerView.setAdapter(coursestListAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

        super.onResume();
    }

    private void parseCourses(String jsonFile) {
        try {
            JSONArray jsonArray = new JSONArray(jsonFile);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String courseName = jsonObject.getString("courseName");
                String date = jsonObject.getString("date");
                String teacherName = jsonObject.getString("teacherName");
                String avatarUrl = jsonObject.getString("avatarUrl");
                courseNames.add(courseName);
                courseDates.add("Ближайшее занятие: \n"+ date);
                courseTeachers.add("Учитель: \n" + teacherName);
                courseImages.add(avatarUrl);
            }
            OurData.courseNames = new String[courseNames.size()];
            OurData.courseImageUrls = new String[courseImages.size()];
            OurData.courseTeachers = new String[courseTeachers.size()];
            OurData.courseDates = new String[courseDates.size()];
            OurData.courseNames = courseNames.toArray(OurData.courseNames);
            OurData.courseImageUrls = courseImages.toArray(OurData.courseImageUrls);
            OurData.courseTeachers = courseTeachers.toArray(OurData.courseTeachers);
            OurData.courseDates = courseDates.toArray(OurData.courseDates);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
