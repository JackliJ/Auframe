package com.maiguoer.component.http.app;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;

/**
 * 网络终端接口
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

public abstract class MgeSubscriber<T extends BaseRequestBean> extends StringCallback {

    private final static String TAG = "http";

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        onStart();
    }

    @Override
    public void onSuccess(Response<String> response) {
        try {
            Log.d(TAG, response.body());
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T o = JSON.parseObject(response.body(), entityClass);
            onSuccess(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //缓存的数据直接返回到success的接口
    @Override
    public void onCacheSuccess(Response<String> response) {
        super.onCacheSuccess(response);
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T o = JSON.parseObject(response.body(), entityClass);
            onSuccess(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        onFailure(response.code(), response.message());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        onEnd();
    }

    /**
     * 开始请求  show
     */
    public abstract void onStart();



    /**
     * 成功的的处理
     *
     * @param result 返回的实体
     */
    public abstract void onSuccess(T result);

    /**
     * 失败时的处理
     *
     * @param code
     * @param msg
     */
    public abstract void onFailure(int code, String msg);

    /**
     * 失败时的处理
     */
    public abstract void onEnd();


}
