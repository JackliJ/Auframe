package com.guide.business.library.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
    onToucherListener mlistener;
    public PhotoAdapter(Context context,ArrayList<String> imageLists,onToucherListener listener){
        this.mContext = context;
        this.mImageLists = imageLists;
        mlistener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext).load(mImageLists.get(position)).into(holder.imageView);
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            //按下时的Y坐标
            float keyDownY;
            //是否能托动
            boolean canStart = true;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        keyDownY = event.getY();
                        canStart = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY();
                        //开始拖拽
                        if(keyDownY - moveY > 50 && canStart){
                            canStart = false;
                            mlistener.onToucher(holder);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //点击处理
                        if(canStart){
                            Toast.makeText(mContext,"click"+position,Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
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

    public interface onToucherListener{
        void onToucher(ViewHolder holder);
    }
}
