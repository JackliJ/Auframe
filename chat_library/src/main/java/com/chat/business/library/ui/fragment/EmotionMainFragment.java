package com.chat.business.library.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.business.library.R;
import com.chat.business.library.emotionkeyboardview.EmotionKeyboard;
import com.chat.business.library.emotionkeyboardview.NoHorizontalScrollerViewPager;
import com.chat.business.library.model.ImageModel;
import com.chat.business.library.util.EmotionUtils;
import com.chat.business.library.util.GlobalOnItemClickManagerUtils;
import com.chat.business.library.util.SoftKeyBoardListener;
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
    private LinearLayout vImgAdd;
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
        //初始化展现动画
        //控件显示的动画 缩放动画
        /* 仿造微信安卓版的效果
                参数解释：
                    第一个参数：X轴水平缩放起始位置的大小（fromX）。1代表正常大小
                    第二个参数：X轴水平缩放完了之后（toX）的大小，0代表完全消失了
                    第三个参数：Y轴垂直缩放起始时的大小（fromY）
                    第四个参数：Y轴垂直缩放结束后的大小（toY）
                    第五个参数：pivotXType为动画在X轴相对于物件位置类型
                    第六个参数：pivotXValue为动画相对于物件的X坐标的开始位置
                    第七个参数：pivotXType为动画在Y轴相对于物件位置类型
                    第八个参数：pivotYValue为动画相对于物件的Y坐标的开始位置

                   （第五个参数，第六个参数），（第七个参数,第八个参数）是用来指定缩放的中心点
                    0.5f代表从中心缩放
        */
        mSShowAnim = new ScaleAnimation(0.5f, 1, 0.5f, 1,
                1, 0.5f, 1, 0.5f);
        mSShowAnim.setDuration(150);


        //控件隐藏的动画 上移动画
        /* 位移动画 模拟将发送按钮发送出去的效果
                    fromXDelta：起始X坐标
                    toXDelta： 结束X坐标
                    fromYDelta：起始Y坐标
                    toYDelta： 结束Y坐标
         */
        HiddenAmin = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        HiddenAmin.setDuration(50);

        /*以view中心点正向旋转45度
                toDegrees：旋转的结束角度。
                pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
                pivotXValue：X坐标的伸缩值。
                pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
                pivotYValue：Y坐标的伸缩值
                 * */
        mRotaAnimDown = new RotateAnimation(0, 45,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotaAnimDown.setDuration(100);
        //动画执行完毕后是否停在结束时的角度上
        mRotaAnimDown.setFillAfter(true);
        //还原动画
        mRotaAnimOut = new RotateAnimation(0, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotaAnimOut.setDuration(100);
        //动画执行完毕后是否停在结束时的角度上
        mRotaAnimOut.setFillAfter(true);
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

        bar_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = bar_edit_text.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    listener.thank(content, mHuanxinID);
                    bar_edit_text.setText("");
                } else {
                    Toast.makeText(context, getResources().getString(R.string.chat_send_editost), Toast.LENGTH_SHORT);
                }
            }
        });
        bar_edit_text.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“SEND”键*/
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String content = bar_edit_text.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        listener.thank(content, mHuanxinID);
                        bar_edit_text.setText("");
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.chat_send_editost), Toast.LENGTH_SHORT);
                    }

                    return true;
                }
                return false;
            }
        });
        bar_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //监听输入 隐藏创建模块
                if (!TextUtils.isEmpty(bar_edit_text.getText().toString().trim())) {
                    bar_btn_send.setBackgroundResource(R.drawable.public_btn_submit_select_click);
                    if (mIsAnim) {
                        bar_btn_send.startAnimation(mSShowAnim);
                        vImgAdd.startAnimation(HiddenAmin);
                        mIsAnim = false;
                    }
                    bar_btn_send.setVisibility(View.VISIBLE);
                    vImgAdd.setVisibility(View.GONE);
                } else {
                    if (!mIsAnim) {
                        bar_btn_send.startAnimation(HiddenAmin);
                        vImgAdd.startAnimation(mSShowAnim);
                        mIsAnim = true;
                    }
                    bar_btn_send.setVisibility(View.GONE);
                    vImgAdd.setVisibility(View.VISIBLE);
                }

            }
        });
        //监听软键盘弹起的类
        SoftKeyBoardListener.setListener((Activity) context, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {//键盘打开
                if (rebotton != null) {
                    rebotton.setVisibility(View.GONE);
                }
                if (ll_emotion_layout != null) {
                    ll_emotion_layout.setVisibility(View.GONE);
                }
                if (vImgAdd != null) {
                    if (bar_edit_text.getText().toString().trim().length() == 0) {
                        vImgAdd.startAnimation(mRotaAnimOut);
                    }
                }
            }

            @Override
            public void keyBoardHide(int height) {//键盘收起

            }
        });
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
        vImgAdd = rootView.findViewById(R.id.include_chat_emotion_add);
        rebotton = rootView.findViewById(R.id.ll_devoicefrls);
        ll_emotion_layout = rootView.findViewById(R.id.ll_emotion_layout);
        bar_btn_send = rootView.findViewById(R.id.bar_btn_send);
        re_top = rootView.findViewById(R.id.emf_lrm);
        bar_edit_text = rootView.findViewById(R.id.bar_edit_text);
        rl_editbar_bg = rootView.findViewById(R.id.rl_editbar_bg);

        bar_edit_text.setOnClickListener(this);
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

    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }

    /**
     * 表情布局没隐藏的时候隐藏它
     */
    public void isShowInterceptBackPress() {
        mEmotionKeyboard.interceptback();
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

    boolean mIsEditext = true;

    @Override
    public void onClick(View v) {
        //加好
        if (v.getId() == R.id.include_chat_emotion_add) {
            ll_emotion_layout.setVisibility(View.GONE);
            if (isvoide) {
                rebotton.setVisibility(View.GONE);
                isvoide = false;
            } else {
                rebotton.setVisibility(View.VISIBLE);
                isvoide = true;
            }
        } else if (v.getId() == R.id.bar_edit_text) {
            if (re_top != null && btn_longvoice != null) {
                re_top.setVisibility(View.VISIBLE);
                btn_longvoice.setVisibility(View.GONE);
            }
            if (rebotton != null) {
                rebotton.setVisibility(View.GONE);
            }
            mEmotionKeyboard.intercept();
            mIsEditext = false;
            isvoide = false;
        }
    }

}

