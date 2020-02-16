package com.coistem.stemdiary;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ShopFragment extends Fragment {
    private RecyclerView shopList;
    private View view;
    public ImageView backgroundImg;
    private ImageView backgroundMaskImage;
    public static TextView balanceTxt;
    float[] hsv;
    int runColor;

   /* private String jsonList = "{\t\"items\":[{\n" +
            "  \"name\":\"Блокнот\",\n" +
            "  \"img\":\"https://c7.hotpng.com/preview/90/753/217/5bb8ead5f0d90.jpg\"\n" +
            "},{\n" +
            "  \"name\":\"Футболка\",\n" +
            "  \"img\":\"https://s1.iconbird.com/ico/2014/1/619/w512h5121390853824tshirt512.png\"\n" +
            "},{\n" +
            "  \"name\":\"Стикер\",\n" +
            "  \"img\":\"https://s1.iconbird.com/ico/1012/SimplifiedApp/w513h5121350915237appicnsStickies.png\"\n" +
            "},{\n" +
            "  \"name\":\"Флешка \",\n" +
            "  \"img\":\"https://s1.iconbird.com/ico/2013/12/517/w512h5121386955459usb.png\"\n" +
            "},{\n" +
            "  \"name\":\"Сумка\",\n" +
            "  \"img\":\"https://s1.iconbird.com/ico/2013/8/429/w256h2561377940402185103bagshoppingstreamline2.png\"\n" +
            "},{\n" +
            "  \"name\":\"Бесплатное занятие\",\n" +
            "  \"img\":\"https://c7.hotpng.com/preview/151/949/273/square-academic-cap-ico-graduation-ceremony-icon-mortarboard-cliparts.jpg\"\n" +
            "}]\n" +
            "  \n" +
            "}";*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        shopList = view.findViewById(R.id.shopList);
        backgroundImg = view.findViewById(R.id.backgroundShopImage);
        backgroundMaskImage = view.findViewById(R.id.backgroundMaskShopImage);
        balanceTxt = view.findViewById(R.id.balanceText);
        balanceTxt.setText("Ваш баланс: "+GetUserInfo.userCounterCoins+" коинов");
        takeItems("fsddsfkdsf");
//        Toast.makeText(getContext(), takeItems(GetUserInfo.userToken), Toast.LENGTH_SHORT).show();
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
//        valueAnimator.setDuration(10);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                System.out.println(animation.getAnimatedValue());
//                backgroundImg.setAlpha((Float)animation.getAnimatedValue());
//            }
//        });
//        changeColors();
//        ImageView mask = view.findViewById(R.id.backgroundMaskShopImage);
//        int rgb = Color.rgb(151, 101, 165);
//        mask.setBackgroundColor(Color.rgb(101, 51,115));
//        mask.setAlpha(0.3f);
//        backgroundImg.setBackgroundColor(rgb);
        return view;
    }

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageURLs = new ArrayList<>();
    private ArrayList<String> costs = new ArrayList<>();

    public void randomChangeColors() {
        int[][] colors = new int[5][3];
        colors[0][0]=151;
        colors[0][1]=101;
        colors[0][2]=165;
        //-----
        colors[1][0]= 67;
        colors[1][1]=184;
        colors[1][2]=98;
        //-----
        colors[2][0]=245;
        colors[2][1]=88 ;
        colors[2][2]=125;
        //-----
        colors[3][0]= 63;
        colors[3][1]=81;
        colors[3][2]=181;
        //-----
        colors[4][0]=249;
        colors[4][1]=142;
        colors[4][2]=61;
        //-----
        Random rnd = new Random();
        int i = rnd.nextInt(colors.length);
        System.out.println("COUNT: "+i);
        int[] color = colors[i];
        int rgb = Color.rgb(color[0], color[1], color[2]);
        int maskRgb = Color.rgb(color[0]-50, color[1]-50, color[2]-50);
        backgroundMaskImage.setBackgroundColor(maskRgb);
        backgroundMaskImage.setAlpha(0.3f);
        backgroundImg.setBackgroundColor(rgb);
    }

    public void changeColors() {
        ValueAnimator anim = ValueAnimator.ofInt(0,360);
        anim.setDuration(2000);
        int hue = 0;
        hsv = new float[3]; // Transition color
        hsv[1] = 0.4f;
        hsv[2] = 1;
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                hsv[0] = 360 * animation.getAnimatedFraction();
                int i = (Integer) animation.getAnimatedValue();
                System.out.println(i);
                hsv[0] = (float) i;
                runColor = Color.HSVToColor(hsv);
                backgroundImg.setBackgroundColor(runColor);
            }
        });
        anim.setRepeatCount(Animation.INFINITE);

        anim.start();
    }

    @Override
    public void onResume() {
        ShopItemsListAdapter shopItemsListAdapter = new ShopItemsListAdapter();
        shopList.setAdapter(shopItemsListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        shopList.setLayoutManager(layoutManager);
        randomChangeColors();
        super.onResume();
    }

    public String takeItems(String token) {
        SocketConnect socketConnect = new SocketConnect();
        String shop = "";
        try {
            shop = (String)socketConnect.execute("shop", token).get();
            String[] databases = shop.split("Database");
            shop = databases[1];
            System.out.println(shop);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String o = null;
//        try {
//            o = (String) socketConnect.get(2, TimeUnit.SECONDS);
//        } catch (ExecutionException | TimeoutException | InterruptedException e) {
//            e.printStackTrace();
//        }
        parseItems(shop);
        return o;
    }

    private void parseItems(String jsonFile) {
        try {
            System.out.println(jsonFile);

            JSONArray items = new JSONArray(jsonFile);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = items.getJSONObject(i);
                String name = jsonObject.getString("title");
                String img = jsonObject.getString("imgSrc");
                String cost = jsonObject.getString("cost");
                System.out.println(cost);
                System.out.println(name);
                System.out.println(img);
                names.add(name);
                imageURLs.add(img);
                costs.add(cost);
            }

            OurData.itemNames = new String[names.size()];
            OurData.itemNames = names.toArray(OurData.itemNames);
            OurData.itemImageUrls = new String[imageURLs.size()];
            OurData.itemImageUrls = imageURLs.toArray(OurData.itemImageUrls);
            OurData.itemCosts = new String[costs.size()];
            OurData.itemCosts = costs.toArray(OurData.itemCosts);
//            Toast.makeText(getContext(), "JSON Result. Name: "+name+" URL: "+img, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        names.clear();
        imageURLs.clear();

        super.onPause();
    }
}
