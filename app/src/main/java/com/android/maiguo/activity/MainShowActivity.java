package com.android.maiguo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 启动页
 * Create by www.lijin@foxmail.com on 2019/1/22 0022.
 * <br/>
 */

public class MainShowActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_main_show_layout);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ARouter.getInstance().build("/chat/ChatLoginActivity").navigation();
            }
        }, 2000);
    }
}
