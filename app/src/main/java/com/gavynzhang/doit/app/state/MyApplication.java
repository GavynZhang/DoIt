package com.gavynzhang.doit.app.state;

import android.app.Application;
import android.content.Context;

/**
 * Created by GavynZhang on 2016/8/20 2:14.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
