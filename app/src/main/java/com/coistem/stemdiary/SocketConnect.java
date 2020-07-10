package com.coistem.stemdiary;

import android.os.AsyncTask;

import com.coistem.stemdiary.activities.MainActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

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
    public static final String GET_CONFIRMED_BASKET = "getConfirmedBasket";
    public static final String GET_ADMIN_CONFIRMED_BASKET = "getAdminBaskets";
    public static final String ADD_COURSE = "addCourse";
    public static final String ACCEPT_PURCHASE = "acceptPurchase";
    public static final String CANCEL_PURCHASE = "cancelPurchase";
    public static final String SEND_STATUS = "setStatus";
    public static final String SEND_HOMEWORK = "sendHomework";

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
            Document document = Jsoup.connect("http://"+ MainActivity.serverIp+"/androidLogin/")
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

    private String acceptPurchase(String basketId) {
        try {
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/confirm/")
                    .data("login", MainActivity.userLogin,
                            "password",MainActivity.userPassword,
                            "basketId", basketId).post();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Я хз!")) {
                return "hz";
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

    private String setStatus(String status, Integer id) {
        try {
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/setStatus/")
                    .data("login", MainActivity.userLogin,
                            "password",MainActivity.userPassword,
                            "basketId", id.toString(),
                            "status", status).post();
            String text = document.text();
            System.out.println(text);
            if (text.contains("Логин")) {
                return GO_DALEKO;
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CONNECTION_ERROR;
    }

    private String cancelPurchase(String basketId) {
        try {
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/decline/")
                    .data("login", MainActivity.userLogin,
                            "password",MainActivity.userPassword,
                            "basketId", basketId).post();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Я хз!")) {
                return "hz";
            } else if (text.contains("Логин")) {
                return CONNECTION_ERROR;
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

    private String getStudentCourses(String login, String password, Boolean isTeacher) {
        try {
            Document document = null;
            if(!isTeacher) {
                document = Jsoup.connect("http://" + MainActivity.serverIp + "/getPupilCourses")
                        .data("login", login)
                        .data("password", password).post();
            } else {
                document = Jsoup.connect("http://" + MainActivity.serverIp + "/getTeacherCourses")
                        .data("login", login)
                        .data("password", password).post();
            }
            String text = document.text();
            System.out.println(text);
            if (text.contains("Логин")) {
                return GO_DALEKO;
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

    private String sendStudentRate(String courseName, String date, String student, Integer discRate, Integer progressRate, Integer addRate) {
        try {
            String url = "http://"+MainActivity.serverIp+"/setStudentRate/";
            Document document = Jsoup.connect(url)
                    .data("login", MainActivity.userLogin,
                            "password", MainActivity.userPassword,
                            "courseName", courseName,
                            "date", date,
                            "a", discRate.toString(),
                            "b", progressRate.toString(),
                            "c",addRate.toString(),
                            "pupilLogin", student
                    ).post();
            System.out.println(url);
            String text = document.text();
            System.out.println(text);
            if(text.contains("Логин")) {
                return CONNECTION_ERROR;
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CONNECTION_ERROR;
    }

    private String getUnconfirmedBasket() {
        try {
            Document basket = Jsoup.connect("http://" + MainActivity.serverIp + "/getAllUnconfirmedBaskets").data("login", MainActivity.userLogin, "password", MainActivity.userPassword).post();
            String text = basket.text();
            System.out.println(text);
            if(text.contains("Логин")) {
                return "Connection error";
            } else if(text.contains("Nothing!")) {
                return GO_DALEKO;
            } else{
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CONNECTION_ERROR;
    }

    private String getConfirmedBasket() {
        try {
            Document basket = Jsoup.connect("http://" + MainActivity.serverIp + "/getAllConfirmedBaskets").data("login", MainActivity.userLogin, "password", MainActivity.userPassword).post();
            String text = basket.text();
            System.out.println(text);
            if(text.contains("Логин")) {
                return "Connection error";
            } else if(text.contains("[]")) {
                return GO_DALEKO;
            } else{
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CONNECTION_ERROR;
    }

    private String getAdminConfirmedBasket() {
        try {
            Document document = Jsoup.connect("http://"+MainActivity.serverIp+"/getAllAdminConfirmedBaskets")
                    .data("login", MainActivity.userLogin,
                            "password",MainActivity.userPassword
                    ).post();
            String text = document.text();
            System.out.println(text);
            if(text.contains("Логин")) {
                return "Connection error";
            } else if(text.contains("Nothing!")) {
                return GO_DALEKO;
            } else{
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CONNECTION_ERROR;
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
        return CONNECTION_ERROR;
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
    private String addCourse(String courseName, String date, String[] pupils, String teacherLogin, String imageUrl) {
        Connection data = Jsoup.connect("http://" + MainActivity.serverIp + "/addCourse").data("login", MainActivity.userLogin, "password", MainActivity.userPassword, "name", courseName, "date", date, "teacher", teacherLogin, "imgSrc", imageUrl);
        for (int i = 0; i < pupils.length; i++) {
            data.data("pupils", pupils[i]);
        }
        try {
            Document document = data.post();
            String text = document.text();
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

    private String setHomework(String courseName, String date, String homework) {
        try {
            Document document = Jsoup.connect("http://"+ MainActivity.serverIp+"/setHomework/")
                    .data(
                            "login",MainActivity.userLogin,
                            "password", MainActivity.userPassword,
                            "courseName",courseName,
                            "date", date,
                            "homework", homework
                    )
                    .post();
            String text = document.text();
            System.out.println(text);
            if(text.equals("Логин")) {
                return GO_DALEKO;
            } else {
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CONNECTION_ERROR;
    }

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
                return getStudentCourses((String) objects[1], (String) objects[2], (Boolean) objects[3]);
            }
            case GET_ALL_TEACHERS: {
                return takeAllTeachers();
            }
            case GET_ALL_PUPILS: {
                return takeAllPupils();
            }
            case ADD_COURSE: {
                return addCourse((String) objects[1], (String) objects[2], (String[]) objects[3], (String) objects[4], (String) objects[5]);
            }
            case ACCEPT_PURCHASE: {
                return acceptPurchase((String) objects[1]);
            }
            case CANCEL_PURCHASE: {
                return cancelPurchase((String) objects[1]);
            }
            case GET_CONFIRMED_BASKET: {
                return getConfirmedBasket();
            }
            case GET_ADMIN_CONFIRMED_BASKET: {
                return getAdminConfirmedBasket();
            }
            case SEND_STATUS: {
                return setStatus((String) objects[1], (Integer) objects[2]);
            }
            case SEND_HOMEWORK: {
                return setHomework((String) objects[1], (String) objects[2], (String) objects[3]);
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
