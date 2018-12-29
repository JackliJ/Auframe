package com.android.maiguo.activity;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 应用主页 用于启动项目 这里展示项目主页  完成初始化后 跳转到引导广告页面
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //展示APP主页 准备一些需要在项目运行需要准备的东西  如下载广告 引导页等  建议停留不超过2秒  这里使用延时展示效果
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ARouter.getInstance().build("/guide/GuiDeMain")
                        .navigation();
            }
        }, 2000);
    }
}
