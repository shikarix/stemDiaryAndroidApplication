package com.coistem.stemdiary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends Activity {

    HashMap<String,String> accounts = new HashMap<>();
    private EditText loginText;
    private EditText passwordTxt;
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
    private ImageView showPassIcon;
    private CheckingConnectionTask checkingConnectionTask;
    private boolean isPasswordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginText = findViewById(R.id.loginText);
        passwordTxt = findViewById(R.id.pswTxt);
//        isLocalServerBox = findViewById(R.id.isLocalServerCheck);
        showPassIcon = findViewById(R.id.showPasswordView);
        final Button signInButton = findViewById(R.id.loginInBtn);

        sharedPreferences = getSharedPreferences("logins",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String loginSP = sharedPreferences.getString("login", null);
        String passwordSP = sharedPreferences.getString("password", null);

        boolean isChecked = sharedPreferences.getBoolean("isChecked",false);

        rememberBox = findViewById(R.id.rememberCheck);
        rememberBox.setChecked(false);

        showPassIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordShowing) {
                    showPassIcon.setImageResource(R.drawable.ic_dontshow_password);
                    passwordTxt.setTransformationMethod(null);
                } else {
                    showPassIcon.setImageResource(R.drawable.ic_show_password_icon);
                    passwordTxt.setTransformationMethod(new PasswordTransformationMethod());
                }
                isPasswordShowing = !isPasswordShowing;
            }
        });

        errorConnectionBuilder = new AlertDialog.Builder(this)
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
        loginErrorBuilder = new AlertDialog.Builder(this)
                .setTitle("Ошибка авторизации")
                .setMessage("Неверная комбинация логин/пароль")
                .setPositiveButton("ОK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginErrorDialog.cancel();
                    }
                });
        loginErrorDialog = loginErrorBuilder.create();

        ProgressBar progressBar = new ProgressBar(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        progressBar.setLayoutParams(lp);

        loadingBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(R.layout.pleasewaitdialog)
//                .setView(progressBar)
                .setMessage("Авторизируемся. Пожалуйста, подождите...");
        loadingDialog = loadingBuilder.create();

        if(loginSP!=null && passwordSP!=null) {
            login = loginSP;
            password = passwordSP;
            loginText.setText(loginSP);
            passwordTxt.setText(passwordSP);
            loadingDialog.show();
            connectToServer();
        }
        passwordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordTxt.getText().length() > 0) {
                    setVisibleShowPass();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = loginText.getText().toString();
                password = passwordTxt.getText().toString();
                loadingDialog.show();
                connectToServer();
            }
        });

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
        editor.putBoolean("isChecked",rememberBox.isChecked());
        editor.apply();
        finish();
    }

    public void setVisibleShowPass() {
        showPassIcon.animate()
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        showPassIcon.setVisibility(View.VISIBLE);
                    }
                })
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(100)
                .start();
    }

    private void connectToServer() {
        MainActivity.serverIp = "18.191.156.108";
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
            if (execute.contains("Логин")) {
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
                sanyadebil = (Boolean) checkingConnection.execute(LoginActivity.this, "https://vk.com/").get();
                loadingDialog.cancel();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(sanyadebil) {
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
                    Toast.makeText(LoginActivity.this, "В настоящее время сервера недоступны. Повторите попытку позже.", Toast.LENGTH_SHORT).show();
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
