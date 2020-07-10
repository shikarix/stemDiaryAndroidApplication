package com.coistem.stemdiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.coistem.stemdiary.NextLessonService;
import com.coistem.stemdiary.OurData;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.SocketConnect;
import com.coistem.stemdiary.entities.GetUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static final String SETTINGS_PREF = "settings";
    public static final String NOTIFY_PREF = "notify";
    private boolean isSwitched = false;
    private AlertDialog warningDialog;
    private Switch notifySwitch;
    private AlertDialog processDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSharedPreferences();
        notifySwitch = findViewById(R.id.notifyAboutTimetable);
        notifySwitch.setChecked(isSwitched);
        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    showDialog();
                } else {
                    setPref(NOTIFY_PREF, false);
                }
            }
        });
        processDialog = new AlertDialog.Builder(this)
                .setTitle("Получаем данные о курсах...")
                .setView(R.layout.pleasewaitdialog)
                .setCancelable(false)
                .create();
        CardView backButton = findViewById(R.id.backCardView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDialog() {
        warningDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Внимание!")
                .setMessage("Мы не несём ответственность за корректность присланных оповещений об уроках.\nИными словами, не получится отмазаться, что приложение тебя не оповестило)")
                .setPositiveButton("Понял", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        warningDialog.dismiss();
                        GetCourseDates getCourseDates = new GetCourseDates();
                        getCourseDates.execute();
                    }
                })
                .create();
        warningDialog.show();
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS_PREF, MODE_PRIVATE);
        isSwitched = sharedPreferences.getBoolean(NOTIFY_PREF, false);
    }

    private void setPref(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    private String getCourses(String login, String password, Boolean isTeacher) {
        try {
            Document document = null;
            if(!isTeacher) {
                document = Jsoup.connect("http://" + MainActivity.serverIp + "/getPupilCourses")
                        .data("login", login)
                        .data("password", password).post();
            } else {
                document = Jsoup.connect("http://" + MainActivity.serverIp + "/getTeacherCourses")
                        .data("login", login)
                        .data("password", password).post();
            }
            String text = document.text();
            System.out.println(text);
            if (text.contains("Логин")) {
                return SocketConnect.GO_DALEKO;
            } else {
                return text;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return SocketConnect.CONNECTION_ERROR;
    }

    private class GetCourseDates extends AsyncTask {
        @Override
        protected void onPreExecute() {
            processDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if(o.equals("error")) {
                Toast.makeText(SettingsActivity.this, "Возникла ошибка при получении курсов.", Toast.LENGTH_SHORT).show();
                processDialog.dismiss();
                setPref(NOTIFY_PREF, false);
                notifySwitch.setChecked(false);
            } else {
                processDialog.dismiss();
                setPref(NOTIFY_PREF, true);
                if(!isServiceRunning(NextLessonService.class)) {
                    Intent intent = new Intent(SettingsActivity.this, NextLessonService.class);
                    intent.putExtra("date", (String[]) o);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                }
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String courses = "";
            switch (GetUserInfo.userAccessType) {
                case "TEACHER":
                    courses = getCourses(MainActivity.userLogin, MainActivity.userPassword, true);
                    break;
                case "ADMIN":
                    // do nothing
                    break;
                default:
                    courses = getCourses(MainActivity.userLogin, MainActivity.userPassword, false);
                    break;
            }
            String[] strings = parseCourses(courses);
            return strings;
        }

        private String[] parseCourses(String jsonFile) {
            if (!jsonFile.equals(SocketConnect.CONNECTION_ERROR) && !jsonFile.equals(SocketConnect.GO_DALEKO)) {
                String[] words = jsonFile.split("Андроид ");
                jsonFile = words[1];
                ArrayList<String> dates = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonFile);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        dates.add(date);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] courseDates = new String[dates.size()];
                courseDates = dates.toArray(courseDates);
                return courseDates;
            }
            return new String[]{"error"};
        }
    }

}