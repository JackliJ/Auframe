package com.smallvideo.maiguo.aliyun.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.smallvideo.maiguo.aliyun.view.effects.filter.UIEditorPage;

/**
 * Created by cross_ly on 2018/8/27.
 * <p>描述:
 */
public abstract class BaseChooser extends FrameLayout {

    protected final static String TAG = BaseChooser.class.getName();
    protected FrameLayout mThumbContainer;
    protected AlivcEditView.PlayerListener mPlayerListener;
    private View mTransparentView;

    public BaseChooser(@NonNull Context context) {
        super(context);
    }

    public BaseChooser(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseChooser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mThumbContainer = getThumbContainer();
    }

    /**
     * 需要显示缩略图滑动条的子类选择实现
     * @return FrameLayout
     */
    protected FrameLayout getThumbContainer() {
        return null;
    }

    /**
     * 子类继承实现 用于显示指定的缩略图覆盖效果
     * @return UIEditorPage
     */
    protected UIEditorPage getUIEditorPage() {
        return null;
    }

    public void removeOwn(){
        ViewParent parent = this.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }
        onRemove();
    }

    /**
     * 设置整个容器是否可点击
     * @param clickable 默认可点击
     */
    public void setClickable(boolean clickable){
        setClickable(this,clickable );
    }
    private void setClickable(ViewGroup viewGroup,boolean clickable){
        if (mTransparentView == null) {
            mTransparentView = new ImageButton(getContext());

            ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
            mTransparentView.setLayoutParams(layoutParams);
            mTransparentView.setBackgroundColor(0x00000000);
        }
        ViewParent parent = mTransparentView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mTransparentView);
        }
        viewGroup.addView(mTransparentView);
        if (clickable){
            mTransparentView.setVisibility(GONE);
        }else {
            mTransparentView.setVisibility(VISIBLE);
        }
    }
    public void setThumbScrollEnable(boolean enable){
        if (mThumbContainer!=null) {
            setClickable(mThumbContainer,enable );
        }
    }
    /**
     * remove this 的时候调用，用于销毁资源
     */
    protected void onRemove() {
        Log.d(TAG, "---------------- onRemove -------------");
        if (mThumbContainer != null) {
            View childAt = mThumbContainer.getChildAt(0);
            if (childAt != null) {
//                ((ThumbLineBar) childAt).hide();
            }
        }
    }

    /**
     * 初始化
     */
    protected abstract void init();

    public int getCalculateHeight(){
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight == 0){
            measure(0,0);
            measuredHeight = getMeasuredHeight();
        }
        return measuredHeight;
    }

    /**
     * 是否需要缩放播放界面
     * @return boolean
     */
    public abstract boolean isPlayerNeedZoom();

    /**
     * 是否显示确认界面
     * @return boolean
     */
    public boolean isShowSelectedView(){
        return true;
    }

    public void onBackPressed(){
    }

    public void setPlayerListener(AlivcEditView.PlayerListener playerListener) {
        this.mPlayerListener = playerListener;
    }
}
