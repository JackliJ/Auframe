package com.guide.business.library.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.guide.business.library.R;
import com.maiguoer.component.http.utils.Utils;

/**
 * 引导页和广告页面 判断用户是否首次登陆 如果是则展示引导页面  否则展示广告页面  默认五秒
 * Create by www.lijin@foxmail.com on 2018/12/28 0028.
 * <br/>
 */

@Route(path = "/guide/SendPhotoAlbumActivity")
public class SendPhotoAlbumActivity extends FragmentActivity{

    View vStatusBarV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_send_phone_album);
        vStatusBarV = findViewById(R.id.v_status_bar);


    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.setTranslucent(this,R.color.b2);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = Utils.getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
    }

 }
