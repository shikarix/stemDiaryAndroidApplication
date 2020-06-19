package com.coistem.stemdiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AddingTimetableActivity extends AppCompatActivity {

    private AlertDialog selectStudentsDialog;

    private AlertDialog alertDialog;
    private Spinner teacherSpinner;

    private EditText courseNameEditText;
    private EditText courseImageEditText;
    private TextView dateText;

    private MaterialCalendarView calendar;


    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> logins = new ArrayList<>();

    private String courseName;
    private String teacherLogin;
    private String teacherName;
    private long courseDate;
    private String courseImageUrl = "";
    private String[] coursePupils;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_timetable);
        Button addCourseButton = findViewById(R.id.addCourseButton);
        Button selectStudentsButton = findViewById(R.id.selectStudentsButton);
        teacherSpinner = findViewById(R.id.teacherSpinner);
        courseNameEditText = findViewById(R.id.courseNameText);
        courseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                courseName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        courseImageEditText = findViewById(R.id.courseImageEditText);
        courseImageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                courseImageUrl = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        calendar = findViewById(R.id.calendarView);
        dateText = findViewById(R.id.selectedTimeText);
        dateText.setText("Выберите дату и время.");
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                year = date.getYear();
                month = date.getMonth();
                day = date.getDay();
                openTimeDialog();
            }
        });
        selectStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStudentsDialog.show();
            }
        });
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!courseImageUrl.equals(" ") && courseName!=null && teacherName!=null && year != 0 && coursePupils!=null) {
                    try {
                        if (coursePupils[0]!="") {
                            createDialog();
                        }
                    }catch (ArrayIndexOutOfBoundsException e) {
                        Toast.makeText(AddingTimetableActivity.this, "Вы забыли выбрать учеников.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AddingTimetableActivity.this, "Вы должны заполнить все поля.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                takeTeachers();
                takePupils();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(OurData.teacherNames);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddingTimetableActivity.this, R.layout.support_simple_spinner_dropdown_item, OurData.teacherNames);
                        teacherSpinner.setAdapter(adapter);
                        teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                teacherLogin = OurData.teacherLogins[position];
                                teacherName = OurData.teacherNames[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        for (int i = 0; i < OurData.pupilsIsSelected.length; i++) {
                            System.out.println(OurData.pupilsNames[i]);
                        }
                        AlertDialog.Builder bui = new AlertDialog.Builder(AddingTimetableActivity.this)
                                .setMultiChoiceItems(OurData.pupilsNames, OurData.pupilsIsSelected, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        OurData.pupilsIsSelected[which] = isChecked;
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ArrayList<String> peoples = new ArrayList<>();
                                        for (int i = 0; i < OurData.pupilsNames.length; i++) {
                                            if (OurData.pupilsIsSelected[i]) {
                                                peoples.add(OurData.pupilsLogins[i]);
                                            }
                                        }
                                        coursePupils = new String[peoples.size()];
                                        coursePupils = peoples.toArray(coursePupils);
                                        selectStudentsDialog.dismiss();
                                    }
                                });
                        selectStudentsDialog = bui.create();
                    }
                });
            }
        }).start();
    }

    private void openTimeDialog() {
        TimePickerDialog timepickerdialog = new TimePickerDialog(AddingTimetableActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateText(year,month,day,hourOfDay,minute);
            }
        },12,0,true);
        timepickerdialog.show();
    }

    private void updateText(int y,int m,int d, int h, int min) {
        Date date = new Date(y, m, d, h, min);
        courseDate = date.getTime();
        dateText.setText("Выбранная дата: "+d+"."+m+"."+y+" | "+h+":"+min);
    }

    private void createDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.listview_courses, null);
        AlertDialog.Builder previewCourseDialog = new AlertDialog.Builder(AddingTimetableActivity.this)
                .setView(view)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AddingTimetableActivity.this, "ок норм сохраняем", Toast.LENGTH_SHORT).show();
                    }
                })
                .setMessage("Так будет выглядить ваш курс. Сохранить?");

        ImageView dialogCourseImage = view.findViewById(R.id.courseImageView);
        TextView dialogCourseTeacher = view.findViewById(R.id.teacherNameText);
        TextView dialogCourseDate = view.findViewById(R.id.nextLessonText);
        TextView dialogCourseName = view.findViewById(R.id.courseListName);

        dialogCourseName.setText(courseName);
        Picasso.with(AddingTimetableActivity.this).load(courseImageUrl).error(R.drawable.stem_logo).into(dialogCourseImage);
        dialogCourseTeacher.setText("Учитель:\n"+teacherName);
        dialogCourseDate.setText("Ближайшее занятие:\n"+day+"."+month+"."+year);
        alertDialog = previewCourseDialog.create();
        alertDialog.show();
    }

    private void addCourse() {
        SocketConnect socketConnect = new SocketConnect();
        String teachers = (String) socketConnect.execute(SocketConnect.GET_ALL_TEACHERS).get();
        if(teachers.equals(SocketConnect.CONNECTION_ERROR) || teachers.equals(SocketConnect.GO_DALEKO)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddingTimetableActivity.this, "Возникла ошибка при получении данных с сервера.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String[] words = teachers.split("Андроид ");
            teachers = words[1];
            parseTeachers(teachers);
        }
    }

    private void takeTeachers() {
        try {
            SocketConnect socketConnect = new SocketConnect();
            String teachers = (String) socketConnect.execute(SocketConnect.GET_ALL_TEACHERS).get();
            if(teachers.equals(SocketConnect.CONNECTION_ERROR) || teachers.equals(SocketConnect.GO_DALEKO)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddingTimetableActivity.this, "Возникла ошибка при получении данных с сервера.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                String[] words = teachers.split("Андроид ");
                teachers = words[1];
                parseTeachers(teachers);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void parseTeachers(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String surname = jsonObject.getString("surname");
                String login = jsonObject.getString("login");
                names.add(name + " " + surname);
                logins.add(login);
            }
            OurData.teacherNames = new String[names.size()];
            OurData.teacherNames = names.toArray(OurData.teacherNames);
            OurData.teacherLogins = new String[logins.size()];
            OurData.teacherLogins = logins.toArray(OurData.teacherLogins);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void takePupils() {
        try {
            SocketConnect socketConnect = new SocketConnect();
            String pupils = (String) socketConnect.execute(SocketConnect.GET_ALL_PUPILS).get();
            if(pupils.equals(SocketConnect.CONNECTION_ERROR) || pupils.equals(SocketConnect.GO_DALEKO)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddingTimetableActivity.this, "Возникла ошибка при получении данных с сервера.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                String[] words = pupils.split("Андроид ");
                pupils = words[1];
                parsePupils(pupils);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void parsePupils(String json) {
        names = new ArrayList<>();
        logins = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String surname = jsonObject.getString("surname");
                String login = jsonObject.getString("login");
                names.add(name + " " + surname);
                logins.add(login);
                System.out.println(login);
            }
            OurData.pupilsNames = new String[names.size()];
            OurData.pupilsNames = names.toArray(OurData.pupilsNames);
            OurData.pupilsLogins = new String[logins.size()];
            OurData.pupilsLogins = logins.toArray(OurData.pupilsLogins);
            OurData.pupilsIsSelected = new boolean[names.size()];
            Arrays.fill(OurData.pupilsIsSelected, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
