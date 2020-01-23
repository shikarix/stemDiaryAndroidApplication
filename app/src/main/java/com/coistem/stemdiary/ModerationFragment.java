package com.coistem.stemdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ModerationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moderation, container, false);

        final TextView hintTxt = view.findViewById(R.id.hintLoginTxt);
        
        final EditText loginField = view.findViewById(R.id.loginText);
        Button moderateBtn = view.findViewById(R.id.moderateProfileBtn);
        final Spinner roleSpinner = view.findViewById(R.id.roleSpinner);
        final EditText nameField = view.findViewById(R.id.moderateNameField);
        final EditText surnameField = view.findViewById(R.id.moderateSurnameField);
        final EditText loginEditField = view.findViewById(R.id.moderateLoginField);
        final EditText passwordField = view.findViewById(R.id.moderatePasswordField);
        final EditText coinsField = view.findViewById(R.id.moderateCoinsField);

        loginEditField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_TAB)){
                    Toast.makeText(getContext(), "ENTER NAJAT", Toast.LENGTH_SHORT).show();
                   return true;
                };
                return false;
            }
        });

        moderateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserInfo getUserInfo = new GetUserInfo();
                getUserInfo.moderateParseJson(loginField.getText().toString());
                String login = loginField.getText().toString();
                loginField.setText("");
                hintTxt.setVisibility(View.INVISIBLE);
                nameField.setVisibility(View.VISIBLE);
                surnameField.setVisibility(View.VISIBLE);
                passwordField.setVisibility(View.VISIBLE);
                coinsField.setVisibility(View.VISIBLE);
                nameField.setText(GetUserInfo.moderationUserName);
                surnameField.setText(GetUserInfo.moderationUserSurname);
                loginEditField.setVisibility(View.VISIBLE);
                loginEditField.setText(login);
                coinsField.setText(GetUserInfo.moderationUserCoins);
                roleSpinner.setVisibility(View.VISIBLE);
            }
        });
        hintTxt.setVisibility(View.VISIBLE);
        nameField.setVisibility(View.INVISIBLE);
        loginEditField.setVisibility(View.INVISIBLE);
        surnameField.setVisibility(View.INVISIBLE);
        passwordField.setVisibility(View.INVISIBLE);
        coinsField.setVisibility(View.INVISIBLE);
        roleSpinner.setVisibility(View.INVISIBLE);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] stringArray = getResources().getStringArray(R.array.courses_names);
                Toast.makeText(getContext(), "Selected: "+stringArray[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

}
