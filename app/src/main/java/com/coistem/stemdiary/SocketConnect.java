package com.coistem.stemdiary;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketConnect extends AsyncTask {
    private OnTaskComplete listener;

    public interface OnTaskComplete {
        void onTaskComplete();
    }
    public static String result;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dos;
    Socket socket;

    public String authorizate(String login, String password){
//        try {
//            try {
//                socket = new Socket("192.168.1.100", 45654);
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//                dataInputStream = new DataInputStream(socket.getInputStream());
//                dos = new DataOutputStream(socket.getOutputStream());
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("login",login);
//                jsonObject.put("password",password);
//                String s1 = jsonObject.toString();
//                dos.writeUTF(s1);
//                dos.flush();
//                String s2 = dataInputStream.readUTF();
//                Log.d("Server answer",s2);
//                Log.d("Server input", s1);
//                return s2;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                dataInputStream.close();
//                dos.close();
//                socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            return "Connection error";
//        }
//        return "error";
        try {
            char[] log = login.toCharArray();
            String lg = "";
            String ps = "";
            for (int i = 0; i < log.length; i++) {
                log[i]+=50;
                lg += log[i];
            }
            char[] pass = password.toCharArray();
            for (int i = 0; i < pass.length; i++) {
                pass[i] += 50;
                ps += pass[i];
            }
            System.out.println("LOGIN: "+lg+"PASSWORDDD: "+ps);
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/database/"+lg+"/"+ps).get();
            String text = document.text();
            String html = document.outerHtml();
            System.out.println(html);
            if(text.equals("Что-то пошло не так...")) {
                return "Connection error";
            } else {
                return text;
            }
        } catch (IOException e) {
            return "Connection error";
        }
    }
    public String getStudentInfo(String login) {
        try {
           Document document = Jsoup.connect("http://" + MainActivity.serverIp + "/getStemCoins/" + MainActivity.userLogin + "/" + MainActivity.userPassword + "/" + login).get();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Что-то пошло не так...")) {
                return "Connection error";
            } else if(text.equals("Go daleko!")) {
                return "User not found";
            }  else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Connection error";
        }
    }

    public String getTeacherCourses() {
        try {
            Document document = Jsoup.connect("http://" + MainActivity.serverIp + "/courseGet/" + MainActivity.userLogin + "/" + MainActivity.userPassword).get();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Что-то пошло не так...")) {
                return "Connection error";
            } else if(text.equals("Go daleko!")) {
                return "Access error";
            }  else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Connection error";
        }
    }

    public String purchaseSomething(int id) {
        try {
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/buy/"+id+"/"+MainActivity.userLogin+"/"+MainActivity.userPassword).get();
            String text = document.text();
            String html = document.outerHtml();
            System.out.println(text);
            if(text.equals("Что-то пошло не так...")) {
                return "Connection error";
            } else {
                return text;
            }
        } catch (IOException e) {
            return "Connection error";
        }
    }

    public String takeShopItems(String token) {
        try {
            Document document = Jsoup.connect("http://"+ MainActivity.serverIp+"/androidShop/"+MainActivity.userLogin+"/"+MainActivity.userPassword).get();
            String text = document.text();
            String html = document.outerHtml();
            System.out.println("http://"+ MainActivity.serverIp+"/androidShop/"+MainActivity.userLogin+"/"+MainActivity.userPassword);
            System.out.println(text);
            if(text.equals("Go daleko!")) {
                return "Go daleko!";
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            try {
//                socket = new Socket("192.168.1.100", 45654);
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//                dataInputStream = new DataInputStream(socket.getInputStream());
//                dos = new DataOutputStream(socket.getOutputStream());
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("token", token);
//                jsonObject.put("command","getShop");
//                String s1 = jsonObject.toString();
//                dos.writeUTF(s1);
//                dos.flush();
//                String s2 = dataInputStream.readUTF();
//                Log.d("Server answer",s2);
//                Log.d("Server input", s1);
//                return s2;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                dataInputStream.close();
//                dos.close();
//                socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            return "Connection error";
//        }

        return "error";
    }

    @Override
    protected void onPreExecute() {
//        LoginActivity.loadingDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        switch ((String) objects[0]) {
            case "auth" : {
                String authorizate = authorizate((String) objects[1], (String) objects[2]);
                result = authorizate;
                return authorizate;
            }
            case "shop" : {
                 String getShopItems = takeShopItems((String) objects[1]);
                 return getShopItems;
            }
            case "purchase": {
                String purchase = purchaseSomething((Integer) objects[1]);
                return purchase;
            }
            case "getStudentInfo" : {
                String studentInfo = getStudentInfo((String) objects[1]);
                return studentInfo;
            }
            case "getTeacherCourses": {
                String teacherCourses = getTeacherCourses();
                return teacherCourses;
            }
        }
        return "unknown command";
    }

    @Override
    protected void onPostExecute(Object o) {
//        LoginActivity.loadingDialog.cancel();
//        listener.onTaskComplete();
    }
}
