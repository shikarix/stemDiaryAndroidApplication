package com.coistem.stemdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class TimetableFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddingTimetableActivity.class);
                startActivityForResult(intent, 123);
            }
        });
        WebView webView = view.findViewById(R.id.webView);
        SimpleWebViewClient webViewClient = new SimpleWebViewClient(getActivity());
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://vk.com/coistem");
        webView.loadUrl("http://192.168.1.100:8080/timetableAndroid/1");
        return view;
    }
}
