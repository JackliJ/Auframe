package com.chat.business.library.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.chat.business.library.R;
import com.chat.business.library.emotionkeyboardview.EmotionKeyboard;
import com.chat.business.library.emotionkeyboardview.NoHorizontalScrollerViewPager;
import com.chat.business.library.model.ImageModel;
import com.chat.business.library.util.EmotionUtils;
import com.chat.business.library.util.GlobalOnItemClickManagerUtils;
import com.maiguoer.component.http.base.BasisFragment;
import com.maiguoer.component.http.utils.Constant;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijin
 * Time  17/09/21 下午3:26
 * Email www.lijin@foxmail.com
 * Description:表情主界面
 */
public class EmotionMainFragment extends BasisFragment implements View.OnClickListener {

    private Context context;
    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT = "bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN = "hide bar's editText and btn";
    public static final String EMOTION_FRAGMRNT_USERNAME = "UserName";
    public static final String EMOTION_FRAGMRNT_HXID = "HXID";
    //显示弹框的主布局
    private LinearLayout facelayout;
    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    //底部水平tab
    private RecyclerView recyclerview_horizontal;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    private EmotionKeyboard mEmotionKeyboard;
    private EditText bar_edit_text;
    private Button bar_btn_send;
    private LinearLayout rl_editbar_bg;
    //需要绑定的内容view
    private View contentView;
    private Bundle args;
    //不可横向滚动的ViewPager
    private NoHorizontalScrollerViewPager viewPager;
    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText = true;
    //是否隐藏bar上的编辑框和发生按钮,默认不隐藏
    private boolean isHidenBarEditTextAndBtn = false;
    List<Fragment> fragments = new ArrayList<>();
    //语音录制按钮
    private Button btn_longvoice;
    //输入选择区域
    private LinearLayout re_top;
    //底部选择框
    private LinearLayout rebotton;
    private String mHuanxinID;
    //加号按钮
    private ImageButton vImgAdd;
    //传递数据的回调
    private FragmentListener listener = null;
    // 对方用户昵称 username 对方用户头像 avatar  对方用户实名认证状态 authStatus 对方用户企业认证状态 businessAuthStatus
    private String mOtherUid, mOtherUsername, mOtherUserAvatar,
            mOtherAuthStatus, mOtherBusinessAuthStatus,
            mOtherNamgeCardBgImage;
    //对方用户VIP等级 vipLevel
    private String mOtherVipLevel;
    //平移动画
    private TranslateAnimation HiddenAmin;
    //缩放动画
    private ScaleAnimation mSShowAnim;
    //加号的旋转动画
    private RotateAnimation mRotaAnimDown;
    private RotateAnimation mRotaAnimOut;
    //是否在删除的时候不加载动画
    private boolean mIsAnim = true;
    //表情布局
    private LinearLayout ll_emotion_layout;
    private GlobalOnItemClickManagerUtils globalOnItemClickManager;
    //开放给fragment
    public static boolean isvoide = false;


    public interface FragmentListener {
        void thank(String text, String UserName);
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (FragmentListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    /**
     * 创建与Fragment对象关联的View视图时调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_sing_button_layout, container, false);
        context = getActivity();
        isHidenBarEditTextAndBtn = args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
        mHuanxinID = args.getString(Constant.MEG_INTNT_CHATMESSAGE_HXID);
        mOtherUsername = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERNAME);
        mOtherUid = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHRTUID);
        mOtherUserAvatar = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERAVATAR);
        mOtherAuthStatus = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHERAURHSTATUS);
        mOtherBusinessAuthStatus = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHERABUSINESSAU);
        mOtherVipLevel = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHERVIPLEVEL);
        mOtherNamgeCardBgImage = args.getString(Constant.MEG_INTNT_CHATMESSAGE_OTHERNAMGECARSBGIMAGE);

        //获取判断绑定对象的参数
        isBindToBarEditText = args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);
        initView(rootView);
        //绑定表情按钮
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(rootView.findViewById(R.id.ll_emotion_layout))
                .bindToContent(contentView)
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) rootView.findViewById(R.id.bar_edit_text)))
                .bindToEmotionButton(rootView.findViewById(R.id.include_chat_emotion), rebotton, re_top, btn_longvoice, vImgAdd)
                .build();
        initDatas();

        //绑定模块布局
        EmotionKeyboard.with(getActivity())
                .setEmotionView(rootView.findViewById(R.id.ll_devoicefrls))
                .bindToContent(contentView)
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) rootView.findViewById(R.id.bar_edit_text)))
                .bindToViewButton(rootView.findViewById(R.id.include_chat_emotion_add), ll_emotion_layout, vImgAdd)
                .build();

        //创建全局监听
        globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());

        if (isBindToBarEditText) {
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(bar_edit_text);

        } else {
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText((EditText) contentView);
            mEmotionKeyboard.bindToEditText((EditText) contentView);
        }
//        String draft = SharedPreferencedUtils.getString(context, mHuanxinID);
        //将键盘右下角设置为发送
        bar_edit_text.setImeOptions(EditorInfo.IME_ACTION_SEND);
        bar_edit_text.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        bar_edit_text.setSingleLine(true);

        return rootView;
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView) {
        viewPager = rootView.findViewById(R.id.vp_emotionview_layout);
        recyclerview_horizontal = rootView.findViewById(R.id.recyclerview_horizontal);
        rebotton = rootView.findViewById(R.id.ll_devoicefrls);
        ll_emotion_layout = rootView.findViewById(R.id.ll_emotion_layout);
        bar_btn_send = rootView.findViewById(R.id.bar_btn_send);
        re_top = rootView.findViewById(R.id.emf_lrm);
        bar_edit_text = rootView.findViewById(R.id.bar_edit_text);
        rl_editbar_bg = rootView.findViewById(R.id.rl_editbar_bg);
        if (isHidenBarEditTextAndBtn) {//隐藏
            bar_edit_text.setVisibility(View.GONE);
            rl_editbar_bg.setBackgroundResource(R.color.b15);
        } else {
            bar_edit_text.setVisibility(View.VISIBLE);
            rl_editbar_bg.setBackgroundResource(R.drawable.shape_bg_reply_edittext);
        }
    }


    /**
     * 数据操作
     */
    protected void initDatas() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.mipmap.chat_icon_exp_tab_default);
                model1.flag = getResources().getString(R.string.chat_classics_emoj);
                model1.isSelected = true;
                list.add(model1);
            } else if (i == 1) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.mipmap.exp_egg_01);
                model1.flag = getResources().getString(R.string.chat_extend_emoj);
                model1.isSelected = false;
                list.add(model1);
            } else if (i == 2) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.mipmap.exp_fh_small_01);
                model1.flag = getResources().getString(R.string.chat_phoenix_emoj);
                model1.isSelected = false;
                list.add(model1);
            } else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.mipmap.ic_plus);
                model.flag = getResources().getString(R.string.chat_other_emoj) + i;
                model.isSelected = false;
                list.add(model);
            }
        }

        //记录底部默认选中第一个
        CurrentPosition = 0;
        SharedPreferencesUtils.setParam(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        recyclerview_horizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = (Integer) SharedPreferencesUtils.getParam(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SharedPreferencesUtils.setParam(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });


    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTTON_AM_TYPE, false);
        fragments.add(f1);
        EmotiomComplateFragment f2 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE, true);
        fragments.add(f2);
        EmotiomComplateFragment f3 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTTON_PHONEIX_TYPE, true);
        fragments.add(f3);
        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        globalOnItemClickManager.detachFromEditText();
    }

}


