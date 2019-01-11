package com.android.maiguo.activity.event;

import android.app.Activity;

/**
 * Created by Sky on 2017.10.13.
 * <br/>
 * 高德地图定位
 */

public class MapLocationEvent {

    Activity mapLocationActivity;

    public MapLocationEvent(Activity activity) {
        this.mapLocationActivity = activity;
    }

    public Activity getMapLocationActivity() {
        return mapLocationActivity;
    }
}
