/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.smallvideo.maiguo.aliyun.media;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smallvideo.maiguo.R;


public class GalleryDirChooser {

    private PopupWindow popupWindow;
    //选择相册标题
    private View titleView;
    private boolean isShowGalleryDir;
    private GalleryDirAdapter adapter;
    private Activity mActivity;

    public GalleryDirChooser(Context context, View titleView,ThumbnailGenerator thumbnailGenerator,final MediaStorage storage){
        //选择视频标题
        this.titleView = titleView;
        this.mActivity = (Activity) context;
        RecyclerView recyclerView = (RecyclerView)View.inflate(context,R.layout.aliyun_svideo_import_layout_qupai_effect_container_normal, null);
        adapter = new GalleryDirAdapter(thumbnailGenerator);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setData(storage.getDirs());
        popupWindow = new PopupWindow(recyclerView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                context.getResources().getColor(android.R.color.white)));
        popupWindow.setOutsideTouchable(true);

        //暂时不需要切换视频路径，这里直接默认显示所有的视频
        /*titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storage.isActive()){
                    showOrHideGalleryDir();
                }
            }
        });*/

        storage.setOnMediaDirUpdateListener(
                new MediaStorage.OnMediaDirUpdate() {
                    @Override
                    public void onDirUpdate(MediaDir dir) {
                        GalleryDirChooser.this.titleView.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                        );

                    }
                }
        );

        adapter.setOnItemClickListener(new GalleryDirAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(GalleryDirAdapter adapter, int adapter_position) {
                MediaDir dir = adapter.getItem(adapter_position);
                showOrHideGalleryDir();
                storage.setCurrentDir(dir);
                return false;
            }
        });
    }

    public void setAllGalleryCount(int count){
        adapter.setAllFileCount(count);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showOrHideGalleryDir(){
        if(mActivity.isDestroyed()){
            return;
        }
        if(isShowGalleryDir){
            popupWindow.dismiss();
        }else{
            if (Build.VERSION.SDK_INT < 24) {
                popupWindow.showAsDropDown(titleView);
            }
            else {
                // 适配 android 7.0
                int[] location = new int[2];
                titleView.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(titleView, Gravity.NO_GRAVITY, 0, y + titleView.getHeight());
            }
        }
        isShowGalleryDir = !isShowGalleryDir;
        titleView.setActivated(isShowGalleryDir);
    }

    public boolean isShowGalleryDir() {
        return isShowGalleryDir;
    }

}
