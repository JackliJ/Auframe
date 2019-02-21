/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.smallvideo.maiguo.aliyun.edit;

import android.content.Context;
import android.content.Intent;


/**
 * 底部导航栏的 view stack
 */
public class ViewStack {

    private final static String TAG = ViewStack.class.getName();
    private ViewOperator mViewOperator;
    private final Context mContext;
    private ColorFilterChooserView mColorFilterCHoosrView;

    public ViewStack(Context context, AlivcEditView editView, ViewOperator viewOperator) {

        mContext = context;
        mViewOperator = viewOperator;
    }

    public void setActiveIndex(int value) {

        UIEditorPage index = UIEditorPage.get(value);

        switch (index) {
            case FILTER:
                // 颜色滤镜
                if (mColorFilterCHoosrView == null) {
                    mColorFilterCHoosrView = new ColorFilterChooserView(mContext);
                }
                mViewOperator.showBottomView(mColorFilterCHoosrView);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 设置view的可见状态, 会在activity的onStart和onStop中调用
     * @param isVisible true: 可见, false: 不可见
     */
    public void setVisibleStatus(boolean isVisible) {

    }


    /**
     * 播放时间回调器
     * @param playerListener PlayerListener
     */
    public void setPlayerListener(AlivcEditView.PlayerListener playerListener) {

    }
}
