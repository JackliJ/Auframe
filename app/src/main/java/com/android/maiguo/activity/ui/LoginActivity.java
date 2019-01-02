package com.android.maiguo.activity.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.maiguo.activity.R;

/**
 * 登录页面
 * Create by www.lijin@foxmail.com on 2018/12/29 0029.
 * <br/>
 */
@Route(path = "/main/LoginActivity")
public class LoginActivity extends Activity {

    private EditText vEdAccount, vEdPassword;
    private Button vBuLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
    }

    private void initView() {
        vEdAccount = findViewById(R.id.guide_login_account);
        vEdPassword = findViewById(R.id.guide_login_password);
        vBuLogin = findViewById(R.id.guide_login_bu_click);

        initData();
    }

    private void initData() {
        //登录环信
        vBuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
