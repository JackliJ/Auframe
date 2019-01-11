package com.android.maiguo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.maiguo.activity.event.MapLocationEvent;
import com.android.maiguo.activity.event.MapLocationSuccessEvent;
import com.blankj.utilcode.util.LogUtils;
import com.maiguoer.component.http.utils.ScreenListener;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by laotie on 2019/1/11.
 * 全局Service
 */

public class MaiGuoErService extends Service {

    private ScreenListener screenListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("MaiGuoErService", "start");
        EventBus.getDefault().register(this);
        // 初始化定位
        initMapLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("MaiGuoErService", "onDestroy");
        EventBus.getDefault().unregister(this);
        if (mLocationClient != null)
            mLocationClient.onDestroy();
        if (screenListener != null)
            screenListener.unregisterListener();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onMapLocationEvent(MapLocationEvent event) {
        // 初始化定位
        initMapLocation();
    }



    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    /**
     * 高德定位
     */
    private void initMapLocation() {
        //声明定位回调监听器
        AMapLocationListener locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LogUtils.d("Latitude = " + aMapLocation.getLatitude());
                LogUtils.d("Longitude = " + aMapLocation.getLongitude());
                // 保存当前定位城市
                SharedPreferencesUtils.SaveLocationCity(MaiGuoErService.this, aMapLocation.getCity());
                // 保存当前定位 所有信息
                SharedPreferencesUtils.SaveLocationInfo(MaiGuoErService.this, aMapLocation.toString());
                // Servic定位成功通知
                EventBus.getDefault().post(new MapLocationSuccessEvent(aMapLocation));
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置定位回调监听
        mLocationClient.setLocationListener(locationListener);
        // 声明mLocationOption对象
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置只定位一次
        locationOption.setOnceLocation(true);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(locationOption);
        //启动定位
        mLocationClient.startLocation();
    }




}
