package com.guide.business.library.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guide.business.library.R;

import java.util.ArrayList;

/**
 * Created by zhangxiaodong on 2019/1/3 16:11.
 * <br/>
 */

public class PhotoAdapter extends  RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    ArrayList<String> mImageLists ;
    Context mContext;
    public PhotoAdapter(Context context,ArrayList<String> imageLists){
        this.mContext = context;
        this.mImageLists = imageLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mImageLists.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }
}
