package com.smallvideo.maiguo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.sys.AlivcSdkCore;
import com.smallvideo.maiguo.aliyun.faceunity.FaceUnityManager;

/**
 * Created by zhangxiaodong on 2019/1/25 9:32.
 * <br/>
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //加载动态库
//        System.loadLibrary("QuCore");
//        System.loadLibrary("live-openh264");
//        System.loadLibrary("fdk-aac");
//        System.loadLibrary("svideo_alivcffmpeg");
        //初始化相关配置
        FaceUnityManager.getInstance().setUp(this);
        QupaiHttpFinal.getInstance().initOkHttpFinal();
        com.aliyun.vod.common.httpfinal.QupaiHttpFinal.getInstance().initOkHttpFinal();
        AlivcSdkCore.register(getApplicationContext());
        AlivcSdkCore.setLogLevel(AlivcSdkCore.AlivcLogLevel.AlivcLogDebug);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //突破65536
        MultiDex.install(this);
    }
}
