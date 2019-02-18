/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.smallvideo.maiguo.aliyun.media;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aliyun.common.utils.ToastUtil;
import com.aliyun.jasonparse.JSONSupportImpl;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.duanqu.transcode.NativeParser;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;
import com.smallvideo.maiguo.R;
import com.smallvideo.maiguo.aliyun.edit.AlivcEditorRoute;
import com.smallvideo.maiguo.aliyun.util.FixedToastUtils;
import com.smallvideo.maiguo.aliyun.widget.ProgressDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑模块的media选择Activity
 */
public class MediaActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE_VIDEO_CROP = 1;
    private static final int REQUEST_CODE_IMAGE_CROP = 2;
    private static final int IMAGE_DURATION = 3000;//图片代表的时长


    private MediaStorage storage;
    private ProgressDialog progressDialog;
    private ThumbnailGenerator thumbnailGenerator;
    private GalleryMediaChooser galleryMediaChooser;
    //取消
    private TextView vBack;
    //确定
    private TextView vOk;
    private TextView vTitle;

    private Transcoder mTransCoder;
    private MediaInfo mCurrMediaInfo;
    private int mCropPosition;
    private boolean mIsReachedMaxDuration = false;

    private int requestWidth;
    private int requestHeight;
    //本地选择的信息
    List<MediaInfo> resultVideos = new ArrayList<>();

    private AlivcSvideoEditParam mSvideoParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_svideo_import_activity_media);
        getData();
        init();
    }

    private void getData() {
        int mRatio = getIntent().getIntExtra(AlivcSvideoEditParam.VIDEO_RATIO, AlivcSvideoEditParam.RATIO_MODE_9_16);
        int mResolutionMode = getIntent().getIntExtra(AlivcSvideoEditParam.VIDEO_RESOLUTION, AlivcSvideoEditParam.RESOLUTION_720P);
        boolean hasTailAnimation = getIntent().getBooleanExtra(AlivcSvideoEditParam.TAIL_ANIMATION, false);
        String entrance = getIntent().getStringExtra(AlivcSvideoEditParam.INTENT_PARAM_KEY_ENTRANCE);
        VideoDisplayMode scaleMode = (VideoDisplayMode)getIntent().getSerializableExtra(AlivcSvideoEditParam.VIDEO_CROP_MODE);
        if (scaleMode == null) {
            scaleMode = VideoDisplayMode.FILL;
        }
        int frameRate = getIntent().getIntExtra(AlivcSvideoEditParam.VIDEO_FRAMERATE, 25);
        int gop = getIntent().getIntExtra(AlivcSvideoEditParam.VIDEO_GOP, 125);
        int mBitrate = getIntent().getIntExtra(AlivcSvideoEditParam.VIDEO_BITRATE, 0);
        VideoQuality quality = (VideoQuality)getIntent().getSerializableExtra(AlivcSvideoEditParam.VIDEO_QUALITY);
        VideoCodecs videoCodecs = (VideoCodecs) getIntent().getSerializableExtra(AlivcSvideoEditParam.VIDEO_CODEC);
        if (quality == null) {
            quality = VideoQuality.SSD;
        }
        if(videoCodecs == null){
            videoCodecs = VideoCodecs.H264_HARDWARE;
        }
        mSvideoParam = new AlivcSvideoEditParam.Build()
            .setRatio(mRatio)
            .setResolutionMode(mResolutionMode)
            .setHasTailAnimation(hasTailAnimation)
            .setEntrance(entrance)
            .setCropMode(scaleMode)
            .setFrameRate(frameRate)
            .setGop(gop)
            .setBitrate(mBitrate)
            .setVideoQuality(quality)
            .setVideoCodec(videoCodecs)
            .build();
    }

    private void init() {
        mTransCoder = new Transcoder();
        mTransCoder.init(this);
        mTransCoder.setTransCallback(new Transcoder.TransCallback() {
            @Override
            public void onError(Throwable e, final int errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        switch (errorCode) {
                            case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                                ToastUtil.showToast(MediaActivity.this, R.string.aliyun_not_supported_audio);
                                break;
                            case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                                ToastUtil.showToast(MediaActivity.this, R.string.aliyun_video_crop_error);
                                break;
                            case AliyunErrorCode.ERROR_UNKNOWN:
                            default:
                                ToastUtil.showToast(MediaActivity.this, R.string.aliyun_video_error);
                        }
                    }
                });

            }
            @Override
            public void onProgress(int progress) {
                if (progressDialog != null) {
                    progressDialog.setProgress(progress);
                }
            }
            @Override
            public void onComplete(List<MediaInfo> resultVideos) {
                Log.d("TRANCODE", "ONCOMPLETED, dialog : " + (progressDialog == null));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                //跳转EditorActivity
//                AlivcEditorRoute.startEditorActivity(MediaActivity.this, mSvideoParam, (ArrayList<MediaInfo>) resultVideos);
            }

            @Override
            public void onCancelComplete() {
                //取消完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        RecyclerView galleryRecycler = (RecyclerView) findViewById(R.id.gallery_media);
        vTitle = (TextView)findViewById(R.id.gallery_title);
        vTitle.setText(R.string.video_select);
        //取消
        vBack = findViewById(R.id.tv_cancel);
        //确定
        vOk = findViewById(R.id.tv_ok);
        vBack.setOnClickListener(this);
        vOk.setOnClickListener(this);
        storage = new MediaStorage(this, new JSONSupportImpl());
        thumbnailGenerator = new ThumbnailGenerator(this);
        GalleryDirChooser galleryDirChooser = new GalleryDirChooser(this, findViewById(R.id.topPanel),thumbnailGenerator, storage);
        galleryMediaChooser = new GalleryMediaChooser(galleryRecycler, galleryDirChooser, storage, thumbnailGenerator,vOk);

        //设置查询类型
        storage.setSortMode(MediaStorage.SORT_MODE_VIDEO);
        try {
            storage.startFetchmedias();
        } catch (SecurityException e) {
            FixedToastUtils.show(MediaActivity.this, "没有权限");
        }
        //点击标题切换视频路径时监听
        storage.setOnMediaDirChangeListener(new MediaStorage.OnMediaDirChange() {
            @Override
            public void onMediaDirChanged() {
                MediaDir dir = storage.getCurrentDir();
                if (dir.id == -1) {
                    vTitle.setText(getString(R.string.gallery_all_media));
                } else {
                    vTitle.setText(dir.dirName);
                }
                galleryMediaChooser.changeMediaDir(dir);
            }
        });
        //当前选择的视频信息
        storage.setOnCurrentMediaInfoChangeListener(new MediaStorage.OnCurrentMediaInfoChange() {
            @Override
            public void onCurrentMediaInfoChanged(MediaInfo info) {
                MediaInfo infoCopy = new MediaInfo();
                infoCopy.addTime = info.addTime;
                infoCopy.mimeType = info.mimeType;
                String outputPath = null;
                if (info.mimeType.startsWith("image")) {
                    if (info.filePath.endsWith("gif")||info.filePath.endsWith("GIF")) {
                        NativeParser parser = new NativeParser();
                        parser.init(info.filePath);
                        int frameCount = Integer.parseInt(parser.getValue(NativeParser.VIDEO_FRAME_COUNT));
                        //当gif动图为一帧的时候当作图片处理，否则当作视频处理
                        if (frameCount>1){
                            infoCopy.mimeType="video";
                            infoCopy.duration = Integer.parseInt(parser.getValue(NativeParser
                                .VIDEO_DURATION)) / 1000;
                        }else {
                            infoCopy.duration = IMAGE_DURATION;
                        }

                    } else {
                        infoCopy.duration = IMAGE_DURATION;
                    }

                } else {
                    infoCopy.duration = info.duration;
                }
                if (outputPath != null) {
                    infoCopy.filePath = outputPath;//info.filePath;
                } else {
                    infoCopy.filePath = info.filePath;
                }
                infoCopy.id = info.id;
                infoCopy.isSquare = info.isSquare;
                infoCopy.thumbnailPath = info.thumbnailPath;
                infoCopy.title = info.title;
                infoCopy.type = info.type;
               /* //第一次添加并且分辨率为原比例的时候，计算视频真实宽高
                if (mSelectedVideoAdapter.getItemCount() == 0 ) {
                    requestWidth = mSvideoParam.getVideoWidth();
                    requestHeight = mSvideoParam.getVideoHeight(info);
                }
                mSelectedVideoAdapter.addMedia(infoCopy);*/
                //                mImport.addVideo(infoCopy.filePath, 3000, AliyunDisplayMode.DEFAULT);    //导入器中添加视频
                mTransCoder.addMedia(infoCopy);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
            switch (requestCode) {
                case REQUEST_CODE_VIDEO_CROP:
                    long duration = data.getLongExtra(CropKey.RESULT_KEY_DURATION, 0);
                    long startTime = data.getLongExtra(CropKey.RESULT_KEY_START_TIME, 0);
                    if (!TextUtils.isEmpty(path) && duration > 0 && mCurrMediaInfo != null) {
                        int index = mTransCoder.removeMedia(mCurrMediaInfo);
                        mCurrMediaInfo.filePath = path;
                        mCurrMediaInfo.startTime = startTime;
                        mCurrMediaInfo.duration = (int)duration;
                        mTransCoder.addMedia(index, mCurrMediaInfo);
                    }
                    break;
                case REQUEST_CODE_IMAGE_CROP:
                    if (!TextUtils.isEmpty(path) && mCurrMediaInfo != null) {
                        int index = mTransCoder.removeMedia(mCurrMediaInfo);
                        mCurrMediaInfo.filePath = path;
                        mTransCoder.addMedia(index, mCurrMediaInfo);
                    }
                    break;
            }
        }
    }
    private String convertDuration2Text(long duration) {
        int sec = Math.round(((float)duration) / 1000);
        int hour = sec / 3600;
        int min = (sec % 3600) / 60;
        sec = (sec % 60);
        return String.format(getString(R.string.video_duration),
            hour,
            min,
            sec);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage.saveCurrentDirToCache();
        storage.cancelTask();
        mTransCoder.release();
        thumbnailGenerator.cancelAllTask();
    }

    @Override
    public void onClick(View v) {
        if (v == vBack) {
            //取消
//            finish();
            resultVideos.add(storage.getCurrentMedia());
            AlivcEditorRoute.startEditorActivity(MediaActivity.this, mSvideoParam, (ArrayList<MediaInfo>) resultVideos,null);
        } else if(v == vOk){
            //确定
            resultVideos.add(storage.getCurrentMedia());
            AlivcEditorRoute.startEditorActivity(MediaActivity.this, mSvideoParam, (ArrayList<MediaInfo>) resultVideos,null);
        }
    }

    /**
     * progressDialog cancel listener
     */
    private static class OnCancelListener implements DialogInterface.OnCancelListener {

        private WeakReference<MediaActivity> weakReference;

        public OnCancelListener(MediaActivity mediaActivity){
            weakReference = new WeakReference<>(mediaActivity);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            MediaActivity mediaActivity = weakReference.get();
            if(mediaActivity != null){
                mediaActivity.mTransCoder.cancel();
            }
        }
    }

    public static void startImport(Context context,AlivcSvideoEditParam param){
        Intent intent = new Intent(context,MediaActivity.class);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_BITRATE, param.getBitrate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_FRAMERATE, param.getFrameRate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_GOP, param.getGop());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RATIO, param.getRatio());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_QUALITY, param.getVideoQuality());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RESOLUTION, param.getResolutionMode());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_CROP_MODE, param.getCropMode());
        intent.putExtra(AlivcSvideoEditParam.TAIL_ANIMATION, param.isHasTailAnimation());
        intent.putExtra(AlivcSvideoEditParam.INTENT_PARAM_KEY_ENTRANCE,"svideo" );
        intent.putExtra(AlivcSvideoEditParam.VIDEO_CODEC,param.getVideoCodec() );
        context.startActivity(intent);
    }
}
