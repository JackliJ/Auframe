/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.smallvideo.maiguo.aliyun.downloader;

import android.database.sqlite.SQLiteDatabase;

public interface DbUpgradeListener {
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
