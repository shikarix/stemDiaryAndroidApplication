package com.coistem.stemdiary;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetUserInfo {

    public static String userName;
    public static String userSurname;
    public static String userCoins;
    public static int userCounterCoins;
    public static String avatarUrl;
    public static String userAccessType;
    public static String userToken;

    public static String moderationUserName;
    public static String moderationUserSurname;
    public static String moderationUserThirdName;
    public static String moderationUserCoins;
    public static int moderationUserCounterCoins;
    public static String moderationUserAccessType;

    private String jsonFile;

    public void parseJson(String userLogin) {
        try {
            JSONObject jsonObject = new JSONObject(jsonFile);
            JSONArray jsonArray = jsonObject.getJSONArray(userLogin);
            JSONObject userInfo = jsonArray.getJSONObject(0);
            userName = userInfo.getString("name");
            userCoins = userInfo.getString("coins");
            userSurname = userInfo.getString("surname");
            userAccessType = userInfo.getString("accessType");
//            avatarUrl = userInfo.getString("avatarUrl");
            System.out.println(userAccessType);
            userCounterCoins = Integer.parseInt(userCoins);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonFile);
    }

    public void moderateParseJson(String login) {
        try {
            JSONObject jsonObject = new JSONObject(jsonFile);
            JSONArray jsonArray = jsonObject.getJSONArray(login);
            JSONObject userInfo = jsonArray.getJSONObject(0);
            moderationUserName = userInfo.getString("name");
            moderationUserCoins = userInfo.getString("coins");
            moderationUserSurname = userInfo.getString("surname");
            JSONObject jo = new JSONObject();
            moderationUserCounterCoins = Integer.parseInt(userCoins);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonFile);
    }

    public void prepareJsonFile(Context context, String login) {
        JSONFileEditing jsonFileEditing = new JSONFileEditing();
        jsonFile = jsonFileEditing.getJsonData(context);
        System.out.println("JSOOOOOOOOOOOOOOOOOON"+jsonFile);
        parseJson(login);
    }

    public void parseJSONFromServer(String jsonInput) {
        try {
            String[] databases = jsonInput.split("Database");
            jsonInput = databases[1];
            char[] chars = jsonInput.toCharArray();
            String str = "";
            for (int i = 2; i < chars.length-1; i++) {
                str+= chars[i];
            }
            jsonInput = str;
            Log.d("Server JSON:",jsonInput);
            JSONObject userInfo = new JSONObject(jsonInput);
            avatarUrl = userInfo.getString("avatarUrl");
            userName = userInfo.getString("name");
            userCoins = userInfo.getString("stemCoins");
            userSurname = userInfo.getString("surname");
            userAccessType = userInfo.getString("accessType");
//            userToken = userInfo.getString("token");
            System.out.println(userAccessType);
            userCounterCoins = Integer.parseInt(userCoins);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonInput);
    }





}
