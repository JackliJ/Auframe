/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.smallvideo.maiguo.aliyun.media;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smallvideo.maiguo.R;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class GalleryMediaChooser {

    private RecyclerView mRecyclerView;
    private GalleryAdapter adapter;
    private MediaStorage mStorage;

    public GalleryMediaChooser(RecyclerView recyclerView, final GalleryDirChooser dirChooser,
                               MediaStorage storage, ThumbnailGenerator thumbnailGenerator,final TextView ok){
        this.mRecyclerView = recyclerView;
        mRecyclerView.addItemDecoration(new GalleryItemDecoration());
        this.mStorage = storage;
        adapter = new GalleryAdapter(thumbnailGenerator);
        recyclerView.setLayoutManager(new WrapContentGridLayoutManager(recyclerView.getContext(),4, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
//        adapter.addDraftItem();
        adapter.setData(storage.getMedias());
        storage.setOnMediaDataUpdateListener(new MediaStorage.OnMediaDataUpdate() {
            @Override
            public void onDataUpdate(List<MediaInfo> list) {
                    int count = adapter.getItemCount();
                    int size = list.size();
                    int insert = count - size;
                    adapter.notifyItemRangeInserted(insert, size);
                    if (size == MediaStorage.FIRST_NOTIFY_SIZE || mStorage.getMedias().size() < MediaStorage.FIRST_NOTIFY_SIZE) {
                        selectedFirstMediaOnAll(list);
                    }
                dirChooser.setAllGalleryCount(mStorage.getMedias().size());

            }
        });

        //设置视频item点击事件
        adapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(GalleryAdapter adapter, int adapterPosition) {
                if (adapter.getItemCount() > adapterPosition) {
                    MediaInfo info = adapter.getItem(adapterPosition);
                    if (info != null) {
                        //保存当前被点击的视频信息
                        mStorage.setCurrentMedia(info);
                        //改变确定按扭颜色
                        ok.setClickable(true);
                        ok.setTextColor(ok.getContext().getResources().getColor(R.color.T4));
                    }
                }
                return true;
            }
        });
        //确定点击事件
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentMediaInfo();
            }
        });
    }
    //zxd 获取当前点击的视频信息
    public MediaInfo getCurrentMediaInfo(){
        return mStorage.getCurrentMedia();
    }

    public void setCurrentMediaInfoActived(){
        int pos = adapter.setActiveDataItem(mStorage.getCurrentMedia());
        mRecyclerView.smoothScrollToPosition(pos);
    }

    public void setDraftCount(int draftCount){
        adapter.setDraftCount(draftCount);
        adapter.notifyItemChanged(0);
    }

    private void selectedFirstMediaOnAll(List<MediaInfo> list){
        if(list.size() == 0){
            return ;
        }
        MediaInfo info = list.get(0);
//        mStorage.setCurrentDisplayMediaData(info);
        adapter.setActiveDataItem(info);
    }

    public void changeMediaDir(MediaDir dir){
        if(dir.id == -1){
            //adapter.addDraftItem();
            adapter.setData(mStorage.getMedias());
            selectedFirstMediaOnAll(mStorage.getMedias());
        }else{
            //adapter.removeDraftItem();
            adapter.setData(mStorage.findMediaByDir(dir));
            selectedFirstMediaOnAll(mStorage.findMediaByDir(dir));
        }
    }

    public RecyclerView getGallery(){
        return mRecyclerView;
    }

}
