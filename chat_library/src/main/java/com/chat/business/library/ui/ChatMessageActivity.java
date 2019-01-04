package com.chat.business.library.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.chat.business.library.R;
import com.chat.business.library.model.ChatVoiceEMMessage;
import com.chat.business.library.model.PreviewBean;
import com.chat.business.library.ui.fragment.EmotionMainFragment;
import com.hyphenate.chat.EMMessage;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;
import com.maiguoer.component.http.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 单聊
 * Create by www.lijin@foxmail.com on 2019/1/3 0003.
 * <br/>
 */
public class ChatMessageActivity extends BaseSwipeBackActivity implements  EmotionMainFragment.FragmentListener,SwipeRefreshLayout.OnRefreshListener {

    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout vSwipeRefresh;
    /**
     * message的父级 用于获取上级ID
     */
    private LinearLayout vFather;

    // 对方用户昵称 username 对方用户头像 avatar  对方用户实名认证状态 authStatus 对方用户企业认证状态 businessAuthStatus
    private String mOtherUid, mOtherUsername, mOtherUserAvatar,
            mOtherAuthStatus, mOtherBusinessAuthStatus,
            mOtherNamgeCardBgImage;
    //对方用户VIP等级 vipLevel
    private String mOtherVipLevel;
    //传递进来的环信ID  当前人的环信ID
    private String mHXid;
    //全局上下文
    private Context mContext;
    //底部框 包含输入区域和表情区域
    private EmotionMainFragment mEmotionMainFragment;
    //用于注册关闭的Filter
    private IntentFilter mFilter;
    //用于全局接收的Intent
    private Intent mIn;
    //展示数据的Adapter
    private ChatMessageAdapter mAdapter;
    //语音播放器
    MediaPlayer player = new MediaPlayer();
    //存储播放大图的零时集合
    ArrayList<PreviewBean> mDList;
    //存储大图路径的几个
    ArrayList<String> mPathList;
    //大图的实体
    PreviewBean previewBean;
    //用于保存需要传递到大图的下标
    int mPicPosition = 0;
    //RecyclerView 的 LinearLayoutManager
    LinearLayoutManager linearLayoutManager;
    //用于保存未读语音的数组
    List<ChatVoiceEMMessage> mVoiceBody = new ArrayList<>();
    //用于保存未读语音的动画View
    List<View> mVoiceView = new ArrayList<>();
    //自增的播放下标
    int mVoicePosition = 0;
    //用于保存未读语音的实体
    ChatVoiceEMMessage chatVoiceEMMessage;
    //用于全局保存语音是否在播放的状态
    boolean mIsVoiceStart = false;
    //保存当前播放到的view
    View mFastView;
    //有多少条未读
    private int mMarkingContent;
    List<EMMessage> messages;
    //设置一页展示的数据
    int PageSize = 20;
    //用于判断定位的方式
    int mPageSize = 10;
    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";
    String SYSTEM_HOME_KEY_LONG = "recentapps";
    //TAG
    private final static String TAG = "ChatMessageActivity_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_sing_layout);
        //赋值上下文
        mContext = this;
        //组件赋值
        initView();
        //数据查询与赋值
        initData();
    }

    private void initView() {
        vFather = (LinearLayout) findViewById(R.id.chat_ry_father);
        vSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.chat_message_chat_refresh);
        //初始化刷新
        //为SwipeRefreshLayout设置监听事件
        vSwipeRefresh.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        vSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 初始化布局数据
     */
    private void initData() {
        //初始化表情列表
        initEmotionMainFragment();
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_HXID, mHXid);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHRTUID, mOtherUid);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERNAME, mOtherUsername);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERAVATAR, mOtherUserAvatar);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHERAURHSTATUS, mOtherAuthStatus);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHERABUSINESSAU, mOtherBusinessAuthStatus);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHERNAMGECARSBGIMAGE, mOtherNamgeCardBgImage);
        bundle.putString(Constant.MEG_INTNT_CHATMESSAGE_OTHERVIPLEVEL, mOtherVipLevel);
        //替换fragment
        //创建修改实例
        mEmotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        mEmotionMainFragment.bindToContentView(vFather);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_fr, mEmotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    @Override
    public void thank(String text, String UserName) {

    }
}
