package com.smallvideo.maiguo.aliyun.bean;

import java.util.ArrayList;

/**
 * Created by zhangxiaodong on 2018/10/31 16:24.
 * <br/>
 */

public class GoodCallbackEvent {
    private ArrayList<GoodBeanEvent> ls;

    public GoodCallbackEvent(ArrayList<GoodBeanEvent> ls){
        this.ls = ls;
    }

    public ArrayList<GoodBeanEvent> getLs() {
        return ls;
    }
}
