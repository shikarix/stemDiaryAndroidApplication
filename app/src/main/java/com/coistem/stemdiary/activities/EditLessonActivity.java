package com.coistem.stemdiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coistem.stemdiary.R;
import com.coistem.stemdiary.SocketConnect;
import com.coistem.stemdiary.adapters.CoursesPageViewAdapter;
import com.coistem.stemdiary.adapters.PupilRatesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EditLessonActivity extends AppCompatActivity {

    public static final String EDIT_DATE = "date";
    public static final String EDIT_COURSENAME = "courseName";
    public static final String EDIT_HOMEWORK = "homework";
    public static final String WHICH_LESSON = "something";

    public static JSONArray pupils;

    private static String whichlesson = "";

    private ArrayList<String> pupilNames = new ArrayList<>();
    private ArrayList<Integer[]> marks = new ArrayList<>();
    private ArrayList<String> logins = new ArrayList<>();

    public static TextView courseNameText;
    public static TextView dateOfLesson;
    private EditText homeworkEdit;


    public static Integer courseId;
    public static String[] names;
    public static Object[] pupilMarks;
    public static String[] pupilLogins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lesson);

        String date = getIntent().getStringExtra(EDIT_DATE);
        String courseName = getIntent().getStringExtra(EDIT_COURSENAME);
        String homework = getIntent().getStringExtra(EDIT_HOMEWORK);
        whichlesson = getIntent().getStringExtra(WHICH_LESSON);
        parsePupils();
        dateOfLesson = findViewById(R.id.editDateOfLesson);
        courseNameText = findViewById(R.id.editCourseName);
        homeworkEdit = findViewById(R.id.homeworkEditText);
        Button undoButton = findViewById(R.id.cancelEditButton);
        Button acceptButton = findViewById(R.id.saveEditButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SocketConnect socketConnect = new SocketConnect();
                        try {
                            Looper.prepare();
                            String sendedHomework = (String) socketConnect.execute(SocketConnect.SEND_HOMEWORK, courseNameText.getText().toString(), dateOfLesson.getText().toString(), homeworkEdit.getText().toString()).get();
                            if(sendedHomework.contains("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(EditLessonActivity.this, "Курс успешно обновлен!", Toast.LENGTH_SHORT).show();
                                        setResult(1);
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(EditLessonActivity.this, "При обновлении курса произошла ошибка.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        courseNameText.setText(courseName);
        dateOfLesson.setText(date);
        if(!homework.equals("Кажется, ничего нет!")) {
            homeworkEdit.setText(homework);
        }
    }

    private Integer[] parseMark(JSONArray array) {
        try {
            if (array.get(0) != null) {

                ArrayList<Integer> marks = new ArrayList<>();
                for (int j = 0; j < array.length(); j++) {
                    marks.add(array.getInt(j));
                }
                Integer[] mrks = new Integer[marks.size()];
                mrks = marks.toArray(mrks);
                return mrks;
            } else {
                return new Integer[]{-1};
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Integer[]{-1};
    }

    private void parsePupils() {
        for (int i = 0; i < pupils.length(); i++) {
            try {
                JSONObject jsonObject = pupils.getJSONObject(i);
                String name = jsonObject.getString("name");
                String login = jsonObject.getString("login");
                switch (whichlesson) {
                    case "pre": {
                        JSONArray markArray = jsonObject.getJSONArray("preMark");
                        Integer[] parsedMarks = parseMark(markArray);
                        marks.add(parsedMarks);
                        break;
                    }
                    case "now": {
                        JSONArray markArray = jsonObject.getJSONArray("mark");
                        Integer[] parsedMark = parseMark(markArray);
                        marks.add(parsedMark);
                        break;
                    }
                    case "post": {
                        JSONArray markArray = jsonObject.getJSONArray("postMark");
                        Integer[] parsedMark = parseMark(markArray);
                        marks.add(parsedMark);
                        break;
                    }
                }
                pupilNames.add(name);
                logins.add(login);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        names = new String[pupilNames.size()];
        names = pupilNames.toArray(names);
        pupilLogins = new String[logins.size()];
        pupilLogins = logins.toArray(pupilLogins);
        pupilMarks = new Object[marks.size()];
        pupilMarks = marks.toArray(pupilMarks);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            System.out.println(name);
        }
        RecyclerView recyclerView = findViewById(R.id.pupilsRateList);
        PupilRatesAdapter pupilRatesAdapter = new PupilRatesAdapter(EditLessonActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditLessonActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pupilRatesAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
}