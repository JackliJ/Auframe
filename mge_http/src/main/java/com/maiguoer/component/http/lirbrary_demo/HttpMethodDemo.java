package com.maiguoer.component.http.lirbrary_demo;

import android.content.Context;
import android.util.ArrayMap;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.maiguoer.component.http.app.MgeSubscriber;
import com.maiguoer.component.http.lirbrary_demo.bean.LoginBean;
import com.maiguoer.component.http.lirbrary_demo.bean.TestBean;
import com.maiguoer.component.http.lirbrary_demo.bean.UserBean;
import com.maiguoer.component.http.utils.HttpConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

public class HttpMethodDemo {

    /**
     * Get请求
     *
     * @param tag           请求标签
     * @param mgeSubscriber 请求回调
     */
    public void GetHttPJson(String tag, final MgeSubscriber<TestBean> mgeSubscriber) {
        String URL = "http://t.weather.sojson.com/api/weather/city/101030100";
        Map<String, String> params = new ArrayMap<>();
        params.put("_id", "26");
        params.put("id", "26");
        params.put("pid", "0");
        params.put("city_code", "101030100");
        params.put("city_name", "天津");
        OkGo.<String>get(URL)       //在这里设置请求地址和方式
                .tag(tag)             //设置请求Tag  用于取消
                .cacheKey(tag + "_cache")     //设置缓存名称
                .cacheTime(36000000)             //当前缓存的有效时间是多长 单位毫秒  这里为3600秒 即缓存保留一小时
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)//设置缓存模式 先请求网络  如果网络请求失败则读取上一次的缓存
                .params(params)            //设置请求参数
                .execute(mgeSubscriber);
    }

    /**
     * 登录
     *
     * @param tag
     * @param username
     * @param password
     * @param mgeSubscriber
     */
    public void StartLogin(Context mContext, String tag, String username, String password, final MgeSubscriber<LoginBean> mgeSubscriber) {
        String Path = "v2.0/login";
        String URL = HttpConfig.HTTP_URL + Path;
        Map<String, String> params = new ArrayMap<>();
        params.put("username", username);
        params.put("password", password);
        params.putAll(HttpConfig.Builder(mContext, Path));
        OkGo.<String>post(URL)       //在这里设置请求地址和方式
                .tag(tag)             //设置请求Tag  用于取消
                .cacheMode(CacheMode.NO_CACHE)//不使用缓存
                .params(params)            //设置请求参数
                .execute(mgeSubscriber);
    }

    /**
     * 请求用户数据
     *
     * @param tag
     * @param mgeSubscriber
     */
    public void GetMemberShowInfo(Context mContext, String tag, final MgeSubscriber<UserBean> mgeSubscriber) {
        String Path = "v2.0/member/show_info";
        String URL = HttpConfig.HTTP_URL + Path;
        Map<String, String> params = new ArrayMap<>();
        params.putAll(HttpConfig.Builder(mContext, Path));
        OkGo.<String>get(URL)       //在这里设置请求地址和方式
                .tag(tag)             //设置请求Tag  用于取消
                .cacheKey(tag + "_" + System.currentTimeMillis())     //设置缓存名称
                .cacheTime(36000000)             //当前缓存的有效时间是多长 单位毫秒  这里为3600秒 即缓存保留一小时
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)//设置缓存模式 先请求网络  如果网络请求失败则读取上一次的缓存
                .params(params)            //设置请求参数
                .execute(mgeSubscriber);
    }

}
