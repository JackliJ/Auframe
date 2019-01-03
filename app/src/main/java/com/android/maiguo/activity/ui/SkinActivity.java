package com.android.maiguo.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.android.maiguo.activity.R;
import com.maiguoer.component.http.utils.Utils;

import skin.support.SkinCompatManager;
import skin.support.utils.SkinPreference;



/**
 * 项目名称：mgev4_android
 * 类描述：语言环境
 * 创建人：Alan·zhang
 * 创建时间：2017年9月12日15:43:13
 * 修改人：Alan·zhang
 * 修改时间：2017年9月12日15:43:16
 * 修改备注：
 */
public class SkinActivity extends AppCompatActivity  {
    /**
     * 顶替状态栏位置
     */
    View vStatusBarV;
    TextView btnBack;

    View vStatus;

    /**
     * 跳入设置页面
     *
     * @param activity 上个页面
     */
    public static void navigateToSkinActivity(Activity activity) {
        Intent intent = new Intent(activity, SkinActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_activity);
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        vStatus = findViewById(R.id.v_status_bar);
        vStatusBarV = findViewById(R.id.v_status_bar);
        btnBack = findViewById(R.id.btn_back);

        findViewById(R.id.tv_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclicked();
            }
        });
        findViewById(R.id.tv_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclicked();
            }
        });
        findViewById(R.id.tv_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/guide/TestSkin").navigation();
            }
        });

    }

    private void onclicked(){
        if (TextUtils.isEmpty(SkinPreference.getInstance().getSkinName())) {
            SkinCompatManager.getInstance().loadSkin("skin-bule-release.skin", null);
        } else {
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
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
