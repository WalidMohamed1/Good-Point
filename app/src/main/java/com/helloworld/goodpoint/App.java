package com.helloworld.goodpoint;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;

import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.ui.myService.NotificationBroadcast;

public class App extends Application {
    private static App mInstance;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
        filter.addAction("android.intent.action.QUICKBOOT_POWERON");
        filter.addAction("android.intent.action.MY_PACKAGE_REPLACED");
        filter.addAction("com.helloworld.goodpoint.ui.myService.NotificationBroadcast");
        filter.addCategory("android.intent.category.DEFAULT");
        registerReceiver(new NotificationBroadcast(), filter);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getResourses() {
        return res;
    }

}
