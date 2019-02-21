/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.smallvideo.maiguo.aliyun.view.effects.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aliyun.svideo.sdk.external.struct.effect.EffectFilter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.maiguoer.widget.ShapedImageView;
import com.smallvideo.maiguo.R;
import com.smallvideo.maiguo.aliyun.image.AbstractImageLoaderTarget;
import com.smallvideo.maiguo.aliyun.image.ImageLoaderImpl;
import com.smallvideo.maiguo.aliyun.view.effects.filter.interfaces.OnFilterItemClickListener;
import com.smallvideo.maiguo.aliyun.widget.CircularImageView;

import java.util.List;

/**
 * 滤镜adapter
 * @author xlx
 */
public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener {

    private Context mContext;
    private OnFilterItemClickListener mItemClick;
    private int mSelectedPos = -1;
    private FilterViewHolder mSelectedHolder;
    private List<String> mFilterList;

    public FilterAdapter(Context context, List<String> dataList) {
        this.mContext = context;

        this.mFilterList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alivc_svideo_item_effect_filter, parent, false);
        FilterViewHolder filterViewHolder = new FilterViewHolder(view);
        filterViewHolder.frameLayout = (FrameLayout)view.findViewById(R.id.resource_image);
        return filterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FilterViewHolder filterViewHolder = (FilterViewHolder)holder;
        String name = mContext.getString(R.string.alivc_filter_none);
        String path = mFilterList.get(position);
        if (path == null || "".equals(path)) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.alivc_svideo_effect_none);
            filterViewHolder.mImage.setImageDrawable(drawable);
        } else {
            EffectFilter effectFilter = new EffectFilter(path);
            name = effectFilter.getName();
            if (filterViewHolder != null) {
                Glide.with(mContext).asBitmap().load(effectFilter.getPath() + "/icon.png").into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        filterViewHolder.mImage.setImageBitmap(resource);
                    }
                });
            }
        }

        if (mSelectedPos > mFilterList.size()) {
            mSelectedPos = 0;
        }

        //当前item被选择时改变图片及文字显示样式
        if (mSelectedPos == position) {
            filterViewHolder.mImage.setSelected(true);
            filterViewHolder.mName.setSelected(true);
            mSelectedHolder = filterViewHolder;
        } else {
            filterViewHolder.mImage.setSelected(false);
            filterViewHolder.mName.setSelected(false);
        }
        filterViewHolder.mName.setText(name);
        filterViewHolder.itemView.setTag(holder);
        filterViewHolder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }



    private static class FilterViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;

        CircularImageView mImage;
        TextView mName;

        public FilterViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.resource_image_view);
            mName = (TextView)itemView.findViewById(R.id.resource_name);
        }

    }

    public void setOnItemClickListener(OnFilterItemClickListener listener) {
        mItemClick = listener;
    }


    @Override
    public void onClick(View view) {
        if (mItemClick != null) {
            FilterViewHolder viewHolder = (FilterViewHolder)view.getTag();
            int position = viewHolder.getAdapterPosition();
            if (mSelectedPos != position) {
                if (mSelectedHolder != null && mSelectedHolder.mImage != null) {
                    mSelectedHolder.mImage.setSelected(false);
                }
                viewHolder.mImage.setSelected(true);
                mSelectedPos = position;
                mSelectedHolder = viewHolder;

                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.FILTER_EFFECT;
                effectInfo.setPath(mFilterList.get(position));
                effectInfo.id = position;
                mItemClick.onItemClick(effectInfo, position);
            }
        }
    }

    public void setDataList(List<String> list) {
        mFilterList.clear();
        mFilterList.add(null);
        mFilterList.addAll(list);
    }

    public void setSelectedPos(int position) {
        mSelectedPos = position;
    }
}
