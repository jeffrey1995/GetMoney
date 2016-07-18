package com.zhuanfa.money.getmoney;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tianxiying on 16/7/7.
 */
public class MyApplication extends Application {
    private static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getHttpQueue() {return queue;}

}
