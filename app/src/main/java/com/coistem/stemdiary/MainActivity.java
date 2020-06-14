package com.coistem.stemdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private InfoFragment infoFragment;
    private NewsFragment newsFragment;
    private ShopFragment shopFragment;
    private ModerationFragment moderationFragment;
    private TimetableFragment timetableFragment;
    private boolean isInfoVisible;
    private boolean isTimeTableVisible;
    private boolean isNewsVisible;
    private boolean isShopVisible;
    private boolean isModerationVisible;
    public static String serverIp = "";
    public static String userLogin = "";
    public static String userPassword = "";
    private int backClicks = 0;
    private static SharedPreferences sp;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    if(isNewsVisible) {

                    } else {
                        //replaced news fragment
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, newsFragment);
                        fragmentTransaction.commit();
                        isTimeTableVisible = false;
                        isModerationVisible = false;
                        isShopVisible = false;
                        isNewsVisible = true;
                        isInfoVisible = false;
                    }
                    return true;
                case R.id.navigation_timetable:
                    if(isTimeTableVisible) {

                    } else {
                        // replaced timetable fragment
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, timetableFragment);
                        fragmentTransaction.commit();
                        isTimeTableVisible = true;
                        isNewsVisible = false;
                        isModerationVisible = false;
                        isShopVisible = false;
                        isInfoVisible = false;
                    }
                    return true;

                case R.id.navigation_shop:
                    if(isShopVisible) {

                    } else {
                        isInfoVisible = false;
                        isShopVisible = true;
                        isNewsVisible = false;
                        isModerationVisible = false;
                        isTimeTableVisible = false;
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, shopFragment);
                        fragmentTransaction.commit();
                    }
                    return true;

                case R.id.navigation_info:
                    if(isInfoVisible) {

                    } else {
                        isInfoVisible = true;
                        isNewsVisible = false;
                        isShopVisible = false;
                        isModerationVisible = false;
                        isTimeTableVisible = false;
                        FragmentManager supportFragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, infoFragment);
                        fragmentTransaction.commit();
                    }
                    return true;


            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view44);
        infoFragment = new InfoFragment();
        newsFragment = new NewsFragment();
        shopFragment = new ShopFragment();
        timetableFragment = new TimetableFragment();
        moderationFragment = new ModerationFragment();

//        sp = getSharedPreferences("logins",MODE_PRIVATE);                   // offnut
//        GetUserInfo.avatarUrl = sp.getString("avatarUrl","null");    //ofnut

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if(fragmentTransaction.isEmpty()) {
            fragmentTransaction.replace(R.id.main_content, newsFragment);
            fragmentTransaction.commit();
            isTimeTableVisible = false;
            isShopVisible = false;
            isNewsVisible = true;
            isModerationVisible = false;
            isInfoVisible = false;
        }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //        if(resultCode== Activity.RESULT_OK){
        System.out.println("requestCode = " + requestCode + ", resultCode = " + resultCode);
        Toast.makeText(MainActivity.this, "Выбранная дата: "+data.getStringExtra("date"), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "Ничего не выбрано.", Toast.LENGTH_SHORT).show();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        backClicks++;
        if(backClicks==1) {
            Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT).show();
        }
        if(backClicks>=2){
            finishAffinity();
        }
    }
}
