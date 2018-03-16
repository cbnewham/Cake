package com.waracle.androidtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by cbnewham on 14/03/2018.
 */
public class MyApplication  extends Application
{
    private static MyApplication instance;


    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }


    public static Context getContext()
    {
        return instance.getApplicationContext();
    }
}
