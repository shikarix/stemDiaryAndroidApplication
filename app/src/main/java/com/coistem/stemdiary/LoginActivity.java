package com.coistem.stemdiary;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity{

    HashMap<String,String> accounts = new HashMap<>();
    private EditText loginText;
    private EditText passwordTxt;
    private CheckBox isLocalServerBox;
    private AlertDialog.Builder loadingBuilder;
    public static AlertDialog loadingDialog;
    private AlertDialog loginErrorDialog;
    private AlertDialog.Builder loginErrorBuilder;
    private AlertDialog errorConnectionDialog;
    private AlertDialog.Builder errorConnectionBuilder;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox rememberBox;
    private String isSuccesfulLogin;
    private String login;
    private String password;
    private CheckingConnectionTask checkingConnectionTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginText = findViewById(R.id.loginText);
        passwordTxt = findViewById(R.id.pswTxt);
        isLocalServerBox = findViewById(R.id.isLocalServerCheck);
        final Button signInButton = findViewById(R.id.loginInBtn);
        addAccounts(); // вносим аккаунты в бд
        sharedPreferences = getSharedPreferences("logins",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String loginSP = sharedPreferences.getString("login", null);
        String passwordSP = sharedPreferences.getString("password", null);
        boolean isChecked = sharedPreferences.getBoolean("isChecked",false);
        rememberBox = findViewById(R.id.rememberCheck);
        rememberBox.setChecked(false);

        errorConnectionBuilder = new AlertDialog.Builder(LoginActivity.this)
                .setMessage("Ошибка подключения к интернету.\nПроверьте подключение к интернету или повторите попытку позже.")
                .setPositiveButton("обновить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorConnectionDialog.cancel();
                        signInButton.performClick();
                    }
                })
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
        errorConnectionDialog = errorConnectionBuilder.create();
        loginErrorBuilder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Ошибка авторизации")
                .setMessage("Неверная комбинация логин/пароль")
                .setPositiveButton("ОK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginErrorDialog.cancel();
                    }
                });
        loginErrorDialog = loginErrorBuilder.create();

        ProgressBar progressBar = new ProgressBar(LoginActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        progressBar.setLayoutParams(lp);

        loadingBuilder = new AlertDialog.Builder(LoginActivity.this)
                .setCancelable(false)
                .setView(R.layout.pleasewaitdialog)
//                .setView(progressBar)
                .setMessage("Авторизируемся. Пожалуйста, подождите...");
        loadingDialog = loadingBuilder.create();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                connectToServer();
            }
        });

        if(loginSP!=null && passwordSP!=null) {
            loginText.setText(loginSP);
            passwordTxt.setText(passwordSP);
            signInButton.performClick();
        }

    }

    public void logIn() {
//        Toast.makeText(LoginActivity.this, "запускаю активность...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        if (rememberBox.isChecked()){
            editor.putString("login",login);
            editor.putString("password",password);
            editor.putBoolean("isChecked",rememberBox.isChecked());
            editor.apply();
        }
        finish();
    }

    private void connectToServer() {
        if(isLocalServerBox.isChecked()) {
            MainActivity.serverIp = "192.168.1.106:8080";
        } else {
            MainActivity.serverIp = "18.191.156.108";
        }
//        CheckingConnection checkingConnection = new CheckingConnection();
        checkingConnectionTask = new CheckingConnectionTask();
        checkingConnectionTask.execute();
    }

    private String signIn(String login, String password) {
        if(login==null || login.equals("")) {
            Toast.makeText(this, "Поле логина не может быть пустым.", Toast.LENGTH_SHORT).show();
            return "Go daleko!";
        } else if(password == null || password.equals("")) {
            Toast.makeText(this, "Поле пароля не может быть пустым.", Toast.LENGTH_SHORT).show();
            return "Go daleko!";
        } else {
            SocketConnect socketConnect = new SocketConnect();
            String execute = null;
            socketConnect.execute("auth", login, password);
            try {
                execute = (String) socketConnect.get(2, TimeUnit.SECONDS);
                System.out.println(execute);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                execute = "Connection error";
                e.printStackTrace();
            }
            if (execute.equalsIgnoreCase("Database \"Go daleko!\"")) {
                return "Go daleko!";
            } else if (execute.equals("Connection error")) {
                return "Server Connection error";
            } else {
                GetUserInfo getUserInfo = new GetUserInfo();
                getUserInfo.parseJSONFromServer(execute);
                return "Successful";
            }
            //--------server connection---------
//        String pass = accounts.get(login);
//        System.out.println(pass);
//        if(password.equals(pass)){
//            MainActivity.userLogin = login;
//            return true;
//        } else if(pass!=null&&!password.equals(pass)) {
//            Toast.makeText(LoginActivity.this, "Неверный пароль.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            Toast.makeText(LoginActivity.this, "Такого пользователя не существует.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        }
    }

    private void addAccounts() {
        accounts.put("user","12345");
        accounts.put("eremin15","1337228");
        accounts.put("yeliseyenko23","56789");
        accounts.put("vasilev75","54321");
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginText.setText("");
        passwordTxt.setText("");
        rememberBox.setChecked(false);
    }

    private class CheckingConnectionTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            boolean sanyadebil = false;

            try {
                CheckingConnection checkingConnection = new CheckingConnection();
                sanyadebil = (Boolean) checkingConnection.execute(getBaseContext(), "https://vk.com/").get();
                loadingDialog.cancel();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String s = (sanyadebil ? "Online" : "Offline");
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            if(sanyadebil) {
                login = loginText.getText().toString();
                password = passwordTxt.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isSuccesfulLogin = signIn(login, password);
//                        isSuccesfulLogin = "Successful";
                        System.out.println(isSuccesfulLogin);
                    }
                }).run();
                if(isSuccesfulLogin.equals("Successful")) {
                    if(!VKSdk.isLoggedIn()) {
                        Toast.makeText(LoginActivity.this, "Пожалуйста, авторизуйтесь.", Toast.LENGTH_SHORT).show();
                        String[] scope = {VKScope.FRIENDS};
                        VKSdk.login(LoginActivity.this,scope);
                        MainActivity.userLogin = login;
                        MainActivity.userPassword = password;
                    } else {
                        MainActivity.userLogin = login;
                        MainActivity.userPassword = password;
                        logIn();

                    }
                } else if(isSuccesfulLogin.equals("Go daleko!")) {
                    loginErrorDialog.show();
                } else if(isSuccesfulLogin.equals("Server Connection error")) {
                    Toast.makeText(LoginActivity.this, "Server connection error", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(LoginActivity.this, "НЕТ ИНТЕРНЕТА КАШМАР КАКОЙТА", Toast.LENGTH_SHORT).show();
                errorConnectionDialog.show();
            }
            super.onPostExecute(o);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {
                // Пользователь успешно авторизовался
                logIn();
            }

            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
