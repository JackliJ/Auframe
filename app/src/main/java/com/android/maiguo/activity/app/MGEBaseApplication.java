package com.android.maiguo.activity.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.maiguo.activity.BuildConfig;
import com.guide.business.library.LanguageUtil;
import com.maiguoer.component.http.app.BaseHttpApplication;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

public class MGEBaseApplication extends BaseHttpApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {

        //换肤
        SkinCompatManager.withoutActivity(this)                 // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())    // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())  // ConstraintLayout 控件换肤初 始化[可选]
                .addInflater(new SkinCardViewInflater())        // CardView v7 控件换肤初始化[可选]
                .loadSkin();

        //阿里云路由注解初始化
        if (BuildConfig.DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog(); // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(base, SharedPreferencesUtils.getAppLanguage(base)));
    }
}
