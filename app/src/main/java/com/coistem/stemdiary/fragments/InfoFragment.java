package com.coistem.stemdiary.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.coistem.stemdiary.activities.SettingsActivity;
import com.coistem.stemdiary.entities.GetUserInfo;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.activities.LoginActivity;
import com.coistem.stemdiary.activities.MainActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class InfoFragment extends Fragment {

    private CircleImageView avatar;
    private AlertDialog avatarUrlDialog;
    private AlertDialog.Builder avatarUrlBuilder;
    private Switch darkThemeSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        avatar = view.findViewById(R.id.teacherAvatarImage);
        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        avatarUrlBuilder = new AlertDialog.Builder(getContext())
                .setMessage("Вставьте URL вашего файла.\nВажно: Ссылка должна быть прямой, и вести на картинку, в ином случае картинка не будет загружена.")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String avatarUrl = input.getText().toString();
                        if(!avatarUrl.equals("")) {
                            MainActivity mainActivity = new MainActivity();
                            mainActivity.savePreferences("avatarUrl", avatarUrl);
                            Picasso.with(getContext()).load(avatarUrl).error(R.drawable.ic_warning).into(avatar);
                        } else {
                            Toast.makeText(getContext(), "URL-Адрес не может быть пустым.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        ;
        Button settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        avatarUrlDialog = avatarUrlBuilder.create();
        if(GetUserInfo.avatarUrl.equals("")) {
            Picasso.with(getContext()).load(R.drawable.stem_logo).error(R.drawable.stem_logo).into(avatar);
        } else {
            Picasso.with(getContext()).load(GetUserInfo.avatarUrl).error(R.drawable.stem_logo).into(avatar);
        }
//        avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    avatarUrlDialog.show();
////                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
////                photoPickerIntent.setType("image/*");
////                startActivityForResult(photoPickerIntent, 1);
//
//            }
//        });
        TextView nameTxt = view.findViewById(R.id.nameText);
        nameTxt.setText(GetUserInfo.userSurname+" "+GetUserInfo.userName);
        TextView coinsTxt = view.findViewById(R.id.coinsText);
        coinsTxt.setText("Коины: "+GetUserInfo.userCoins);
        Button exitFromAccount = view.findViewById(R.id.exitButton);
        exitFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFromAccount();

            }
        });
        return view;
    }

    private void exitFromAccount() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("logins", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login",null);
        editor.putString("password",null);
        editor.putBoolean("isChecked",false);
        editor.apply();
        preferences = this.getActivity().getSharedPreferences(SettingsActivity.SETTINGS_PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(SettingsActivity.NOTIFY_PREF, false);
        editor.apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
