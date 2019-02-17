package com.smallvideo.maiguo.aliyun.view.control;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.smallvideo.maiguo.R;
import com.smallvideo.maiguo.aliyun.util.FastClickUtil;
import com.smallvideo.maiguo.aliyun.util.UIConfigManager;
import com.smallvideo.maiguo.aliyun.view.BaseScrollPickerView;
import com.smallvideo.maiguo.aliyun.view.StringScrollPicker;
import com.smallvideo.maiguo.aliyun.widget.FanProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ControlView extends RelativeLayout implements View.OnTouchListener {
    private static final String TAG = ControlView.class.getSimpleName();
    private static final int MAX_ITEM_COUNT = 5;
    //滤镜
    private TextView vFilterFace;
    /*美颜*/
    private TextView vBeauty;
    /*倒计时拍摄*/
    private ImageView ivReadyRecord;
    //闪光灯
    private ImageView aliyunSwitchLight;
    private ImageView aliyunSwitchCamera;
    /*回删*/
    private ImageView aliyunDelete;
    /*选择本地视频*/
    private ImageView vSelectLocal;
    /*下一步*/
    private ImageView aliyunComplete;
    //返回
    private ImageView aliyunBack;
    private LinearLayout aliyunRecordLayoutBottom;
    private TextView aliyunRecordDuration;
    /*录制按扭*/
    private FrameLayout aliyunRecordBtn;
//    private FanProgressBar aliyunRecordProgress;
    private TextView mRecordTipTV;
    private FrameLayout mTitleView;
    private ControlViewListener mListener;
    //录制模式
    private RecordMode recordMode = RecordMode.SINGLE_CLICK;
    //是否有录制片段，true可以删除，不可选择音乐、拍摄模式view消失
    private boolean hasRecordPiece = false;
    //是否可以完成录制，录制时长大于最小录制时长时为true
    private boolean canComplete = false;
    //音乐选择是否弹出
    private boolean isMusicSelViewShow = false;
    //其他弹窗选择是否弹出
    private boolean isEffectSelViewShow = false;
    //闪光灯类型
    private FlashType flashType = FlashType.OFF;
    //摄像头类型
    private CameraType cameraType = CameraType.FRONT;
    //录制速度
    private RecordRate recordRate = RecordRate.STANDARD;
    //录制状态，开始、暂停、准备,只是针对UI变化
    private RecordState recordState = RecordState.STOP;
    //录制按钮宽度
    private int itemWidth;
    //是否倒计时拍摄中
    private boolean isCountDownRecording = false;
    //是否实际正在录制，由于结束录制时UI立刻变化，但是尚未真正结束录制，所以此时不能继续录制视频否则会崩溃
    private boolean isRecording = false;

    public ControlView(Context context) {
        this(context, null);
    }

    public ControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        calculateItemWidth();
        //Inflate布局
        LayoutInflater.from(getContext()).inflate(R.layout.aliyun_svideo_view_control, this, true);
        assignViews();
        //设置view的监听事件
        setViewListener();
        //更新view的显示
        updateAllViews();
    }

    private void assignViews() {
        ivReadyRecord = (ImageView) findViewById(R.id.aliyun_ready_record);
        aliyunSwitchLight = (ImageView) findViewById(R.id.aliyun_switch_light);
        aliyunSwitchCamera = (ImageView) findViewById(R.id.aliyun_switch_camera);
        aliyunComplete = (ImageView) findViewById(R.id.aliyun_complete);
        //返回
        aliyunBack = (ImageView) findViewById(R.id.aliyun_back);
        aliyunRecordLayoutBottom = (LinearLayout) findViewById(R.id.aliyun_record_layout_bottom);
        aliyunRecordDuration = (TextView) findViewById(R.id.aliyun_record_duration);
        aliyunRecordBtn = (FrameLayout) findViewById(R.id.aliyun_record_bg);
//        aliyunRecordProgress = (FanProgressBar) findViewById(R.id.aliyun_record_progress);
        //回删
        aliyunDelete = (ImageView) findViewById(R.id.aliyun_delete);
        //选择本地视频
        vSelectLocal = (ImageView) findViewById(R.id.iv_select_local);
        //滤镜
        vFilterFace = findViewById(R.id.tv_beauty_face);
        //美颜
        vBeauty = findViewById(R.id.tv_beauty);
        mTitleView = findViewById(R.id.alivc_record_title_view);
        mRecordTipTV = findViewById(R.id.alivc_record_tip_tv);
        //设置图片
        UIConfigManager.setImageResourceConfig(
                new ImageView[]{ ivReadyRecord, aliyunComplete}
                , new int[]{ R.attr.countdownImage, R.attr.finishImageUnable}
                , new int[]{ R.mipmap.video_time_off, R.mipmap.video_next}
        );

        //回删对应的图片
        //拍摄中红点对应的图片
        UIConfigManager.setImageResourceConfig(
                new TextView[]{aliyunRecordDuration}
                , new int[]{ 0}
                , new int[]{ R.attr.dotImage}
                , new int[]{R.mipmap.alivc_svideo_record_time_tip});
        //切换摄像头的图片
        aliyunSwitchCamera.setImageDrawable(getSwitchCameraDrawable());
        List<String> strings = new ArrayList<>(2);
        strings.add(getResources().getString(R.string.alivc_record_click));
        strings.add(getResources().getString(R.string.alivc_record_long_press));
    }

    /**
     * 获取切换摄像头的图片的selector
     *
     * @return Drawable
     */
    private Drawable getSwitchCameraDrawable() {

        Drawable drawable = UIConfigManager.getDrawableResources(getContext(), R.attr.switchCameraImage, R.mipmap.alivc_svideo_icon_magic_turn);
        Drawable pressDrawable = drawable.getConstantState().newDrawable().mutate();
        pressDrawable.setAlpha(66);//透明度60%
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                pressDrawable);
        stateListDrawable.addState(new int[]{},
                drawable);
        return stateListDrawable;
    }

    /**
     * 给各个view设置监听
     */
    private void setViewListener() {
        //返回
        aliyunBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mListener != null) {
                    mListener.onBackClick();
                }
            }
        });
        //倒计时拍摄
        ivReadyRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (isRecording) {
                    return;
                }
                if (recordState == RecordState.STOP) {
                    recordState = RecordState.READY;
                    updateAllViews();
                    if (mListener != null) {
                        mListener.onReadyRecordClick(false);
                    }
                } else {
                    recordState = RecordState.STOP;
                    if (mListener != null) {
                        mListener.onReadyRecordClick(true);
                    }
                }
            }
        });
        //闪光灯
        aliyunSwitchLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (flashType == FlashType.ON) {
                    flashType = FlashType.OFF;
                } else {
                    flashType = FlashType.ON;
                }
                updateLightSwitchView();
                if (mListener != null) {
                    mListener.onLightSwitch(flashType);
                }
            }
        });
        //前后摄像头切换
        aliyunSwitchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mListener != null) {
                    mListener.onCameraSwitch();
                }
            }
        });
        //下一步
        aliyunComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (mListener != null) {
                    mListener.onNextClick();
                }
            }
        });
        //滤镜
        vFilterFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (FastClickUtil.isFastClick()) {
                        return;
                    }
                    mListener.onBeautyFaceClick();
                }
            }
        });
        //回删
        aliyunDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteClick();
                }
            }
        });
        //选择本地视频
        vSelectLocal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelectLocal();
            }
        });
        aliyunRecordBtn.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //防止多点触碰
        //if (event.getPointerCount()>1){
        //    return true;
        //}
        if (FastClickUtil.isRecordWithOtherClick()) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (recordState != RecordState.COUNT_DOWN_RECORDING && recordMode == RecordMode.LONG_PRESS) {
                if (isRecording) {
                    return true;
                } else {
                    if (mListener != null) {
                        mListener.onStartRecordClick();
                    }
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                || event.getAction() == MotionEvent.ACTION_UP) {
            if (recordState == RecordState.COUNT_DOWN_RECORDING) {
                if (mListener != null) {
                    mListener.onStopRecordClick();
                    setRecordState(RecordState.STOP);
                    //停止拍摄后立即展示回删
                    if(hasRecordPiece){
                        setHasRecordPiece(true);
                    }
                }
            } else {
                if (recordMode == RecordMode.LONG_PRESS) {
                    if (mListener != null) {
                        mListener.onStopRecordClick();
                        setRecordState(RecordState.STOP);
                        //停止拍摄后立即展示回删
                        if(hasRecordPiece){
                            setHasRecordPiece(true);
                        }
                    }
                } else {
                    if (recordState == RecordState.RECORDING) {
                        if (mListener != null) {
                            mListener.onStopRecordClick();
                            setRecordState(RecordState.STOP);
                            //停止拍摄后立即展示回删
                            if(hasRecordPiece){
                                setHasRecordPiece(true);
                            }
                        }
                    } else {
                        if (mListener != null && !isRecording) {
                            mListener.onStartRecordClick();
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获取录制按钮宽高
     */
    private void calculateItemWidth() {
        itemWidth = getResources().getDisplayMetrics().widthPixels / MAX_ITEM_COUNT;
    }

    /**
     * 更新所有视图
     */
    private void updateAllViews() {
        if (isMusicSelViewShow || recordState == RecordState.READY) {//准备录制和音乐选择的时候所有view隐藏
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            updateBottomView();
            updateTittleView();
        }
    }

    /**
     * 更新顶部视图
     */
    private void updateTittleView() {
        if (recordState == RecordState.STOP) {
            mTitleView.setVisibility(VISIBLE);
            updateLightSwitchView();
            updateCompleteView();
        } else {
            mTitleView.setVisibility(GONE);
        }
    }

    /**
     * 更新完成录制按钮
     */
    private void updateCompleteView() {
        if (canComplete) {
            //完成的按钮图片 - 可用
            aliyunComplete.setSelected(true);
            aliyunComplete.setEnabled(true);
            UIConfigManager.setImageResourceConfig(aliyunComplete, R.attr.finishImageAble, R.mipmap.video_next);
        } else {
            //完成的按钮图片 - 不可用
            aliyunComplete.setSelected(false);
            aliyunComplete.setEnabled(false);
            UIConfigManager.setImageResourceConfig(aliyunComplete, R.attr.finishImageUnable, R.mipmap.video_next);
        }
    }


    /**
     * 更新底部控制按钮
     */
    private void updateBottomView() {
        if (isEffectSelViewShow) {
            aliyunRecordLayoutBottom.setVisibility(GONE);
        } else {
            aliyunRecordLayoutBottom.setVisibility(VISIBLE);
            updateRecordBtnView();
            updateDeleteView();
            //设置滤镜可见性
            if (recordState == RecordState.STOP) {
                vFilterFace.setVisibility(VISIBLE);
            } else {
                vFilterFace.setVisibility(INVISIBLE);
            }
        }
    }

    /**
     * 更新删除按钮
     */
    private void updateDeleteView() {
        if (!hasRecordPiece || recordState == RecordState.RECORDING
                || recordState == RecordState.COUNT_DOWN_RECORDING) {
            aliyunDelete.setVisibility(GONE);
            aliyunComplete.setVisibility(GONE);

        } else {
            aliyunDelete.setVisibility(VISIBLE);
            aliyunComplete.setVisibility(VISIBLE);
        }

        if(hasRecordPiece){
            //如果有片段，则隐藏选择本地视频图标
            vSelectLocal.setVisibility(GONE);
        }else{
            //否则，显示选择本地视频图标
            vSelectLocal.setVisibility(VISIBLE);
        }
    }

    /**
     * 更新录制按钮状态
     */
    private void updateRecordBtnView() {

        if (recordState == RecordState.STOP) {
            //拍摄按钮图片 - 未开始拍摄
            aliyunRecordBtn.setBackgroundResource(R.mipmap.video_start_record);
            aliyunRecordDuration.setVisibility(GONE);
            mRecordTipTV.setVisibility(VISIBLE);
            if (recordMode == RecordMode.LONG_PRESS) {
                mRecordTipTV.setText(R.string.alivc_record_press);
            } else {
                mRecordTipTV.setText("");
            }
        } else if (recordState == RecordState.COUNT_DOWN_RECORDING) {
            mRecordTipTV.setVisibility(GONE);
            aliyunRecordDuration.setVisibility(VISIBLE);
            //拍摄按钮图片 - 拍摄中
            aliyunRecordBtn.setBackgroundResource(R.mipmap.video_stop_record);
        } else {
            mRecordTipTV.setVisibility(GONE);
            aliyunRecordDuration.setVisibility(VISIBLE);
            if (recordMode == RecordMode.LONG_PRESS) {
                //拍摄按钮图片 - 长按中
                aliyunRecordBtn.setBackgroundResource(R.mipmap.video_stop_record);
            } else {
                //拍摄按钮图片 - 拍摄中
                aliyunRecordBtn.setBackgroundResource(R.mipmap.video_stop_record);
            }
        }
    }

    /**
     * 更新闪光灯按钮
     */
    private void updateLightSwitchView() {
        if (cameraType == CameraType.FRONT) {
            aliyunSwitchLight.setClickable(false);
            // 前置摄像头状态, 闪光灯图标变灰
            aliyunSwitchLight.setColorFilter(ContextCompat.getColor(getContext(), R.color.alivc_svideo_gray));
            UIConfigManager.setImageResourceConfig(aliyunSwitchLight, R.attr.lightImageUnable, R.mipmap.video_flash_shut);

        } else if (cameraType == CameraType.BACK) {
            aliyunSwitchLight.setClickable(true);
            // 后置摄像头状态, 清除过滤器
            aliyunSwitchLight.setColorFilter(null);
            switch (flashType) {
                case ON:
                    aliyunSwitchLight.setSelected(true);
                    aliyunSwitchLight.setActivated(false);
                    UIConfigManager.setImageResourceConfig(aliyunSwitchLight, R.attr.lightImageOpen, R.mipmap.video_flash_open);
                    break;
                case OFF:
                    aliyunSwitchLight.setSelected(true);
                    aliyunSwitchLight.setActivated(true);
                    UIConfigManager.setImageResourceConfig(aliyunSwitchLight, R.attr.lightImageClose, R.mipmap.video_flash_shut);
                    break;
                default:
                    break;
            }
        }

    }

    public FlashType getFlashType() {
        return flashType;
    }

    public CameraType getCameraType() {
        return cameraType;
    }

    public RecordState getRecordState() {
        if (recordState.equals(RecordState.COUNT_DOWN_RECORDING) || recordState.equals(RecordState.RECORDING)) {
            return RecordState.RECORDING;
        }
        return recordState;
    }

    public void setRecordState(RecordState recordState) {
        if (recordState == RecordState.RECORDING) {
            if (this.recordState == RecordState.READY) {
                this.recordState = RecordState.COUNT_DOWN_RECORDING;
            } else {
                this.recordState = recordState;
            }
        } else {
            this.recordState = recordState;
        }
        updateAllViews();
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 是否有录制片段
     *
     * @param hasRecordPiece
     */
    public void setHasRecordPiece(boolean hasRecordPiece) {
        this.hasRecordPiece = hasRecordPiece;
        updateDeleteView();
    }

    public boolean isHasRecordPiece() {
        return hasRecordPiece;
    }

    /**
     * 音乐选择弹窗显示回调
     *
     * @param musicSelViewShow
     */
    public void setMusicSelViewShow(boolean musicSelViewShow) {
        isMusicSelViewShow = musicSelViewShow;
        updateAllViews();
    }

    /**
     * 其他特效选择弹窗回调
     *
     * @param effectSelViewShow
     */
    public void setEffectSelViewShow(boolean effectSelViewShow) {
        isEffectSelViewShow = effectSelViewShow;
        updateBottomView();
    }

    /**
     * 设置录制事件，录制过程中持续被调用
     *
     * @param recordTime
     */
    public void setRecordTime(String recordTime) {
        aliyunRecordDuration.setText(recordTime);
    }

    /**
     * 添加各个控件点击监听
     *
     * @param mListener
     */
    public void setControlViewListener(ControlViewListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 设置摄像头类型，并刷新页面，摄像头切换后被调用
     *
     * @param cameraType
     */
    public void setCameraType(CameraType cameraType) {
        this.cameraType = cameraType;
        //        updateCameraView();
        updateLightSwitchView();
    }

    /**
     * 设置complete按钮是否可以点击
     *
     * @param enable
     */
    public void setCompleteEnable(boolean enable) {
        canComplete = enable;
        updateCompleteView();
    }
}
