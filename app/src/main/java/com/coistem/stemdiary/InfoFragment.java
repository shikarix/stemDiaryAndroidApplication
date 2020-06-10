package com.coistem.stemdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class InfoFragment extends Fragment {

    private CircleImageView avatar;
    private AlertDialog avatarUrlDialog;
    private AlertDialog.Builder avatarUrlBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        avatar = view.findViewById(R.id.avatarImageView);
        final EditText input = new EditText(getContext());
        Toast.makeText(getContext(), GetUserInfo.userToken, Toast.LENGTH_SHORT).show();
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
        avatarUrlDialog = avatarUrlBuilder.create();
        Log.d("Server avatar url",GetUserInfo.avatarUrl);
//        avatar.setBorderWidth(20);
//        avatar.setBorderColor(Color.YELLOW);
        if(GetUserInfo.avatarUrl.equals("")) {
            Picasso.with(getContext()).load(R.drawable.stem_logo).error(R.drawable.stem_logo).into(avatar);
        } else {
            Picasso.with(getContext()).load(GetUserInfo.avatarUrl).error(R.drawable.stem_logo).into(avatar);
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    avatarUrlDialog.show();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);

            }
        });
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
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    Bitmap bitmap = null;
                    Uri selectedImage = data.getData();
                    String encodedPath = selectedImage.getEncodedPath();
//                    System.out.println(encodedPath);
//                    try {
//                        Picasso.
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
////                    avatar.setImageBitmap(bitmap);
                }
                break;
            }
        }
    }
}
