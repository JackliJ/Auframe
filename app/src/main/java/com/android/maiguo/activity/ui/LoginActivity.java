package com.android.maiguo.activity.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.maiguo.activity.R;
import com.blankj.utilcode.util.LogUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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
                loginClick();
            }
        });
    }

    /**
     * 登录
     */
    private void loginClick() {
        final String username = vEdAccount.getText().toString().trim();
        String password = vEdPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.main_login_account_null), Toast.LENGTH_SHORT).show();
            vEdAccount.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.main_login_password_null), Toast.LENGTH_SHORT).show();
            vEdPassword.requestFocus();
            return;
        } else {
//            登录聊天服务器(登出成功后再登录)
            EMClient.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    EMClient.getInstance().login(username, "821c7b52df66ea267ce61c820128b3f3", new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //将会话由本地数据库加载到内存中
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    //跳转到首页
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.main_login_hx_success), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(final int i, final String s) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.main_login_hx_error), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onProgress(int var1, final String var2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    });

                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub
                    LogUtils.d("onError", code + "---" + message);
                }
            });
        }
    }
}
