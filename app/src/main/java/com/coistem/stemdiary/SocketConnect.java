package com.coistem.stemdiary;

import android.app.AlertDialog;
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

    public static final String SHOP_ITEMS = "shop";
    public static final String MAKE_PURCHASE = "purchase";
    public static final String STUDENT_INFO = "getStudentInfo";
    public static final String TEACHER_COURSES = "getTeacherCourses";
    public static final String COURSE_LESSONS = "getCourseLessons";
    public static final String LESSON_STUDENTS = "getLessonStudents";
    public static final String SEND_RATE = "setStudentRate";
    public static final String GET_COURSES = "getCourses";
    public static final String GET_UNCONFIRMED_BASKET = "getUnconfBasket";
    public static final String GET_ALL_TEACHERS = "getAllTeachers";
    public static final String GET_ALL_PUPILS = "getAllPupils";

    public static final String GO_DALEKO = "Go daleko!";
    public static final String CONNECTION_ERROR = "Connection error";

    private OnTaskComplete listener;

    public interface OnTaskComplete {
        void onTaskComplete();
    }
    public static String result;


    private String coursesNames = "{\n" +
            "  \"courses\":[\n" +
            "  {\"courseName\":\"Course1\"},\n" +
            "  {\"courseName\":\"Course2\"},\n" +
            "  {\"courseName\":\"Course3\"},\n" +
            "  {\"courseName\":\"Course4\"},\n" +
            "  {\"courseName\":\"Course5\"},\n" +
            "  {\"courseName\":\"noobourse\"}\n" +
            "  ]\n" +
            "}";

    private String courseLessons1 = "{\n" +
            "\t\"lessons\":[\n" +
            "\t\t{\"lessonDate\":\"4.04.20\"},\n" +
            "\t\t{\"lessonDate\":\"10.04.20\"},\n" +
            "\t\t{\"lessonDate\":\"27.04.20\"}\n" +
            "\t]\n" +
            "}";
    private String courseLessons2 = "{\n" +
            "\t\"lessons\":[\n" +
            "\t\t{\"lessonDate\":\"5.04.20\"},\n" +
            "\t\t{\"lessonDate\":\"11.04.20\"},\n" +
            "\t\t{\"lessonDate\":\"28.04.20\"}\n" +
            "\t]\n" +
            "}";

    private String fourthAprilStudents = "{\n" +
            "\t\"students\":[\n" +
            "\t\t{\"studentName\":\"Alexey Vasiliev\"},\n" +
            "\t\t{\"studentName\":\"Eryomin Vadim\"},\n" +
            "\t\t{\"studentName\":\"Sannikov Andrey\"}\n" +
            "\t]\n" +
            "}";

    private String fifthAprilStudents = "{\n" +
            "\t\"students\":[\n" +
            "\t\t{\"studentName\":\"Yura Yeliseenko\"},\n" +
            "\t\t{\"studentName\":\"Elena Yeliseenko\"},\n" +
            "\t\t{\"studentName\":\"Timur Romanov\"}\n" +
            "\t]\n" +
            "}";


    private String authorizate(String login, String password){
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
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/androidLogin/")
                    .data("login", login)
                    .data("password", password)
                    .post();
            String text = document.text();
            String html = document.outerHtml();
            System.out.println(html);
            String[] words = text.split("Андроид ");
            text = words[1];
            if(text.equals(SocketConnect.GO_DALEKO)) {
                return GO_DALEKO;
            } else {
                return text;
            }
        } catch (IOException e) {
            return "Connection error";
        }
    }

    private String getStudentInfo(String login) {
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

    private String getStudentCourses(String login, String password) {
        try {
            Document document = Jsoup.connect("http://" + MainActivity.serverIp + "/getPupilCourses")
                    .data("login", login)
                    .data("password", password).post();
            String text = document.text();
            if (text.equals("Логин")) {
                return "Access error";
            } else if(text.equals("[]")) {
                return "no courses";
            } else {
                return text;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Connection error";
        }
    }

    private String getLessonStudents(String courseName) {
        try {
            Document document = Jsoup.connect("http://" + MainActivity.serverIp + "/getLessonStudents/" + MainActivity.userLogin + "/" + MainActivity.userPassword + "/" + courseName).get();
            String text = document.text();
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

    private String getCourseLessons(String courseName) {
        try {
            Document document = Jsoup.connect("http://" + MainActivity.serverIp + "/getCourseLessons/" + MainActivity.userLogin + "/" + MainActivity.userPassword + "/" + courseName).get();
            String text = document.text();
            System.out.println(text);
            if (text.equals("Что-то пошло не так...")) {
                return "Connection error";
            } else if (text.equals("Go daleko!")) {
                return "User not found";
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Connection error";
        }
    }

    private String getTeacherCourses() {
        try {
            Document document = Jsoup.connect("http://" + MainActivity.serverIp + "/getTeacherCourses/" + MainActivity.userLogin + "/" + MainActivity.userPassword).get();
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

    private String sendStudentRate(String courseName, String date, String student, int discRate, int progressRate, int addRate) {
        try {
            String[] split = student.split(" ");
            String studentName = split[0];
            String studentSurname = split[1];
            String url = "http://"+MainActivity.serverIp+"/setStudentRate/"+MainActivity.userLogin+"/"+MainActivity.userPassword+"/"+courseName+"/"+date+"/"+studentName+"/"+studentSurname+"/"+discRate+"/"+progressRate+"/"+addRate;
            Document document = Jsoup.connect(url).get();
            System.out.println(url);
            String text = document.text();
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

    private String getUnconfirmedBasket() {
        try {
            Document basket = Jsoup.connect("http://" + MainActivity.serverIp + "/getAllUnconfirmedBaskets").data("login", MainActivity.userLogin, "password", MainActivity.userPassword).post();
            String text = basket.text();
            System.out.println(text);
            if(text.contains("Логин")) {
                return "Connection error";
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Connection error";
        }
    }

    private String purchaseSomething(Integer id) {
        try {
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/buy/")
                    .data("login", MainActivity.userLogin,
                            "password",MainActivity.userPassword,
                            "productId", id.toString()).post();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Not enough product!")) {
                return "Not enough product!";
            } else if(text.equals("Not enough money!")) {
                return "Not enough money!";
            } else if(text.equals("Good")) {
                return "Good";
            } else if(text.equals("Go daleko!")) {
                return "Connection error";
            } else {
                return text;
            }
        } catch (IOException e) {
            return "Connection error";
        }
    }

    private String takeShopItems(String token) {
        try {
            Document document = Jsoup.connect("http://"+ MainActivity.serverIp+"/getAllShop/").data("login",MainActivity.userLogin,"password", MainActivity.userPassword).post();
            String text = document.text();
//            System.out.println("http://"+ MainActivity.serverIp+"/androidShop/"+MainActivity.userLogin+"/"+MainActivity.userPassword);
            System.out.println(text);
            if(text.equals("Логин")) {
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

    private String takeAllTeachers() {
        try {
            Document allTeachers = Jsoup.connect("http://" + MainActivity.serverIp + "/getAllTeachers")
                    .data("login", MainActivity.userLogin, "password", MainActivity.userPassword).get();
            String text = allTeachers.text();
            System.out.println(text);
            if (text.contains("Логин ")) {
                return GO_DALEKO;
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CONNECTION_ERROR;
        }
    }
//
//    private String addCourse(String courseName, long date, String[] pupils, String teacherLogin, String imageUrl) {
////        Jsoup.connect("http://" + MainActivity.serverIp + "/addCourse")
////                .data("login", MainActivity.userLogin, "password", MainActivity.userPassword,"name", courseName, "date",date,)
//    }

    private String takeAllPupils() {
        try {
            Document allTeachers = Jsoup.connect("http://" + MainActivity.serverIp + "/getAllPupils")
                    .data("login", MainActivity.userLogin, "password", MainActivity.userPassword).post();
            String text = allTeachers.text();
            System.out.println(text);
            if (text.contains("Логин ")) {
                return GO_DALEKO;
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CONNECTION_ERROR;
        }
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
            case SHOP_ITEMS : {
                return takeShopItems((String) objects[1]);
            }
            case MAKE_PURCHASE: {
                return purchaseSomething((Integer) objects[1]);
            }
            case STUDENT_INFO : {
                return getStudentInfo((String) objects[1]);
            }
            case GET_UNCONFIRMED_BASKET: {
                return getUnconfirmedBasket();
            }
            case TEACHER_COURSES: {
                return getTeacherCourses();
            }
            case COURSE_LESSONS: {
                return getCourseLessons((String) objects[1]);
            }
            case LESSON_STUDENTS: {
                return getLessonStudents((String) objects[1]);
            }
            case SEND_RATE: {
                return sendStudentRate((String) objects[1],(String) objects[2],(String) objects[3],(int) objects[4],(int) objects[5],(int) objects[6]);
            }
            case GET_COURSES: {
                return getStudentCourses((String) objects[1], (String) objects[2]);
            }
            case GET_ALL_TEACHERS: {
                return takeAllTeachers();
            }
            case GET_ALL_PUPILS: {
                return takeAllPupils();
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
