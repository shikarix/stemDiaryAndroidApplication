package com.coistem.stemdiary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckingConnection  extends AsyncTask {

    public boolean checkConnection(Context context, String url) {
        if (isOnline(context))
        {
            try
            {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(url).openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000); //choose your own timeframe
                urlc.setReadTimeout(4000); //choose your own timeframe
                urlc.connect();
                System.out.println(urlc.getResponseCode());
                return (urlc.getResponseCode() == 200);
            } catch (IOException e)
            {
                return (false);  //connectivity exists, but no internet.
            }
        } else
        {
            return false;  //no connectivity
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        boolean b = checkConnection((Context) objects[0],(String) objects[1]);
        return b;
    }
}
