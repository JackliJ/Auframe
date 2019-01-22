package com.android.maiguo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.maiguo.activity.R;
import com.android.maiguo.base.MaiGuoErBaseFragment;

/**
 * Created by zhangxiaodong on 2019/1/21 15:31.
 * <br/>
 * 我的
 */

public class MeFragment extends MaiGuoErBaseFragment {
    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_me, container, false);
        return mView;
    }
}
