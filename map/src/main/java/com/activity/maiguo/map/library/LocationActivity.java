package com.activity.maiguo.map.library;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.model.MyLocationStyle;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;
import com.maiguoer.component.http.utils.Utils;
import com.maiguoer.widget.MgeToast;
import com.maiguoer.widget.dialog.CustomDialog;

/**
 * Created by laotie on 2019/1/3.
 */

@Route(path = "/map/LocationActivity")
public class LocationActivity extends BaseSwipeBackActivity implements View.OnClickListener {
    /**
     * 顶替状态栏位置
     */
    View vStatusBarV;

    Button v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLightStatusBar(this, true);
        setContentView(R.layout.activity_location);
        v1 = (Button) findViewById(R.id.v_1);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        vStatusBarV = findViewById(R.id.v_status_bar);

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。

    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.setTranslucent(this, R.color.b2);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = Utils.getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.v_1) {

        }else if (v.getId() == R.id.v_2) {
            MgeToast.showErrorToast(LocationActivity.this,"6666666");
        }

    }
}
