package com.guide.business.library.weight;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.guide.business.library.R;
import com.maiguoer.component.http.base.BasisFragment;


/**
 * Created by Administrator on 2017/11/22.
 * 引导页单个图片的fragment
 */

public class ImageFragment extends BasisFragment {

    private View mView;
    /**
     * 图片控件
     */
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.image_fragment, container, false);
        mImageView = mView.findViewById(R.id.splash_image);
        return mView;
    }

    //显示图片
    public void setImage(int drawRes) {
        if (mImageView != null) {
            mImageView.setImageResource(drawRes);
        }
    }
}
