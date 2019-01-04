package com.activity.maiguo.map.library;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;

/**
 * Created by laotie on 2019/1/3.
 */

@Route(path = "/map/LocationActivity")
public class LocationActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
    }

    /**
     * 初始化
     */
    private void init() {

    }



}
