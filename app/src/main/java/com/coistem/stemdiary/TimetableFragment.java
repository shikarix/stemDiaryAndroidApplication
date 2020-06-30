package com.coistem.stemdiary;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<String> teacherAvatarUrls = new ArrayList<>();

    private ArrayList<String[]> homeworks = new ArrayList<>();
    private ArrayList<String[]> lessonDates = new ArrayList<>();

    private RecyclerView recyclerView;
    private boolean isEmpty = true;
    private ProgressBar progressBar;
    private TextView nothingText;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddingTimetableActivity.class);
                startActivity(intent);
            }
        });
        nothingText = view.findViewById(R.id.nothingInTimeTableText);
        nothingText.setVisibility(View.INVISIBLE);
        return view;
    }

    private void takeCourses(String log, String pass) {
        SocketConnect socketConnect = new SocketConnect();
        String courses = "";
        try {
            courses = (String)socketConnect.execute(SocketConnect.GET_COURSES,log, pass).get();
            if(courses.equals(SocketConnect.CONNECTION_ERROR) || courses.equals(SocketConnect.GO_DALEKO)) {
                if(getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Возникла ошибка при получении данных с сервера.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                String[] databases = courses.split("Андроид ");
                courses = databases[1];
                System.out.println(courses);
                if (courses.equals("[]")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nothingText.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    isEmpty = false;
                    parseCourses(courses);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        OurData.courseDates = null;
        OurData.courseImageUrls = null;
        OurData.courseNames = null;
        OurData.courseTeachers = null;
        super.onDestroyView();
    }

    @Override
    public void onResume() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                takeCourses(MainActivity.userLogin, MainActivity.userPassword);
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            if (!isEmpty) {
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                CoursestListAdapter coursestListAdapter = new CoursestListAdapter();
                                recyclerView.setAdapter(coursestListAdapter);
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        }).start();


        super.onResume();
    }

    private void parseCourses(String jsonFile) {
        OurData.courseDates = null;
        OurData.courseImageUrls = null;
        OurData.courseNames = null;
        OurData.courseTeachers = null;
        courseImages.clear();
        courseTeachers.clear();
        courseNames.clear();
        courseDates.clear();
        try {
            JSONArray jsonArray = new JSONArray(jsonFile);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String courseName = jsonObject.getString("courseName");
                String preDate = jsonObject.getString("preDate");
                String date = jsonObject.getString("date");
                String postDate = jsonObject.getString("postDate");
                String[] dates = new String[]{preDate, date, postDate};
                String teacherName = jsonObject.getString("teacherName");
                String avatarUrl = jsonObject.getString("avatarUrl");
                String preHomework = jsonObject.getString("preHomework");
                String postHomework = jsonObject.getString("postHomework");
                String homework = jsonObject.getString("homework");
                String teacherAvatarUrl = jsonObject.getString("teacherAvatarUrl");
                String[] homeworkk = new String[]{preHomework,homework,postHomework};
                teacherAvatarUrls.add(teacherAvatarUrl);
                lessonDates.add(dates);
                homeworks.add(homeworkk);
                courseNames.add(courseName);
                courseDates.add(date);
                courseTeachers.add(teacherName);
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
            OurData.homeworks = new String[homeworks.size()];
            OurData.homeworks = homeworks.toArray();
            OurData.lessonsDates = new String[lessonDates.size()];
            OurData.lessonsDates = lessonDates.toArray();
            OurData.courseTeachersAvatarUrls = new String[teacherAvatarUrls.size()];
            OurData.courseTeachersAvatarUrls = teacherAvatarUrls.toArray(OurData.courseTeachersAvatarUrls);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
