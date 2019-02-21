package com.smallvideo.maiguo.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aliyun.common.utils.MySystemParams;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.svideo.sdk.external.struct.common.AliyunDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoClip;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.maiguoer.widget.dialog.CustomDialog;
import com.smallvideo.maiguo.MainActivity;
import com.smallvideo.maiguo.R;
import com.smallvideo.maiguo.aliyun.bean.ActionInfo;
import com.smallvideo.maiguo.aliyun.bean.AliyunSvideoActionConfig;
import com.smallvideo.maiguo.aliyun.edit.AlivcEditorRoute;
import com.smallvideo.maiguo.aliyun.media.AlivcSvideoEditParam;
import com.smallvideo.maiguo.aliyun.media.MediaActivity;
import com.smallvideo.maiguo.aliyun.media.MediaInfo;
import com.smallvideo.maiguo.aliyun.util.Common;
import com.smallvideo.maiguo.aliyun.util.FixedToastUtils;
import com.smallvideo.maiguo.aliyun.util.NotchScreenUtil;
import com.smallvideo.maiguo.aliyun.util.PermissionUtils;
import com.smallvideo.maiguo.aliyun.util.voice.PhoneStateManger;
import com.smallvideo.maiguo.aliyun.view.AliyunSVideoRecordView;
import com.smallvideo.maiguo.aliyun.widget.ProgressDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AlivcSvideoRecordActivity extends AppCompatActivity {
    public static final String NEED_GALLERY = "need_gallery";

    private AliyunSVideoRecordView videoRecordView;

    /*拍摄视频宽度*/
    private int mResolutionMode = AliyunSnapVideoParam.RESOLUTION_720P;
    /*最小时长*/
    private int mMinDuration = 5 * 1000;
    /*最大时长*/
    private int mMaxDuration = 30 * 1000;
    /*视频关键贞*/
    private int mGop = 250;
    /*码率*/
    private int mBitrate = 0;
    /*视频质量*/
    private VideoQuality mVideoQuality = VideoQuality.HD;
    /*视频编码方式*/
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;
    /*视频比例*/
    private int mRatioMode = AliyunSnapVideoParam.RATIO_MODE_9_16;
    private int mSortMode = AliyunSnapVideoParam.SORT_MODE_MERGE;
    private boolean isNeedClip = true;
    private boolean isNeedGallery;
    private AliyunVideoParam mVideoParam;
    private int mFrame = 30;
    private VideoDisplayMode mCropMode = VideoDisplayMode.SCALE;
    private static final int REQUEST_CODE_PLAY = 2002;
    /*视频扫描刷新*/
    private MediaScannerConnection msc;
    /**
     * 判断是否电话状态
     * true: 响铃, 通话
     * false: 挂断
     */
    private boolean isCalling = false;
    /**
     * 权限申请
     */
    String[] permission = {
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Toast phoningToast;
    private PhoneStateManger phoneStateManger;
    /*判断是编辑模块进入还是通过社区模块的编辑功能进入*/
    private static final String INTENT_PARAM_KEY_ENTRANCE = "entrance";
    /**
     *  判断是编辑模块进入还是通过社区模块的编辑功能进入
     *  svideo: 短视频
     *  community: 社区
     */
    private String entrance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //乐视x820手机在AndroidManifest中设置横竖屏无效，并且只在该activity无效其他activity有效
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MySystemParams.getInstance().init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        // 检测是否是全面屏手机, 如果不是, 设置FullScreen
        if (!NotchScreenUtil.checkNotchScreen(this)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initAssetPath();
        copyAssets();
        setContentView(R.layout.activity_alivc_svideo_record);

        getData();

        boolean checkResult = PermissionUtils.checkPermissionsGroup(this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
        }
        //初始化视频扫描
        msc = new MediaScannerConnection(this, null);
        msc.connect();

        videoRecordView = findViewById(R.id.testRecordView);
        videoRecordView.setActivity(this);
        videoRecordView.setGop(mGop);
        videoRecordView.setBitrate(mBitrate);
        videoRecordView.setMaxRecordTime(mMaxDuration);
        videoRecordView.setMinRecordTime(mMinDuration);
        videoRecordView.setRatioMode(mRatioMode);
        videoRecordView.setVideoQuality(mVideoQuality);
        videoRecordView.setResolutionMode(mResolutionMode);
        videoRecordView.setVideoCodec(mVideoCodec);

    }

    private String[] mEffDirs;
    private AsyncTask<Void, Void, Void> copyAssetsTask;
    private AsyncTask<Void, Void, Void> initAssetPath;

    private void initAssetPath() {
        initAssetPath = new AssetPathInitTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public static class AssetPathInitTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<AlivcSvideoRecordActivity> weakReference;

        AssetPathInitTask(AlivcSvideoRecordActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AlivcSvideoRecordActivity activity = weakReference.get();
            if (activity != null) {
                activity.setAssetPath();
            }
            return null;
        }
    }

    private void setAssetPath() {
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator + Common.QU_NAME
            + File.separator;
        File filter = new File(new File(path), "filter");
        String[] list = filter.list();
        if (list == null || list.length == 0) {
            return;
        }
        mEffDirs = new String[list.length + 1];
        mEffDirs[0] = null;
        int length = list.length;
        for (int i = 0; i < length; i++) {
            mEffDirs[i + 1] = filter.getPath() + File.separator + list[i];
        }
    }

    private void copyAssets() {
        copyAssetsTask = new CopyAssetsTask(this).executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class CopyAssetsTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<AlivcSvideoRecordActivity> weakReference;
        ProgressDialog progressBar;
        CopyAssetsTask(AlivcSvideoRecordActivity activity) {
            weakReference = new WeakReference<>(activity);
            progressBar = new ProgressDialog(activity);
            progressBar.setMessage("资源拷贝中....");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setCancelable(false);
            progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AlivcSvideoRecordActivity activity = weakReference.get();
            if (activity != null) {
                Common.copyAll(activity);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.dismiss();
        }
    }


    /**
     * 获取上个页面的传参
     */
    private void getData() {
        entrance = getIntent().getStringExtra(INTENT_PARAM_KEY_ENTRANCE);
        /**
         * 帧率裁剪参数,默认30
         */
        mVideoParam = new AliyunVideoParam.Builder()
            .gop(mGop)
            .bitrate(mBitrate)
            .crf(0)
            .frameRate(mFrame)
            .outputWidth(getVideoWidth())
            .outputHeight(getVideoHeight())
            .videoQuality(mVideoQuality)
            .videoCodec(mVideoCodec)
            .build();

        mCropMode = (VideoDisplayMode) getIntent().getSerializableExtra(AliyunSnapVideoParam.CROP_MODE);
        if(mCropMode == null){
            mCropMode = VideoDisplayMode.SCALE;
        }
        mSortMode = getIntent().getIntExtra(AliyunSnapVideoParam.SORT_MODE, AliyunSnapVideoParam.SORT_MODE_MERGE);
    }
    /**
     * 获取拍摄视频宽度
     * @return
     */
    private int getVideoWidth(){
        int width = 0;
        switch (mResolutionMode) {
            case AliyunSnapVideoParam.RESOLUTION_360P:
                width = 360;
                break;
            case AliyunSnapVideoParam.RESOLUTION_480P:
                width = 480;
                break;
            case AliyunSnapVideoParam.RESOLUTION_540P:
                width = 540;
                break;
            case AliyunSnapVideoParam.RESOLUTION_720P:
                width = 720;
                break;
            default:
                width = 540;
                break;
        }
        return width;
    }

    private int getVideoHeight(){
        int width = getVideoWidth();
        int height = 0;
        switch (mRatioMode) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                height = width;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                height = width * 4 / 3;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                height = width * 16 / 9;
                break;
            default:
                height = width;
                break;
        }
        return height;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPhoneStateManger();
    }

    private void initPhoneStateManger() {
        if (phoneStateManger == null) {
            phoneStateManger = new PhoneStateManger(this);
            phoneStateManger.registPhoneStateListener();
            phoneStateManger.setOnPhoneStateChangeListener(new PhoneStateManger.OnPhoneStateChangeListener() {

                @Override
                public void stateIdel() {
                    // 挂断
                    videoRecordView.setRecordMute(false);
                    isCalling = false;
                }

                @Override
                public void stateOff() {
                    // 接听
                    videoRecordView.setRecordMute(true);
                    isCalling = true;
                }

                @Override
                public void stateRinging() {
                    // 响铃
                    videoRecordView.setRecordMute(true);
                    isCalling = true;
                }
            });
        }
    }

    /**
     * 弹出确认退出框
     */
    private void showExitDialog(){
        //弹出退出确认按钮
        CustomDialog vCustomViewDialognew = new CustomDialog.Builder(this, CustomDialog.MODE_MESSAGE)
                .setTitle(getResources().getString(R.string.video_record_give_up))
                .setMessage(getResources().getString(R.string.video_record_exit_tip))
                .setSingleLineMsg(true)
                .setConfirm(getResources().getString(R.string.video_give_up), new CustomDialog.DlgCallback() {
                    @Override
                    public void onClick(CustomDialog dialog) {
                        dialog.dismiss();
                        //删除所有临时文件
                        videoRecordView.deleteAllPart();
                        //放回上一个页面
                        finish();
                    }
                })
                .setCancel(getResources().getString(R.string.video_cancel), new CustomDialog.DlgCallback() {
                    @Override
                    public void onClick(CustomDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        vCustomViewDialognew.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoRecordView.startPreview();
        //选择本地视频
        videoRecordView.setOnSelectLocalListener(new AliyunSVideoRecordView.OnSelectLocalListener() {
            @Override
            public void onSelectLocal() {
                startActivity(new Intent(AlivcSvideoRecordActivity.this, MediaActivity.class));
            }
        });
        //返回
        videoRecordView.setBackClickListener(new AliyunSVideoRecordView.OnBackClickListener() {
            @Override
            public void onClick() {
                showExitDialog();
            }
        });
        //设置录制完成回调
        videoRecordView.setCompleteListener(new AliyunSVideoRecordView.OnFinishListener() {
            @Override
            public void onComplete(String path,int duration) {
                //刷新刚录制的视频
                scanFile(path);
                AliyunIImport mImport= AliyunImportCreator.getImportInstance(AlivcSvideoRecordActivity.this);
                mImport.setVideoParam(mVideoParam);
                mImport.addMediaClip(new AliyunVideoClip.Builder()
                    .source(path)
                    .startTime(0)
                    .endTime(duration)
                    .displayMode(AliyunDisplayMode.DEFAULT)
                    .build());
                String projectJsonPath = mImport.generateProjectConfigure();
                //获取录制完成的配置页面
//                String tagClassName = action.getTagClassName(ActionInfo.SVideoAction.RECORD_TARGET_CLASSNAME);
//                intent.setClassName(AlivcSvideoRecordActivity.this,tagClassName);
//                intent.setClass(AlivcSvideoRecordActivity.this, MainActivity.class);
//                intent.putExtra("video_param", mVideoParam);
//                intent.putExtra("project_json_path", projectJsonPath);
//                intent.putExtra(INTENT_PARAM_KEY_ENTRANCE, entrance);
//                startActivity(intent);
                //跳编辑预览界面
                ArrayList<MediaInfo> resultVideos = new ArrayList<>();
                MediaInfo info = new MediaInfo();
                info.filePath = path;
                resultVideos.add(info);
                AlivcSvideoEditParam mSvideoParam = new AlivcSvideoEditParam.Build()
                        .setRatio(AlivcSvideoEditParam.RATIO_MODE_9_16)
                        .setResolutionMode(mResolutionMode)
                        .setEntrance(entrance)
                        .setBitrate(mBitrate)
                        .build();
                AlivcEditorRoute.startEditorActivity(AlivcSvideoRecordActivity.this, mSvideoParam,resultVideos,projectJsonPath);
            }
        });
    }

    private void scanFile(String path) {
        if (msc != null && msc.isConnected()) {
            msc.scanFile(path, "video/mp4");
        }
    }

    @Override
    protected void onPause() {
        videoRecordView.stopPreview();
        super.onPause();
        if (phoningToast != null) {
            phoningToast.cancel();
            phoningToast = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (phoneStateManger != null) {
            phoneStateManger.setOnPhoneStateChangeListener(null);
            phoneStateManger.unRegistPhoneStateListener();
            phoneStateManger = null;
        }
    }

    @Override
    protected void onDestroy() {
        videoRecordView.destroyRecorder();
        super.onDestroy();
        if (copyAssetsTask != null) {
            copyAssetsTask.cancel(true);
            copyAssetsTask = null;
        }

        if (initAssetPath != null) {
            initAssetPath.cancel(true);
            initAssetPath = null;
        }
        //释放媒体扫描库
        if(msc != null){
            msc.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PLAY) {
            if (resultCode == Activity.RESULT_OK) {
                videoRecordView.deleteAllPart();
                finish();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isCalling) {
            phoningToast = FixedToastUtils.show(this, getResources().getString(R.string.alivc_phone_state_calling));
        }
    }

    /**
     * 开启录制
     * @param context Context
     * @param param AliyunSnapVideoParam
     */
    public static void startRecord(Context context, AliyunSnapVideoParam param){
        startRecord(context, param, "");
    }

    /**
     * 开启录制
     * @param context Context
     * @param param AliyunSnapVideoParam
     * @param entrance 模块入口方式
     */
    public static void startRecord(Context context, AliyunSnapVideoParam param, String entrance){
        Intent intent = new Intent(context,AlivcSvideoRecordActivity.class);
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION,param.getResolutionMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO,param.getRatioMode());
        intent.putExtra(AliyunSnapVideoParam.RECORD_MODE,param.getRecordMode());
        intent.putExtra(AliyunSnapVideoParam.FILTER_LIST,param.getFilterList());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_LEVEL,param.getBeautyLevel());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_STATUS,param.getBeautyStatus());
        intent.putExtra(AliyunSnapVideoParam.CAMERA_TYPE, param.getCameraType());
        intent.putExtra(AliyunSnapVideoParam.FLASH_TYPE, param.getFlashType());
        intent.putExtra(AliyunSnapVideoParam.NEED_CLIP,param.isNeedClip());
        intent.putExtra(AliyunSnapVideoParam.MAX_DURATION,param.getMaxDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_DURATION,param.getMinDuration());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY,param.getVideoQuality());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP,param.getGop());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_BITRATE, param.getVideoBitrate());
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE,param.getSortMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_CODEC, param.getVideoCodec());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE,param.getFrameRate());
        intent.putExtra(AliyunSnapVideoParam.CROP_MODE, param.getScaleMode());
        intent.putExtra(AliyunSnapVideoParam.MIN_CROP_DURATION,param.getMinCropDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION,param.getMinVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION,param.getMaxVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE, param.getSortMode());
        intent.putExtra(INTENT_PARAM_KEY_ENTRANCE, entrance);
        context.startActivity(intent);
    }
    public static final int PERMISSION_REQUEST_CODE = 1000;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了
                //Toast.makeText(this, "get All Permisison", Toast.LENGTH_SHORT).show();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                showPermissionDialog();
            }
        }
    }
    //系统授权设置的弹框
    AlertDialog openAppDetDialog = null;
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.app_name) + "需要访问 \"相册\"、\"摄像头\" 和 \"外部存储器\",否则会影响绝大部分功能使用, 请到 \"应用信息 -> 权限\" 中设置！");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("暂不设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        if (null == openAppDetDialog) {
            openAppDetDialog = builder.create();
        }
        if (null != openAppDetDialog && !openAppDetDialog.isShowing()) {
            openAppDetDialog.show();
        }
    }
}
