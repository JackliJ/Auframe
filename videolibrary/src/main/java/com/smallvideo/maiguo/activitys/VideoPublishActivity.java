package com.smallvideo.maiguo.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.aliyun.common.global.AliyunTag;
import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.svideo.sdk.external.struct.common.AliyunVideoParam;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.bumptech.glide.Glide;
import com.maiguoer.utils.FileUtils;
import com.maiguoer.utils.NetWatchdog;
import com.maiguoer.widget.MgeToast;
import com.maiguoer.widget.dialog.CustomDialog;
import com.smallvideo.maiguo.R;
import com.smallvideo.maiguo.aliyun.bean.GoodCallbackEvent;
import com.smallvideo.maiguo.aliyun.util.MediaBitMapUtil;
import com.smallvideo.maiguo.smallvideo.SmallReleaseTagActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 小视频发布页面
 * Create by zxd 2019/2/219
 * <br/>
 */

public class VideoPublishActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 编辑器
     */
    EditText vEditor;
    /**
     * 预览图
     */
    ImageView vMp4Img;
    /**
     * ed输入的长度值
     */
    TextView vTEdTextNum;
    /**
     * 状态栏
     */
    View vStatusBarV;
    /**
     * 视频标签
     */
    TextView vTagTv;
    /**
     * 商品模块布局
     */
    RelativeLayout vGoodLayout;
    /**
     *商品删除
     */
    ImageView vGoodDel;
    /**
     * 确认商品删除
     */
    TextView vGoodConfirmDel;
    /**
     * 商品头像
     */
    ImageView vGoodIcon;
    /**
     * 商品标题
     */
    TextView vGoodTitle;
    /**
     * 商品价格
     */
    TextView vGoodPrice;
    /**
     * 添加商品布局
     */
    LinearLayout vInsertGoodLayout;


    //当前选取或者录制的视屏地址
    private String mMP4Path;
    private Intent intent;
    private Context mContext;
    //上传实体
    private String mImagePath;
    private NetWatchdog netWatchdog;
    private String mMessage;

    //用于取出MP4的地址
    public final static String PREVIEWMP4_PATH = "PREVIEWMP4_PATH";
    //用于判断是从哪个页面进入预览界面的
    public final static String PREVIEW_BOOLEAN = "PREVIEW_BOOLEAN";
    //TAG
    private static final String TAG = "SmaillReleaseActivity_TAG";

    private AliyunVideoParam mVideoParam;

    private String mTag;
    private String mTagID;

    private String mGoodID;

    public static void nativeToVideoPublishActivity(Context contect ,String videoPath){
        Intent intent = new Intent(contect,VideoPublishActivity.class);
        intent.putExtra(PREVIEWMP4_PATH,videoPath);
        contect.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video_layout);
        //订阅
        EventBus.getDefault().register(this);
        initViews();
        mContext = this;
        //初始化下载器
//        vodsVideoUploadClient = new VODSVideoUploadClientImpl(this.getApplicationContext());
//        vodsVideoUploadClient.init();
        //初始化网络监听器
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
               /* if (vodsVideoUploadClient != null) {
                    vodsVideoUploadClient.resume();
                }*/
            }

            @Override
            public void onNetUnConnected() {
//                vodsVideoUploadClient.pause();
            }

        });
        netWatchdog.startWatch();
        //初始化基础数据
        init();
    }

    /**
     * 初始化控件
     */
    private void initViews(){
        //内容输入
        vEditor = findViewById(R.id.et_input);
        //视频缩略图
        vMp4Img = findViewById(R.id.iv_video_thumb);
        //标签
        vTagTv = findViewById(R.id.tv_tag);
        //商品布局
        vGoodLayout = findViewById(R.id.rl_good_layout);
        //商品名
        vGoodTitle = findViewById(R.id.tv_good_title);
        //商品价格
        vGoodPrice = findViewById(R.id.tv_good_price);
        //删除商品
        vGoodDel = findViewById(R.id.iv_img_del);
        //确认删除商品
        vGoodConfirmDel = findViewById(R.id.tv_comfirm_del);
        //添加商品布局
        vInsertGoodLayout = findViewById(R.id.ll_insert_good);


        //设置监听
        vTagTv.setOnClickListener(this);
        vGoodDel.setOnClickListener(this);
        vGoodConfirmDel.setOnClickListener(this);
        vInsertGoodLayout.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    private void init() {
        mVideoParam = new AliyunVideoParam.Builder()
                .gop(5)
                .bitrate(0)
                .frameRate(25)
                .videoQuality(VideoQuality.HD)
                .videoCodec(VideoCodecs.H264_SOFT_OPENH264)
                .build();
        //获取传递数据
        intent = getIntent();
        if (intent != null) {
            mMP4Path = intent.getStringExtra(PREVIEWMP4_PATH);
        }
        //设置文本监听
        vEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文字变化
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //获取长度
                int mEdNum = vEditor.getText().toString().trim().length();
                //设置长度
                vTEdTextNum.setText(mEdNum + "/20");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //根据视频路径截取第一帧预览图保存到本地
        if (!TextUtils.isEmpty(mMP4Path)) {
            MediaBitMapUtil mediaBitMapUtil = new MediaBitMapUtil(mMP4Path);
            Bitmap mBitmap = mediaBitMapUtil.getMp4GOP(1);
            // 首先保存图片
            String fileName = "Mgr_" + System.currentTimeMillis() + ".jpg";
            // 将图片设置到默认地址
            String imageDir = FileUtils.createDir(mContext, FileUtils.images);
            // 拼接图片路径
            String imagePath = imageDir + fileName;
            // 将bitmap保存到本地
            File mFile = new File(imagePath);
            try {
                FileOutputStream fos = new FileOutputStream(mFile);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImagePath = mFile.getPath();
            //设置预览图
            vMp4Img.setImageBitmap(mBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //视频标签返回
        if (requestCode == 1122) {
            if (data != null) {
                mTag = data.getStringExtra("mTag");
                if (!TextUtils.isEmpty(mTag)) {
                    vTagTv.setText(mTag);
                    mTagID = data.getStringExtra("mTagID");
                }
            }
        }
    }

    /**
     * 准备上传
     */
    private void initUpload(final String title, final String filename, int filesize, final String filepath) {
        /*ApiRequestSmallVideo.getInstance().getPrepareVideo(this, new MgeSubscriber<UploadBean>() {
            @Override
            public void onDismissLoading() {
                dismissLoading();
            }
            @Override
            public void onSuccess(UploadBean result) {
                if (result != null) {
                    //开启上传服务
                    SmallUploadListService.startUploadService(mContext, result.getData().getCredentials().getAccessKeyId()
                            , result.getData().getCredentials().getAccessKeySecret(),
                            result.getData().getCredentials().getSecretToken()
                            , result.getData().getCredentials().getExpiration(), filepath, filename, mMessage, mTagID, mGoodID, 1);
                    //跳转到名片页
                    MeCardActivity.navigateToMeCardActivity(SmaillReleaseActivity.this);
                    //发送
                    EventBus.getDefault().post("small_finish");
                    finish();
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                dismissLoading();
            }
        });*/
    }


//    VODSVideoUploadClient vodsVideoUploadClient;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_img_del:
                //删除商品
                vGoodDel.setVisibility(View.GONE);
                vGoodConfirmDel.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_comfirm_del:
                //确认删除商品
                vGoodLayout.setVisibility(View.GONE);
                vInsertGoodLayout.setVisibility(View.VISIBLE);
                //删除保存的商品  todo

                break;
            case R.id.ll_insert_good:
                //插入商品
//                InsertGoodActivity.navigateToInsertGoodActivity(this, 1,new ArrayList<Integer>());
                break;
            case R.id.tv_tag:
                //标签
                Intent in = new Intent(mContext, SmallReleaseTagActivity.class);
                startActivityForResult(in, 1122);
                break;
            case R.id.tv_publish:
                //发布
                if (TextUtils.isEmpty(mTagID)) {
                    MgeToast.showErrorToast(mContext, "please select tag");
                } else {
                    //最少上传一个字符
                    if (vEditor.getText().toString().length() > 0) {
                        //准备上传
                        mMessage = vEditor.getText().toString().trim();
                        //根据文件地址获取文件名称和大小
                        File file = new File(mMP4Path);
                        initUpload(mMessage, file.getName(), (int) file.length(), mMP4Path);
                    } else {
                        MgeToast.showErrorToast(mContext, "please input good descrp");
                    }
                }
                break;
            case R.id.btn_back:
                //返回
                finish();
                break;
        }
    }

    @SuppressLint("LongLogTag")
    @Subscribe(threadMode = ThreadMode.MAIN) //商品选择的回调
    public void onSmallVideoReleaseShop(GoodCallbackEvent event) {
        if (event.getLs().size() > 0) {
            //显示商品布局
            vGoodLayout.setVisibility(View.VISIBLE);
            //隐藏插入商品
            vInsertGoodLayout.setVisibility(View.GONE);
            mGoodID = String.valueOf(event.getLs().get(0).getId());
            //商品图片
            Glide.with(mContext).load(event.getLs().get(0).getImage()).into(vGoodIcon);
            //商品标题
            vGoodTitle.setText(event.getLs().get(0).getGoodsName());
            //商品价格
            vGoodPrice.setText("￥" + event.getLs().get(0).getPrice());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        vodsVideoUploadClient.resume();
     /*   setTranslucent(this);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
//        vodsVideoUploadClient.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除订阅
        EventBus.getDefault().unregister(this);
//        vodsVideoUploadClient.release();
        netWatchdog.stopWatch();
    }
}
