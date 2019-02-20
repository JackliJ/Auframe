package com.smallvideo.maiguo.aliyun.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.global.AliyunTag;
import com.aliyun.editor.EditorCallBack;
import com.aliyun.querrorcode.AliyunEditorErrorCode;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunIComposeCallBack;
import com.aliyun.qupai.editor.AliyunIEditor;
import com.aliyun.qupai.editor.impl.AliyunEditorFactory;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBase;
import com.aliyun.svideo.sdk.external.struct.effect.EffectBean;
import com.maiguoer.widget.CustomCircleProgressBar;
import com.smallvideo.maiguo.R;
import com.smallvideo.maiguo.activitys.VideoPublishActivity;
import com.smallvideo.maiguo.aliyun.util.FixedToastUtils;
import com.smallvideo.maiguo.aliyun.util.UIConfigManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

/**
 * @author zsy_18 data:2018/8/24
 */
public class AlivcEditView extends RelativeLayout  implements View.OnClickListener, OnTabChangeListener{
    private static final String TAG = AlivcEditView.class.getName();
    /**
     * 编辑核心接口类
     */
    private AliyunIEditor mAliyunIEditor;

    /**
     * 底部滑动item的横向ScrollView
     */
    private HorizontalScrollView mBottomLinear;
    /**
     * 编辑需要渲染显示的SurfaceView
     */
    private SurfaceView mSurfaceView;
    /**
     * 底部菜单点击事件管理类
     */
    private TabGroup mTabGroup;
    /**
     * 处理底部菜单点击事件
     */
    private ViewStack mViewStack;
    /**
     * 控件
     */
    private RelativeLayout mActionBar;
    private FrameLayout resCopy;
    public  FrameLayout mPasterContainer;
    private FrameLayout mGlSurfaceContainer;
    private ImageView mIvLeft;
    private TextView mIvRight;
    private LinearLayout mBarLinear;
    private TextView mPlayImage;
    private TextView mTvCurrTime;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 状态，正在添加滤镜特效那个中
     */
    private boolean mUseAnimationFilter = false;
    /**
     * 状态，界面是否被销毁
     */
    private boolean mIsDestroyed = false;
    /**
     * 状态，与生命周期onStop有关
     */
    private boolean mWaitForReady = false;
    /**
     * 音量
     */
    private int mVolume = 50;
    /**
     * 控制UI变动
     */
    private ViewOperator mViewOperate;
    //播放时间、显示时间、缩略图位置同步接口
    private PlayerListener mPlayerListener;
    private Toast showToast;
    /**
     * 合成时的路径
     */
    private String mPath = "/sdcard/output_compose.mp4";
    /**
     * 线程池
     */
    private ExecutorService executorService;
    /**
     * 合成进度
     */
    private CustomCircleProgressBar mCircleProgress;
    /**
     * 滤镜
     */
    private TextView mFilter;

    public AlivcEditView(Context context) {
        this(context, null);
    }

    public AlivcEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlivcEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        EventBus.getDefault().register(this);

        Point point = new Point();
        WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        LayoutInflater.from(getContext()).inflate(R.layout.aliyun_svideo_activity_editor, this, true);
        initView();
        initListView();
        add2Control();
        initThreadHandler();
        copyAssets();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mFilter = findViewById(R.id.tv_beauty);
        resCopy = (FrameLayout)findViewById(R.id.copy_res_tip);
//        mTransCodeTip = (FrameLayout)findViewById(R.id.transcode_tip);
//        mTransCodeProgress = (ProgressBar)findViewById(R.id.transcode_progress);
        mBarLinear = (LinearLayout)findViewById(R.id.bar_linear);
        mBarLinear.bringToFront();
        mActionBar = (RelativeLayout)findViewById(R.id.action_bar);
        mActionBar.setBackgroundDrawable(null);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvRight = (TextView)findViewById(R.id.tv_right);
        mCircleProgress = findViewById(R.id.custom_progress);
        mIvLeft.setImageResource(R.mipmap.aliyun_svideo_icon_back);
        mIvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });
//        mTvCurrTime = (TextView)findViewById(R.id.tv_curr_duration);

        mGlSurfaceContainer = (FrameLayout)findViewById(R.id.glsurface_view);
        mSurfaceView = (SurfaceView)findViewById(R.id.play_view);
        mBottomLinear = findViewById(R.id.edit_bottom_tab);
        setBottomTabResource();
        mPasterContainer = (FrameLayout)findViewById(R.id.pasterView);

        mPlayImage = findViewById(R.id.play_button);
        mPlayImage.setOnClickListener(this);
        switchPlayStateUI(false);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomEditorView();
            }
        });

    }

    /**
     * 设置底部效果按钮图标资源
     */
    private void setBottomTabResource() {
        TextView[] textViews = {
                //zxd 底部栏  滤镜、音乐、动画、字幕、MV
            findViewById(R.id.tab_filter),
        };
        int length = textViews.length;
        int[] index = new int[length];
        for (int i = 0; i < length; i++) {
            //所有的图片方向都是top
            index[i] = 1;
        }
        int[] attrs = {
            R.attr.filterImage,
        };
        int[] defaultResourceIds = {
            R.mipmap.video_filter_face,
        };
        UIConfigManager.setImageResourceConfig(textViews,index,attrs,defaultResourceIds);
    }

    private void initGlSurfaceView() {
        if (mVideoParam == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams)mGlSurfaceContainer.getLayoutParams();
        FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams)mSurfaceView.getLayoutParams();
        int rotation = mAliyunIEditor.getRotation();
        int outputWidth = mVideoParam.getOutputWidth();
        int outputHeight = mVideoParam.getOutputHeight();
        if ((rotation == 90 || rotation == 270)) {
            int temp = outputWidth;
            outputWidth = outputHeight;
            outputHeight = temp;
        }

        float percent;
        if (outputWidth >= outputHeight) {
            percent = (float)outputWidth / outputHeight;
        } else {
            percent = (float)outputHeight / outputWidth;
        }
        //指定surfaceView的宽高比是有必要的，这样可以避免某些非标分辨率下造成显示比例不对的问题
        surfaceLayout.width = mScreenWidth;
        surfaceLayout.height = Math.round((float)outputHeight * mScreenWidth / outputWidth);

        MarginLayoutParams marginParams = null;
        if (layoutParams instanceof MarginLayoutParams) {
            marginParams = (MarginLayoutParams)surfaceLayout;
        } else {
            marginParams = new MarginLayoutParams(surfaceLayout);
        }
        if (percent < 1.5 || (rotation == 90 || rotation == 270)) {
            marginParams.setMargins(0,
                getContext().getResources().getDimensionPixelSize(R.dimen.alivc_svideo_title_height), 0, 0);
        } else {
            if (outputWidth > outputHeight) {
                marginParams.setMargins(0,
                    getContext().getResources().getDimensionPixelSize(R.dimen.alivc_svideo_title_height) * 2, 0, 0);
            }
        }
        mGlSurfaceContainer.setLayoutParams(layoutParams);
        mPasterContainer.setLayoutParams(marginParams);
        mSurfaceView.setLayoutParams(marginParams);
    }

    private void initListView() {
        mViewOperate = new ViewOperator(this, mActionBar, mSurfaceView, mBottomLinear, mPasterContainer, mPlayImage);
//        mEditorService = new EditorService();
        mTabGroup = new TabGroup();
        mViewStack = new ViewStack(getContext(), this, mViewOperate);
        mTabGroup.addView(findViewById(R.id.tab_filter));
    }

    private void add2Control() {
        TabViewStackBinding tabViewStackBinding = new TabViewStackBinding();
        tabViewStackBinding.setViewStack(mViewStack);
        mTabGroup.setOnCheckedChangeListener(tabViewStackBinding);
        mTabGroup.setOnTabChangeListener(this);
    }

    private void initEditor() {
        mAliyunIEditor = AliyunEditorFactory.creatAliyunEditor(mUri, mEditorCallback);
        initGlSurfaceView();

        VideoDisplayMode mode = mVideoParam.getScaleMode();
        int ret = mAliyunIEditor.init(mSurfaceView, getContext().getApplicationContext());
        mAliyunIEditor.setDisplayMode(mode);
        mAliyunIEditor.setVolume(mVolume);
        mAliyunIEditor.setFillBackgroundColor(Color.BLACK);
        if (ret != AliyunErrorCode.OK) {
            showToast = FixedToastUtils.show(getContext(),
                getResources().getString(R.string.aliyun_svideo_editor_init_failed));
            return;
        }
//        mEditorService.addTabEffect(UIEditorPage.MV, mAliyunIEditor.getMVLastApplyId());
//        mEditorService.addTabEffect(UIEditorPage.FILTER_EFFECT, mAliyunIEditor.getFilterLastApplyId());
//        mEditorService.addTabEffect(UIEditorPage.AUDIO_MIX, mAliyunIEditor.getMusicLastApplyId());
//        mEditorService.setPaint(mAliyunIEditor.getPaintLastApply());

        mIvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                mIvRight.setEnabled(false);
                //显示合成进度条
                mCircleProgress.setVisibility(VISIBLE);
                //删除之前合并的文件
                File file = new File(mPath);
                if(file.exists()){
                    file.delete();
                }
                //合成方式分为两种，当前页面合成（前台页面）和其他页面合成（后台合成，这里后台并不是真正的app退到后台）
                //前台合成如下：如果要直接合成（当前页面合成），请打开注释，参考注释代码这种方式
                int ret = mAliyunIEditor.compose(mVideoParam, mPath, new
                 AliyunIComposeCallBack() {
                                    @Override
                                    public void onComposeError(int errorCode) {
                                        Log.e(AliyunTag.TAG, "Compose error, error code "+errorCode);
                                    }

                                    @Override
                                    public void onComposeProgress(int progress) {
                                        Log.e(AliyunTag.TAG, "Compose progress "+progress+"%");
                                        mCircleProgress.setProgress(progress);
                                    }

                                    @Override
                                    public void onComposeCompleted() {
                                        Log.e(AliyunTag.TAG, "Compose complete");
                                        post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mCircleProgress.setVisibility(GONE);
                                                //跳发布界面
                                                VideoPublishActivity.nativeToVideoPublishActivity(getContext(),mPath);
                                            }
                                        });
                                    }
                                });
                                if(ret != AliyunErrorCode.OK) {
                                    Log.e(AliyunTag.TAG, "Compose error, error code "+ret);
                                    v.setEnabled(true);//compose error
                                }
                //后台合成如下：如果要像Demo默认的这样，在其他页面合成，请参考下面这种方式
            /*    mAliyunIEditor.saveEffectToLocal();
                final AliyunIThumbnailFetcher fetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
                fetcher.fromConfigJson(mUri.getPath());
                fetcher.setParameters(mAliyunIEditor.getVideoWidth(), mAliyunIEditor.getVideoHeight(),
                AliyunIThumbnailFetcher.CropMode.Mediate, VideoDisplayMode.FILL, 1);
                fetcher.requestThumbnailImage(new long[] {0}, new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
                    @Override
                    public void onThumbnailReady(Bitmap bitmap, long l) {
                        String path = getContext().getExternalFilesDir(null) + "thumbnail.jpeg";
                        FileOutputStream fileOutputStream = null;
                        try {
                           fileOutputStream = new FileOutputStream(path);
                           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                    fileOutputStream = null;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
//                        Intent intent = new Intent(getContext(), PublishActivity.class);
                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        intent.putExtra(PublishActivity.KEY_PARAM_THUMBNAIL, path);
//                        intent.putExtra(PublishActivity.KEY_PARAM_CONFIG, mUri.getPath());
//                        intent.putExtra(PublishActivity.KEY_PARAM_ENTRANCE, entrance);
                        getContext().startActivity(intent);
                        if (mIvRight != null) {
                            mIvRight.setEnabled(true);
                        }
                        fetcher.release();
                    }

                    @Override
                    public void onError(int errorCode) {
                        fetcher.release();
                    }
                });*/
            }
        });

        mPlayerListener = new PlayerListener() {
            @Override
            public long getCurrDuration() {
                return mAliyunIEditor.getCurrentStreamPosition();
            }

            @Override
            public long getDuration() {
                long streamDuration = mAliyunIEditor.getStreamDuration();
                Log.d(TAG, "getDuration: " + streamDuration);
                return streamDuration;
            }

            @Override
            public void updateDuration(long duration) {
                //顶部时间显示
//                mTvCurrTime.setText(convertDuration2Text(duration));
            }
        };
        mViewStack.setPlayerListener(mPlayerListener);
        mAliyunIEditor.play();
    }


    /**
     * 更改播放状态的图标和文字 播放时,文字内容显示为: 暂停播放, 图标使暂停图标, mipmap/aliyun_svideo_pause 暂停时,文字内容显示为: 播放全篇, 图标使用播放图标,
     * mipmap/aliyun_svideo_play
     *
     * @param changeState, 需要显示的状态,  true: 播放全篇, false: 暂停播放
     */
    public void switchPlayStateUI(boolean changeState) {
        if (changeState) {
            mPlayImage.setText(getResources().getString(R.string.alivc_svideo_play_film));
            mPlayImage.setBackgroundResource(R.mipmap.video_pre_play);
        } else {
            mPlayImage.setText(getResources().getString(R.string.alivc_svideo_pause_film));
            mPlayImage.setBackgroundResource(R.mipmap.video_pre_play);
        }
    }
    /**
     * 初始化线程池和Handler
     */
    private void initThreadHandler() {
        executorService = ThreadUtil.newDynamicSingleThreadedExecutor(new AlivcEditThread());
    }

    public static class AlivcEditThread implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("AlivcEdit Thread");
            return thread;
        }
    }

    private static final int ADD_TRANSITION = 1;
    private static final int REVERT_TRANSITION = 2;


    @Override
    public void onTabChange() {
        Log.d(TAG, "onTabChange: ");
    }

    protected void playingPause() {
        if (mAliyunIEditor.isPlaying()) {
            mAliyunIEditor.pause();
            switchPlayStateUI(true);
        }
    }

    private void playingResume() {
        if (!mAliyunIEditor.isPlaying()) {
            mAliyunIEditor.play();
            mAliyunIEditor.resume();
            switchPlayStateUI(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mPlayImage && mAliyunIEditor != null) {
            //当在添加特效的时候，关闭该按钮
            if (mUseAnimationFilter){
                return;
            }
            if (mAliyunIEditor.isPlaying()) {
                playingPause();
            } else {
                playingResume();
            }
        }
    }

    /**
     * 点击空白出弹窗消失
     */
    private void hideBottomEditorView() {
        int checkIndex = mTabGroup.getCheckedIndex();
        if (checkIndex == -1) {
            return;
        }
        UIEditorPage page = UIEditorPage.get(checkIndex);
        mViewOperate.hideBottomEditorView(page);
    }

    StringBuilder mDurationText = new StringBuilder(5);

    private String convertDuration2Text(long duration) {
        mDurationText.delete(0, mDurationText.length());
        float relSec = (float)duration / (1000 * 1000);// us -> s
        int min = (int)((relSec % 3600) / 60);
        int sec = 0;
        sec = (int)(relSec % 60);
        if (min >= 10) {
            mDurationText.append(min);
        } else {
            mDurationText.append("0").append(min);
        }
        mDurationText.append(":");
        if (sec >= 10) {
            mDurationText.append(sec);
        } else {
            mDurationText.append("0").append(sec);
        }
        return mDurationText.toString();
    }

    private void copyAssets() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Common.copyAll(getContext(), resCopy);
            }
        });
    }

    public AliyunIEditor getEditor() {
        return this.mAliyunIEditor;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventColorFilterSelected(SelectColorFilter selectColorFilter) {
        EffectInfo effectInfo = selectColorFilter.getEffectInfo();
        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        mAliyunIEditor.applyFilter(effect);
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventFilterTabClick(FilterTabClick ft) {
        //切换到特效的tab需要暂停播放，切换到滤镜的tab需要恢复播放
        if (mAliyunIEditor != null) {
            switch (ft.getPosition()) {
                case FilterTabClick.POSITION_ANIMATION_FILTER:
                    if (mAliyunIEditor.isPlaying()) {
                        playingPause();
                    }
                    break;
                case FilterTabClick.POSITION_COLOR_FILTER:
                    if (!mAliyunIEditor.isPlaying()) {
                        playingResume();
                    }
                    break;
            }
        }
    }

    /**
     * 添加特效后，点击播放，然后再点击撤销，需要切换播放按钮状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAnimationFilterDelete(Integer msg){
        switchPlayStateUI(mAliyunIEditor.isPaused());
    }

    private EditorCallBack mEditorCallback = new EditorCallBack() {
        @Override
        public void onEnd(int state) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (!mUseAnimationFilter){
                        //当正在添加滤镜的时候，不允许重新播放
                        mAliyunIEditor.replay();
                    }else {
                        switchPlayStateUI(true);
                    }
                }
            });
        }

        @Override
        public void onError(final int errorCode) {
            Log.e(TAG, "play error " + errorCode);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    switch (errorCode) {
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_WRONG_STATE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_PROCESS_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_NO_FREE_DISK_SPACE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CREATE_DECODE_GOP_TASK_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_AUDIO_STREAM_DECODER_INIT_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_VIDEO_STREAM_DECODER_INIT_FAILED:

                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_QUEUE_FULL_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_SPS_PPS_NULL:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_H264_PARAM_SET_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_HEVC_PARAM_SET_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_QUEUE_EMPTY_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_DECODER_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_STATE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INPUT:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_NO_BUFFER_AVAILABLE:

                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_DECODE_SPS:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_QUEUE_EMPTY_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_QUEUE_FULL_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_CREATE_DECODER_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_STATE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_INPUT:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_NO_BUFFER_AVAILABLE:
                            showToast = FixedToastUtils.show(getContext(), "错误码是" + errorCode);
                            ((Activity)getContext()).finish();
                            break;
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CACHE_DATA_SIZE_OVERFLOW:
                            showToast = FixedToastUtils.show(getContext(), "错误码是" + errorCode);

                            mAliyunIEditor.play();
                            break;
                        case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                            showToast = FixedToastUtils.show(getContext(),
                                getResources().getString(R.string.not_supported_audio));
                            ((Activity)getContext()).finish();
                            break;
                        case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                            showToast = FixedToastUtils.show(getContext(),
                                getResources().getString(R.string.not_supported_video));
                            ((Activity)getContext()).finish();
                            break;
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_STREAM_NOT_EXISTS:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INTERRUPT:
                        case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_PIXEL_FORMAT:
                            showToast = FixedToastUtils.show(getContext(),
                                getResources().getString(R.string.not_supported_pixel_format));
                            ((Activity)getContext()).finish();
                            break;
                        default:
                            showToast = FixedToastUtils.show(getContext(),
                                getResources().getString(R.string.play_video_error));
                            ((Activity)getContext()).finish();
                            break;
                    }
                }
            });
        }

        @Override
        public int onCustomRender(int srcTextureID, int width, int height) {
            return srcTextureID;
        }

        @Override
        public int onTextureRender(int srcTextureID, int width, int height) {
            return 0;
        }

        @Override
        public void onPlayProgress(final long currentPlayTime, final long currentStreamPlayTime) {

        }

        private int c = 0;

        @Override
        public void onDataReady() {
            post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onDataReady received");
                    if (mWaitForReady && c > 0) {
                        Log.d(TAG, "onDataReady resume");
                        mWaitForReady = false;
                        mAliyunIEditor.resume();
                    }
                    c++;
                }
            });

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEYCODE_VOLUME_DOWN:
                mVolume -= 5;
                if (mVolume < 0) {
                    mVolume = 0;
                }
                Log.d("xxffdd", "volume down, current volume = " + mVolume);
                mAliyunIEditor.setVolume(mVolume);
                return true;
            case KEYCODE_VOLUME_UP:
                mVolume += 5;
                if (mVolume > 100) {
                    mVolume = 100;
                }
                Log.d("xxffdd", "volume up, current volume = " + mVolume);
                mAliyunIEditor.setVolume(mVolume);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private boolean isNeedResume = true;

    public void onStart() {
        mIvRight.setEnabled(true);
        if (mViewStack != null) {
            mViewStack.setVisibleStatus(true);
        }
    }

    public void onResume() {
        if (isNeedResume) {
            playingResume();
        }
    }

    public void onPause() {
        isNeedResume = mAliyunIEditor.isPlaying();
        playingPause();
        mAliyunIEditor.saveEffectToLocal();
    }

    public void onStop() {
        if (mViewStack != null) {
            mViewStack.setVisibleStatus(false);
        }
        if (showToast != null) {
            showToast.cancel();
            showToast = null;
        }
    }

    public void onDestroy() {
        mIsDestroyed = true;
        EventBus.getDefault().unregister(this);

        if (mAliyunIEditor != null) {
            mAliyunIEditor.onDestroy();
        }

        if (mViewOperate != null) {
            mViewOperate.setAnimatorListener(null);
            mViewOperate = null;
        }

        if (executorService != null){
            executorService.shutdownNow();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewStack.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onBackPressed() {
        if (mViewOperate != null) {
            boolean isShow = mViewOperate.isBottomViewShow();
            // 直接隐藏
            if (isShow) {
                if (mViewOperate != null) {
                    mViewOperate.getBottomView().onBackPressed();
                    hideBottomEditorView();
                }
            }
            return isShow;
        } else {
            return false;
        }
    }

    private Uri mUri;

    public void setParam(AliyunVideoParam mVideoParam, Uri mUri, boolean hasTailAnimation) {
        this.mUri = mUri;
        this.mVideoParam = mVideoParam;
        initEditor();

    }

    private AliyunVideoParam mVideoParam;

    public void setTempFilePaths(ArrayList<String> mTempFilePaths) {
        this.mTempFilePaths = mTempFilePaths;
    }

    private ArrayList<String> mTempFilePaths = null;

    /**
     * 播放时间、显示时间同步接口
     */
    public interface PlayerListener {

        //获取当前的播放时间（-->缩略图条位置同步）
        long getCurrDuration();

        //获取视频总时间
        long getDuration();

        //更新时间（-->显示时间同步）
        void updateDuration(long duration);
    }
}

