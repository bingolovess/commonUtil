package com.bingo.base;

import android.app.Application;

import com.bingo.base.utils.AppConfig;


/**
 * Created by bingo on 2018/12/27.
 * Time:2018/12/27
 */

public class YunApp extends Application {
    private static YunApp app;

    public static YunApp get() {
        return app;
    }

    //app启动的开始时间
    public long startTime;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        startTime = System.currentTimeMillis();
        AppConfig.getInstance().init(this);
    }
}
