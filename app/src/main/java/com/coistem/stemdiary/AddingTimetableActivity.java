package com.coistem.stemdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

public class AddingTimetableActivity extends Activity implements View.OnClickListener {
    AlertDialog.Builder selectStudentsBuilder;
    AlertDialog selectStudentsDialog;
    String selectedDate = "";
    String selectedCourse = "";
    String selectedPupils = "";
    ArrayList<String> list = new ArrayList<>();
    private TextView addingInfoTxt;
    public static final int ID_ERROR = -100;
    @Override
    public void onClick(View v) {
//        int idInArray = findIdInArray(selectedCourse);
//        String sendDate = selectedDate+", "+idInArray;
//        Intent intent = new Intent();
//        intent.putExtra("date", sendDate);
//        setResult(RESULT_OK, intent);
//        String date = intent.getStringExtra("date");
//        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
//        finish();
    }
    protected void textUpdate(){
        selectedPupils = list.toString();
        addingInfoTxt.setText("Курс: "+selectedCourse+"\nУченики: "+selectedPupils+"\nДата: "+selectedDate);
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_adding_timetable);
//        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
//        Spinner spinner = findViewById(R.id.courseSpinner);
//        addingInfoTxt = findViewById(R.id.addingInfo);
//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                int day = date.getDay();
//                int month = date.getMonth();
//                int year = date.getYear();
//                selectedDate=""+day+'.'+month+'.'+year;
//                textUpdate();
//            }
//        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                String[] stringArray = getResources().getStringArray(R.array.courses_names);
////                selectedCourse = stringArray[position];
////                textUpdate();
////                Toast.makeText(getContext(), "Selected: "+stringArray[position], Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        Button setTimetableBtn = findViewById(R.id.setTimetableBtn);
//        setTimetableBtn.setOnClickListener(this);
//        final String[] array = getResources().getStringArray(R.array.students);
//        final boolean[] mCheckedItems = new boolean[array.length];
//        selectStudentsBuilder = new AlertDialog.Builder(AddingTimetableActivity.this).setMultiChoiceItems(array, mCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                mCheckedItems[i]=b;
//            }
//        }).setPositiveButton("YA NOOB", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                for (int ii = 0; ii < array.length; ii++) {
//                    if (mCheckedItems[ii]) {
//                        list.add(array[ii]);
//                    }
//                }
//                Toast.makeText(AddingTimetableActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
//                selectStudentsDialog.cancel();
//                textUpdate();
//            }
//        });
//        selectStudentsDialog = selectStudentsBuilder.create();
//        Button setStudentsBtn = findViewById(R.id.selectStudentsBtn);
//        setStudentsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectStudentsDialog.show();
//            }
//        });
//    }
//    private int findIdInArray(String value) {
//        String[] array = getResources().getStringArray(R.array.courses_names);
//        for (int i = 0; i < array.length; i++) {
//            if(array[i].equals(value)) {
//                Log.d("Array value: ",array[i]);
//                return i;
//            }
//        }
//        return ID_ERROR;
//    }
}
