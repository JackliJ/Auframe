package com.chat.business.library.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.chat.business.library.R;
import com.chat.business.library.ui.ChatMessageActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;
import com.maiguoer.component.http.permissions.PermissionsUtil;
import com.zhy.m.permission.MPermissions;

/**
 * Create by www.lijin@foxmail.com on 2019/1/4 0004.
 * <br/>
 */

public class ChatLoginActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_login_layout);
        findViewById(R.id.chat_test_qx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请权限
                MPermissions.requestPermissions(ChatLoginActivity.this, PermissionsUtil.REQUEST_CODE_PERMISSION, PermissionsUtil.PERMISSION_STRS);
            }
        });
        final EditText vAccount = (EditText) findViewById(R.id.chat_login_account);
        findViewById(R.id.chat_login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        EMClient.getInstance().login(vAccount.getText().toString(), "821c7b52df66ea267ce61c820128b3f3", new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //将会话由本地数据库加载到内存中
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        Toast.makeText(ChatLoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onError(final int i, final String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMClient.getInstance().logout(false);
                                        Toast.makeText(ChatLoginActivity.this, "login error", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }

                            @Override
                            public void onProgress(int var1, final String var2) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMClient.getInstance().logout(false);
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
        });
        findViewById(R.id.chat_test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatLoginActivity.this, ChatMessageActivity.class));
            }
        });
    }
}
