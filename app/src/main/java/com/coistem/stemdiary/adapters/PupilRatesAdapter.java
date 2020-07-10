package com.coistem.stemdiary.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coistem.stemdiary.OurData;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.SocketConnect;
import com.coistem.stemdiary.activities.EditLessonActivity;

import java.util.concurrent.ExecutionException;

public class PupilRatesAdapter extends RecyclerView.Adapter {

    private TextView pupilName;
    private RadioGroup progressOnLesson;
    private RadioGroup disciplineGroup;
    private RadioGroup homeworkGroup;
    private Button applyRateButton;
    private Context context;
    
    public PupilRatesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_rates,viewGroup,false);
        return new PupilRatesAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((PupilRatesAdapter.ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return EditLessonActivity.names.length;
    }


    private class ListViewHolder extends RecyclerView.ViewHolder {

        private int discRate;
        private int progressRate;
        private int homeworkRate;
        
        public ListViewHolder(View itemView) {
            super(itemView);
            pupilName = itemView.findViewById(R.id.pupilName);
            disciplineGroup = itemView.findViewById(R.id.disciplineGroup);
            progressOnLesson = itemView.findViewById(R.id.progreessOnLessonGroup);
            homeworkGroup = itemView.findViewById(R.id.homeworkRateGroup);
            applyRateButton = itemView.findViewById(R.id.applyRateBtn);
        }

        public void bindView(final int position) {
            Integer[] pupilMark = (Integer[]) EditLessonActivity.pupilMarks[position];
            pupilName.setText(EditLessonActivity.names[position]);
            if(pupilMark[0] != -1) {
                RadioButton discButton = (RadioButton) disciplineGroup.getChildAt(pupilMark[0]);
                RadioButton progressButton = (RadioButton) progressOnLesson.getChildAt(pupilMark[1]);
                RadioButton homeworkButton = (RadioButton) homeworkGroup.getChildAt(pupilMark[2]);
                discButton.setChecked(true);
                progressButton.setChecked(true);
                homeworkButton.setChecked(true);
//                applyRateButton.setText("Суммарно ученик получит: "+pupilMark[3]+" стемкоинов");
            }
            disciplineGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.disc0: discRate = 0; break;
                        case R.id.disc1: discRate = 1; break;
                        case R.id.disc2: discRate = 2; break;
                        case R.id.disc3: discRate = 3; break;
                        case R.id.disc4: discRate = 4; break;
                        case R.id.disc5: discRate = 5; break;
                    }
//                    int summ = Math.round((discRate + progressRate + homeworkRate) / 3f);
//                    applyRateButton.setText("Суммарно ученик получит: "+summ+" стемкоинов");
                }
            });
            progressOnLesson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.progress0: progressRate = 0; break;
                        case R.id.progress1: progressRate = 1; break;
                        case R.id.progress2: progressRate = 2; break;
                        case R.id.progress3: progressRate = 3; break;
                        case R.id.progress4: progressRate = 4; break;
                        case R.id.progress5: progressRate = 5; break;
                    }
//                    int summ = Math.round((discRate + progressRate + homeworkRate) / 3f);
//                    applyRateButton.setText("Суммарно ученик получит: "+summ+" стемкоинов");
                }
            });
            homeworkGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.hw0: homeworkRate = 0; break;
                        case R.id.hw1: homeworkRate = 1; break;
                        case R.id.hw2: homeworkRate = 2; break;
                        case R.id.hw3: homeworkRate = 3; break;
                        case R.id.hw4: homeworkRate = 4; break;
                        case R.id.hw5: homeworkRate = 5; break;
                    }
//                    int summ = Math.round((discRate + progressRate + homeworkRate) / 3f);
//                    applyRateButton.setText("Суммарно ученик получит: "+summ+" стемкоинов");
                }
            });
            applyRateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
//                    Toast.makeText(itemView.getContext(), "setStudentRate/log/pass/"+discRate+"/"+progressRate+"/"+homeworkRate+"/"+EditLessonActivity.courseNameText.getText().toString()+"/"+EditLessonActivity.dateOfLesson.getText().toString()+"/"+EditLessonActivity.pupilLogins[position], Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String courseName = EditLessonActivity.courseNameText.getText().toString();
                            String date = EditLessonActivity.dateOfLesson.getText().toString();
                            String pupilLogin = EditLessonActivity.pupilLogins[position];
                            String pupilName = EditLessonActivity.names[position];
                            setRate(v,discRate,progressRate,homeworkRate, courseName, date,pupilLogin, pupilName);
                        }
                    }).start();
                }
            });
        }
        private void setRate(final View view, int discRate, int progressRate, int homeworkRate, String courseName, String date, String pupilLogin, final String pupilName) {
            System.out.println(courseName);
            System.out.println(date);
            System.out.println(pupilLogin);
            System.out.println(pupilName);
            SocketConnect socketConnect = new SocketConnect();
            String rating = "";
            try {
                rating = (String)socketConnect.execute(SocketConnect.SEND_RATE, courseName, date, pupilLogin, discRate, progressRate, homeworkRate).get();
                if(rating.equals(SocketConnect.CONNECTION_ERROR) || rating.equals(SocketConnect.GO_DALEKO)) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "При оценивании произошла ошибка.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    String[] databases = rating.split("Андроид ");
                    rating = databases[1];
                    System.out.println(rating);
                    if(rating.equals("true")) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(), "Ученик " + pupilName +" успешно оценен.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } if(rating.equals("already")) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(), "Ученик " + pupilName +" уже оценен. Установленная оценка не будет учитываться.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
