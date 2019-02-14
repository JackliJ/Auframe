/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.android.maiguo.activity.aliyun.view.effects.http;

public interface HttpCallback<T> {
    void onSuccess(T result);
    void onFailure(Throwable e);
}
