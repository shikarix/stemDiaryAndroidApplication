package com.coistem.stemdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ModerationFragment extends Fragment {

    private Spinner courseSpinner;
    private Spinner lessonsSpinner;

    private View view;

    private AlertDialog selectStudentDialog;
    private AlertDialog.Builder ssdBuilder;

    private TextView infoAboutTxt;
    private TextView disciplineTxt;
    private TextView progressTxt;
    private TextView addScoreTxt;

    private RadioGroup discGroup;
    private RadioGroup progressGroup;
    private RadioGroup addScoreGroup;

    private Button selectStudentButton;
    private Button applyChangesBtn;

    private ProgressBar moderationProgressBar;

    private int disciplineRate = -1;
    private int progressRate = -1;
    private int addScoreRate = -1;

    String[] courseNames;
    String[] lessonsDates;
    String[] studentsNames;

    String selectedStudent = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moderation, container, false);

        selectStudentButton = view.findViewById(R.id.selectStudentBtn);

        disciplineTxt = view.findViewById(R.id.disciplineTxt);
        progressTxt = view.findViewById(R.id.progressTxt);
        addScoreTxt = view.findViewById(R.id.additionalScore);
        infoAboutTxt = view.findViewById(R.id.infoAboutTableText);

        moderationProgressBar = view.findViewById(R.id.moderationPrBar);

        selectStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentsNames==null) {
                    Toast.makeText(getContext(), "Сначала необходимо выбрать урок.", Toast.LENGTH_SHORT).show();
                } else {
                    ssdBuilder = new AlertDialog.Builder(getContext()).setTitle("Выберите ученика").setItems(studentsNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String student = studentsNames[which];
                            selectedStudent = student;
                            selectStudentButton.setText(student);
                            selectStudentDialog.show();
                        }
                    }).setCancelable(true);
                    selectStudentDialog = ssdBuilder.create();
                    selectStudentDialog.show();
                }
            }
        });
        courseSpinner = view.findViewById(R.id.courseSpinner);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLessons(courseNames[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lessonsSpinner = view.findViewById(R.id.lessonsSpinner);
        lessonsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println((String) courseSpinner.getSelectedItem());
                getStudents((String) courseSpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        applyChangesBtn = view.findViewById(R.id.applyChangesButton);
        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedStudent.equals("") && disciplineRate != -1 && addScoreRate != -1 && progressRate != -1) {
                    Toast.makeText(view.getContext(), "Ученик: " + selectedStudent + "\nПоведение: " + disciplineRate + "\nУспеваемость: " + progressRate + "\nДоп. Баллы: " + addScoreRate, Toast.LENGTH_SHORT).show();
                    sendRate((String) courseSpinner.getSelectedItem(), (String) lessonsSpinner.getSelectedItem());
                } else {
                    Toast.makeText(getContext(), "Для начала выберите оцените ученика.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getCourses();
        discGroup = (RadioGroup) view.findViewById(R.id.discGroup);
        discGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(view.getContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.discRate0:
                        disciplineRate = 0;
                        break;
                    case R.id.discRate1:
                        disciplineRate = 1;
                        break;
                    case R.id.discRate2: {
                        disciplineRate = 2;
                        break;
                    }
                    case R.id.discRate3: {
                        disciplineRate = 3;
                        break;
                    }
                    case R.id.discRate4: {
                        disciplineRate = 4;
                        break;
                    }
                    case R.id.discRate5: {
                        disciplineRate = 5;
                        break;
                    }

                    default:
                        break;
                }
            }
        });

        progressGroup = (RadioGroup) view.findViewById(R.id.progressGroup);

        progressGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(view.getContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.progressRate0:
                        progressRate = 0;
                        break;
                    case R.id.progressRate1:
                        progressRate = 1;
                        break;
                    case R.id.progressRate2: {
                        progressRate = 2;
                        break;
                    }
                    case R.id.progressRate3: {
                        progressRate = 3;
                        break;
                    }
                    case R.id.progressRate4: {
                        progressRate = 4;
                        break;
                    }
                    case R.id.progressRate5: {
                        progressRate = 5;
                        break;
                    }

                    default:
                        break;
                }
            }
        });

        addScoreGroup = (RadioGroup) view.findViewById(R.id.addScoreGroup);

        addScoreGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(view.getContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addScoreRate0:
                        addScoreRate = 0;
                        break;
                    case R.id.addScoreRate1:
                        addScoreRate = 1;
                        break;
                    case R.id.addScoreRate2: {
                        addScoreRate = 2;
                        break;
                    }
                    case R.id.addScoreRate3: {
                        addScoreRate = 3;
                        break;
                    }
                    case R.id.addScoreRate4: {
                        addScoreRate = 4;
                        break;
                    }
                    case R.id.addScoreRate5: {
                        addScoreRate = 5;
                        break;
                    }

                    default:
                        break;
                }
            }
        });

        setItemsVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void parseCourses(String jsonFile) {
        ArrayList<String> names = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonFile);
            JSONArray jsonArray = object.getJSONArray("courses");
            for (int i = 0; i < jsonArray.length(); i++) {
                String courseName = jsonArray.getString(i);
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String courseName = jsonObject.getString("courseName");
                names.add(courseName);

            }
            courseNames = new String[names.size()];
            courseNames = names.toArray(courseNames);
            ArrayAdapter<String> adapter  =  new  ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, courseNames);
            courseSpinner.setAdapter(adapter);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseStudents(String jsonFile) {
        ArrayList<String> names = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonFile);
            JSONArray jsonArray = object.getJSONArray("students");
            for (int i = 0; i < jsonArray.length(); i++) {
                String courseName = jsonArray.getString(i);
                names.add(courseName);
            }
            studentsNames = new String[names.size()];
            studentsNames = names.toArray(studentsNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseLessons(String json) {
        ArrayList<String> less = new ArrayList<>();
        System.out.println("from parse: "+json);
        try {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("lessons");
            for (int i = 0; i < jsonArray.length(); i++) {
                less.add(jsonArray.getString(i));
            }
            lessonsDates = new String[less.size()];
            lessonsDates = less.toArray(lessonsDates);
            ArrayAdapter<String> adapter  =  new  ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, lessonsDates);
            lessonsSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLessons(String courseName) {
        SocketConnect socketConnect = new SocketConnect();
        String getCourseLessons = "";
        try {
            getCourseLessons = (String) socketConnect.execute(SocketConnect.COURSE_LESSONS, courseName).get();
            String[] databases = getCourseLessons.split("Андроид");
            getCourseLessons = databases[1];
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        parseLessons(getCourseLessons);
    }
    public void getCourses() {
        SocketConnect socketConnect = new SocketConnect();
        String courses = "";
        try {
            courses = (String)socketConnect.execute(SocketConnect.TEACHER_COURSES).get();
            String[] databases = courses.split("Андроид");
            courses = databases[1];
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(courses);
        parseCourses(courses);
    }

    public void getStudents(String courseName) {
        SocketConnect socketConnect = new SocketConnect();
        String students = "";
        try {
            students = (String)socketConnect.execute(SocketConnect.LESSON_STUDENTS,courseName).get();
            System.out.println(students);
            String[] databases = students.split("Андроид");
            students = databases[1];
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(students);
        parseStudents(students);
    }

    public void sendRate(String courseName, String date) {
        SocketConnect socketConnect = new SocketConnect();
        String students = "";
        try {
            students = (String)socketConnect.execute(SocketConnect.SEND_RATE,courseName,date,selectedStudent,disciplineRate,progressRate,addScoreRate).get();
//            String[] databases = courses.split("Database");
//            courses = databases[1];
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if(students.equals("Success!")) {
            Toast.makeText(getContext(), "Hurray! Send rate is success!", Toast.LENGTH_SHORT).show();
        }

    }

    public void setItemsVisibility(int visibility) {
        lessonsSpinner.setVisibility(visibility);
        courseSpinner.setVisibility(visibility);
        applyChangesBtn.setVisibility(visibility);
        selectStudentButton.setVisibility(visibility);
        discGroup.setVisibility(visibility);
        addScoreGroup.setVisibility(visibility);
        progressGroup.setVisibility(visibility);
        infoAboutTxt.setVisibility(visibility);
        disciplineTxt.setVisibility(visibility);
        progressTxt.setVisibility(visibility);
        addScoreTxt.setVisibility(visibility);
        if(visibility==View.VISIBLE) {
            moderationProgressBar.setVisibility(View.INVISIBLE);
        } else {
            moderationProgressBar.setVisibility(View.VISIBLE);
        }
    }

}
