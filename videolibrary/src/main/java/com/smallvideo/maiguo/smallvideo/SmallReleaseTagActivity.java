package com.smallvideo.maiguo.smallvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * Create by zxd 2019/02/20
 * <br/>
 * 视频标签页
 */

public class SmallReleaseTagActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 流式布局标签
     */
    TagFlowLayout vFlowLyout;
    /**
     * 初始化标签数据
     */
    private int isFlow = -1;
    /**
     * 状态栏
     */
    View vStatusBarV;

    private Context mContext;
    private String mTag;
    private String mTagID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.small_publish_tag_layout);
        mContext = this;
        //初始化标签数据
        initFlow();
    }

    /**
     * 初始化标签数据
     */
    private void initFlow() {
        //必需。标签类型：0：文章；5：视频；
        int type = getIntent().getIntExtra("type",0);
        /*ApiRequestSmallVideo.getInstance().getZoneTagList(mContext,type, new MgeSubscriber<TagBean>() {
            @Override
            public void onDismissLoading() {

            }

            @Override
            public void onSuccess(final TagBean result) {
                if (result != null) {
                    final LayoutInflater mInflater = LayoutInflater.from(mContext);
                    vFlowLyout.setAdapter(new TagAdapter<TagBean.TagListData.TagList>(result.getData().getTagList()) {

                        @Override
                        public View getView(FlowLayout parent, int position, TagBean.TagListData.TagList s) {
                            TextView tv = (TextView) mInflater.inflate(R.layout.small_release_tv,
                                    vFlowLyout, false);
                            tv.setText(s.getName());
                            return tv;
                        }
                    });
                    vFlowLyout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            if (isFlow == position) {
                                mTag = "";
                            } else {
                                mTag = String.valueOf(result.getData().getTagList().get(position).getName());
                                mTagID = String.valueOf(result.getData().getTagList().get(position).getId());
                            }
                            isFlow = position;
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                MgeToast.showErrorToast(SmallReleaseTagActivity.this,msg);
            }
        });*/
    }

    @Override
    public void onClick(View v) {

    }

 /*   @Override
    protected void onResume() {
        super.onResume();
        setTranslucent(this);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
    }*/
}
