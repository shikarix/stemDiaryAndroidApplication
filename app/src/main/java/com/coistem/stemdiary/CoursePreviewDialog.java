package com.coistem.stemdiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

public class CoursePreviewDialog extends DialogFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView dialogCourseImage = view.findViewById(R.id.courseImageView);
        TextView dialogCourseTeacher = view.findViewById(R.id.teacherNameText);
        TextView dialogCourseDate = view.findViewById(R.id.nextLessonText);
        TextView dialogCourseName = view.findViewById(R.id.courseListName);
        dialogCourseName.setText("Крутой курс");
        Picasso.with(getContext()).load("ZALUPA").error(R.drawable.stem_logo).into(dialogCourseImage);
        dialogCourseTeacher.setText("КРУТОЙ УЧИТЕЛЬ КЛАССНЫЙ ТОП");
        dialogCourseDate.setText("22.12.22");
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder
                .setTitle("Диалоговое окно")
                .setView(R.layout.listview_courses)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "ок норм сохраняем", Toast.LENGTH_SHORT).show();
                    }
                })
                .setMessage("Так будет выглядить ваш курс. Сохранить?")
                .create();
    }
}
