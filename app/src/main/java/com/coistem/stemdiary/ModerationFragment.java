package com.coistem.stemdiary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ModerationFragment extends Fragment {

    private Button applyChangesBtn;
    private Spinner courseSpinner;
    private View view;
    
    private int disciplineRate;
    private int progressRate;
    private int addScoreRate;

    String[] dayOfWeek = { "Понедельник", "Вторник", "Среда", "Четверг",
            "Котопятница", "Субкота", "Воскресенье" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moderation, container, false);

        courseSpinner = view.findViewById(R.id.courseSpinner);
        getCourses();
        applyChangesBtn = view.findViewById(R.id.applyChangesButton);
        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Поведение: "+disciplineRate+"\nУспеваемость: "+progressRate+"\nДоп. Баллы: "+addScoreRate, Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter  =  new  ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, dayOfWeek);
        courseSpinner.setAdapter(adapter);

        RadioGroup discGroup = (RadioGroup) view.findViewById(R.id.discGroup);
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

        RadioGroup progressGroup = (RadioGroup) view.findViewById(R.id.progressGroup);

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

        RadioGroup addScoreGroup = (RadioGroup) view.findViewById(R.id.addScoreGroup);

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
        
        
        return view;
    }

    public void parseCourses(String jsonFile) {
        try {
            JSONArray jsonArray = new JSONArray(jsonFile);
            for (int i = 0; i < jsonArray.length(); i++) {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getCourses() {
        SocketConnect socketConnect = new SocketConnect();
        String courses = "";
        try {
            courses = (String)socketConnect.execute("getTeacherCourses").get();
            String[] databases = courses.split("Database");
            courses = databases[1];
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(courses);
        parseCourses(courses);
    }



}
