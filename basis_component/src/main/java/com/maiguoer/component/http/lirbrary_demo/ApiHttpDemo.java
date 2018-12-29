package com.maiguoer.component.http.lirbrary_demo;


/**
 * 单例  用于再次封装
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

public class ApiHttpDemo extends HttpMethodDemo {

    private static ApiHttpDemo INSTANCE = null;

    //获取单例
    public static ApiHttpDemo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApiHttpDemo();
        }
        return INSTANCE;
    }
}
