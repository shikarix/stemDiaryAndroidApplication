package com.coistem.stemdiary;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OurData {

    public static  String[] title;
    public static String[] imgUrls;
    public static String[] dates;
    public static String[] urlsForPost;

    public static String[] itemNames;
    public static String[] itemImageUrls;
    public static String[] itemCosts;
    public static Integer[] itemIds;
    public static Integer[] itemCounts;

    public static String[] cartItemNames;
    public static String[] cartItemImageUrls;
    public static String[] cartItemCosts;
    public static Integer[] cartItemIds;

    public static String[] inWorkItemStatuses;
    public static String[] inWorkItemNames;

    public static String[] courseNames;
    public static String[] courseImageUrls;
    public static String[] courseDates;
    public static String[] courseTeachers;
    public static String[] courseTeachersAvatarUrls;

    public static Object[] homeworks;
    public static String[] currentHomeworks;
    public static Object[] lessonsDates;
    public static String[] currentLessonsDates;

    public static String[] coursePupilPagesNames;
    public static String[] coursePupilPagesAvatarUrls;
    public static String[] coursePupilPagesUrls;

    public static String[] teacherNames;
    public static String[] teacherLogins;
    public static String[] pupilsNames;
    public static String[] pupilsLogins;
    public static boolean[] pupilsIsSelected;
}
