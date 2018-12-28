package com.android.maiguo.activity.app;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.maiguo.activity.BuildConfig;

/**
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

public class MGEBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {
        //阿里云路由注解初始化
        if (BuildConfig.DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog(); // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
