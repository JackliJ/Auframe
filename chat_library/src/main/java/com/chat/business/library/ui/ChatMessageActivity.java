package com.chat.business.library.ui;

import android.os.Bundle;

import com.chat.business.library.R;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;

/**
 * 单聊
 * Create by www.lijin@foxmail.com on 2019/1/3 0003.
 * <br/>
 */
public class ChatMessageActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_sing_layout);
    }
}
