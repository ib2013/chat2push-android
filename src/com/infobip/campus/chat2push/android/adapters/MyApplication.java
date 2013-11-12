package com.infobip.campus.chat2push.android.adapters;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	
    public void onCreate() {
        super.onCreate();
    }

    private static Activity mCurrentActivity = null;
    
    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }
    
    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
  
}