package com.coistem.stemdiary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewsFragment extends Fragment {

    private static ArrayList<Map<String, Object>> news = new ArrayList<>();
    private View view;
    private static RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static boolean isFirstEnter = true;
    private boolean isAlreadyWork=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.listRecyclerView);
        progressBar = view.findViewById(R.id.newsProgressBar);
        progressBar.setVisibility(View.VISIBLE);
//        listView = view.findViewById(R.id.newsList);
        setToolbar(GetUserInfo.userName);
        vkRequest();
        return view;
    }

    private ArrayList<String> newsText = new ArrayList<>();
    private ArrayList<String> imageURLs = new ArrayList<>();
    private ArrayList<String> newsDates = new ArrayList<>();

    public void vkRequest() {

        OurData.title = null;
        OurData.imgUrls = null;
        OurData.dates = null;
        newsText.clear();
        imageURLs.clear();
        newsDates.clear();

        if(isAlreadyWork) {
            return;
        }

        isAlreadyWork = true;
        VKRequest request = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID,-113376999,VKApiConst.COUNT,20));
        System.out.println(request.toString());
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                String s = error.toString();
                int errorCode = error.errorCode;
                System.out.println("ERRRRRRRRRORRRRRRRRR: "+error.errorMessage);
                isFirstEnter = true;
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {

                    JSONArray items = response.json.getJSONObject("response").getJSONArray("items");
                    HashMap<String, Object> map = new HashMap<>();
                    for(int i = 0; i<20; i++) {
                        try {
                            JSONObject jsonObject = items.getJSONObject(i);
                            JSONArray attachments = jsonObject.getJSONArray("attachments");
                            JSONObject jsonObject1 = attachments.getJSONObject(0);
                            String date = jsonObject.getString("date");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                            String dateOfPost = sdf.format(Long.parseLong(date)*1000);
                            newsDates.add(dateOfPost);
                            if(jsonObject1.getString("type").equals("photo") && !jsonObject1.getString("type").equals("video")) {
                                JSONObject photos = jsonObject1.getJSONObject("photo");
                                String url = photos.getString("photo_807");
                                imageURLs.add(url);
                                String text = jsonObject.getString("text");
                                newsText.add(text);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    newsText.add("Большей новостей в нашей группе ВКонтакте!");
                    imageURLs.add("https://ic.pics.livejournal.com/lev_dmitrich/32679866/221346/221346_original.jpg");
                    OurData.title = new String[newsText.size()];
                    OurData.title = newsText.toArray(OurData.title);
                    OurData.imgUrls = new String[imageURLs.size()];
                    OurData.imgUrls = imageURLs.toArray(OurData.imgUrls);
                    OurData.dates = new String[newsDates.size()];
                    OurData.dates = newsDates.toArray(OurData.dates);

                    NewsListAdapter newsListAdapter = new NewsListAdapter();
                    recyclerView.setAdapter(newsListAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isAlreadyWork = false;

            }
        });
    }

    public void setToolbar(@NonNull String title) {
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(title);
    }

    @Override
    public void onPause() {
        OurData.dates = null;
        OurData.imgUrls = null;
        OurData.title = null;

        super.onPause();
    }
}
