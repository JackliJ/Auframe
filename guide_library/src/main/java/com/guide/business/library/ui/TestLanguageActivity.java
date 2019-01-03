package com.guide.business.library.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.guide.business.library.LanguageUtil;
import com.guide.business.library.R;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;
import com.maiguoer.component.http.utils.Utils;

import java.util.Locale;

import static java.lang.System.exit;

/**
 * 语言环境切换测试
 */

@Route(path = "/guide/TestLanguageActivity")
public class TestLanguageActivity extends FragmentActivity{

    View vStatusBarV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_testlanguage);
        vStatusBarV = findViewById(R.id.v_status_bar);

        //中文
        findViewById(R.id.tv_zh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageUtil.switchLanguage(Locale.SIMPLIFIED_CHINESE, TestLanguageActivity.this);
                ARouter.getInstance().build("/guide/TestSkin").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).navigation();
                exit(0);
            }
        });
        //英文
        findViewById(R.id.tv_en).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //英文
                LanguageUtil.switchLanguage(Locale.ENGLISH,TestLanguageActivity.this);
                ARouter.getInstance().build("/guide/TestSkin").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).navigation();
                exit(0);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, SharedPreferencesUtils.getAppLanguage(newBase)));
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.setTranslucent(this);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = Utils.getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
    }
 }
