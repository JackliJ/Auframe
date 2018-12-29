package com.guide.business.library.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.guide.business.library.BuildConfig;
import com.guide.business.library.R;
import com.guide.business.library.weight.ImageFragment;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * 引导页和广告页面 判断用户是否首次登陆 如果是则展示引导页面  否则展示广告页面  默认五秒
 * Create by www.lijin@foxmail.com on 2018/12/28 0028.
 * <br/>
 */

@Route(path = "/guide/GuiDeMain")
public class GuiDeMain extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * 加载图片控件 viewPager
     */
    private ViewPager mViewPager;
    /**
     * 跳app按扭
     */
    private Button mButton;
    /**
     * 存放图片的数据源
     */
    private ArrayList<Fragment> mFragmentLists = new ArrayList<>();
    private LinearLayout mDotLayous;
    /**
     * 广告的布局
     */
    private RelativeLayout mAdvertising;

    private LinearLayout mTiming;
    /**
     * 倒计时的TextView
     */
    private TextView skipNumberTv;

    /**
     * 单个图片页
     */
    private ImageFragment mFirstImageFragment;
    private ImageFragment mSecondImageFragment;
    private ImageFragment mThreeImageFragment;
    private ImageFragment mFourImageFragment;
    /**
     * 倒计时类
     */
    private SkipTimeCount mTimeCount;
    /**
     * 倒计时 时长
     */
    private long millisInFuture = 6000;
    /**
     * 每次 倒计一时的 时长
     */
    private long countDownInterval = 1000;

    private Context mContext;
    private final static String GUIDE_KEY = "GUIDE_KEY";
    private final static String TAG = "GuiDeMain_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.guide_main_layout);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        //圆点布局
        mDotLayous = findViewById(R.id.layoutDots);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mViewPager = findViewById(R.id.flash_viewpager);

        mAdvertising = findViewById(R.id.guide_re);
        mTiming = findViewById(R.id.v_skip_ll);
        mTiming.setOnClickListener(this);
        skipNumberTv = findViewById(R.id.splash_skip_number);


    }

    /**
     * 初始化viewPager
     */
    private void initViewPager() {
        //初始化每个页面
        mFirstImageFragment = ImageFragment.newInstance(ImageFragment.class, null);
        mSecondImageFragment = ImageFragment.newInstance(ImageFragment.class, null);
        mThreeImageFragment = ImageFragment.newInstance(ImageFragment.class, null);
        mFourImageFragment = ImageFragment.newInstance(ImageFragment.class, null);

        //添加每张图片的容器到集合
        mFragmentLists.add(mFirstImageFragment);
        mFragmentLists.add(mSecondImageFragment);
        mFragmentLists.add(mThreeImageFragment);
        mFragmentLists.add(mFourImageFragment);
        //设置适配器
        mViewPager.setAdapter(new MyViewPageAdapter(getSupportFragmentManager()));

        //设置viewPager滑动监听
        mViewPager.addOnPageChangeListener(this);
        //设置默认为第一张图片
        mViewPager.setCurrentItem(0);
    }

    private void initData() {
        //取得缓存的首次安装标识
        int lastVersion = SharedPreferencesUtils.getLastApplicationVersion(mContext);
        // 低于当前版本展示引导页
        if (lastVersion < BuildConfig.VERSION_CODE) {
            mAdvertising.setVisibility(View.GONE);
            mTiming.setVisibility(View.GONE);
            initViewPager();
        } else {
            //展示广告页面
            //判断用户是否登录  如果已经登录  自动登录  如果没有登录 跳转到登录
            mAdvertising.setVisibility(View.VISIBLE);
            mTiming.setVisibility(View.VISIBLE);
            initAdvertising();
        }

    }

    private void initAdvertising() {
        //加载广告图片
        //开始倒计时
        mTimeCount = new SkipTimeCount(millisInFuture, countDownInterval, mContext, skipNumberTv);
        mTimeCount.start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position) {
            case 0:
                //设置显示第一张图片，并隐藏最后一张的跳转按扭
                mFirstImageFragment.setImage(R.mipmap.guide_icon_one);
                mButton.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //设置显示第二张图片，并隐藏最后一张的跳转按扭
                mSecondImageFragment.setImage(R.mipmap.guide_icon_two);
                mButton.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //设置显示第三张图片，并隐藏最后一张的跳转按扭
                mThreeImageFragment.setImage(R.mipmap.guide_icon_three);
                mButton.setVisibility(View.INVISIBLE);
                break;
            case 3:
                //设置显示第四张图片，并显示最后一张的跳转按扭
                mFourImageFragment.setImage(R.mipmap.guide_icon_form);
                mButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v_skip_ll) {//跳过倒计时
            mTimeCount.onFinish();

        } else if (i == R.id.button) {//存储标识
            SharedPreferencesUtils.SaveLastApplicationVersion(mContext);
            //跳转到登录页面
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class MyViewPageAdapter extends FragmentPagerAdapter {

        public MyViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentLists.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentLists.size();
        }
    }

    class SkipTimeCount extends CountDownTimer {

        Context mContext;
        TextView vTextView;

        /**
         * 参数依次为总时长,和计时的时间间隔
         *
         * @param millisInFuture    总时长
         * @param countDownInterval 计时间隔
         * @param context           上下文
         * @param vTextView         控件
         */
        public SkipTimeCount(long millisInFuture, long countDownInterval, Context context, TextView vTextView) {
            super(millisInFuture, countDownInterval);
            this.vTextView = vTextView;
            this.mContext = context;
        }

        /**
         * 计时完毕时触发
         */
        @Override
        public void onFinish() {
            //取消倒计时
            mTimeCount.cancel();
            // 跳转首页
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            vTextView.setClickable(false);
            vTextView.setText(millisUntilFinished / 1000 + "");
        }
    }
}
