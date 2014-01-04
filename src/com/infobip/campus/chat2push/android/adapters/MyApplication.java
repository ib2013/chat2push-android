package com.infobip.campus.chat2push.android.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {	
    private static Activity mCurrentActivity = null;
    private static Context  currentApplication = null;
	
    public void onCreate() {
        super.onCreate();
        currentApplication = getApplicationContext();
    }
    
    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }
    
    public void setCurrentActivity(Activity mCurrentActivity) {
        MyApplication.mCurrentActivity = mCurrentActivity;
    }
    
    public static Context getAppContext() {
        return currentApplication;
    }   
}