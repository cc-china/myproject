package com.my_project;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by nolan on 2017/9/21.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        setXunFlyRadio();
        super.onCreate();
    }

    private void setXunFlyRadio() {
        /**
         * 初始化讯飞语音
         * */
        SpeechUtility.createUtility(getApplicationContext(), "appid=" + getString(R.string.app_xun_fly_id));
    }

    //创建缓存代理
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
