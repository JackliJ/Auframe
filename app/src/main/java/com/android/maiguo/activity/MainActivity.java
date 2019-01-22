package com.android.maiguo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.maiguo.fragments.CommunicationFragment;
import com.android.maiguo.fragments.HomeMainFragment;
import com.android.maiguo.fragments.MeFragment;
import com.android.maiguo.fragments.MerchantFragment;
import com.chat.business.library.ui.ChatMessageListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaodong on 2019/1/21.
 * 首页界面
 * 应用主页 用于启动项目 这里展示项目主页  完成初始化后 跳转到引导广告页面
 */
@Route(path = "/app/ChatLoginActivity")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * fragment管理器
     */
    private FragmentManager mFragmentManager;
    /**
     * 首页
     */
    private HomeMainFragment mHomePageFragment;
    /**
     * 商户
     */
    private MerchantFragment mMerchantFragment;
    /**
     * 交流
     */
    private ChatMessageListFragment mCommunicationFragment;
    /**
     * 我的
     */
    private MeFragment mMeFragment;
    /**
     * 中间内容布局
     */
    private FrameLayout vMiddleLayout;
    /**
     * 中间+号
     */
    private ImageView vAdd;
    /**
     * 底部按扭对应的资源ID
     */
    private int[] mBtnRes = {R.id.rb_homepage, R.id.rb_merchant, R.id.rb_communication, R.id.rb_me};
    /**
     * 底部底部控件集合
     */
    private List<RadioButton> mBtns = new ArrayList<RadioButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private void init() {
        mFragmentManager = this.getSupportFragmentManager();

        //初始化控件
        for (int i = 0; i < mBtnRes.length; i++) {
            RadioButton layout = (RadioButton) findViewById(mBtnRes[i]);
            layout.setOnClickListener(this);
            mBtns.add(layout);
        }
        //中间加号
        vAdd = (ImageView) findViewById(R.id.iv_middle);
        //中间加号对应的内容
        vMiddleLayout = (FrameLayout) findViewById(R.id.middle_layout);
        vAdd.setOnClickListener(this);
        //默认
        setTabSelect(0);

    }

    //设置当前显示哪个页面
    private void setTabSelect(int index) {
        //设置所有按扭为非选择状态
        for (int i = 0; i < mBtnRes.length; i++) {
            mBtns.get(i).setSelected(false);
        }
        //设置当前按扭为选中状态
        mBtns.get(index).setSelected(true);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);

        if (index == 0) {
            //首页
            if (mHomePageFragment == null) {
                mHomePageFragment = HomeMainFragment.newInstance(HomeMainFragment.class, null);
                transaction.add(R.id.content_layout, mHomePageFragment);
            } else {
                transaction.show(mHomePageFragment);
            }
        } else if (index == 1) {
            //商户
            if (mMerchantFragment == null) {
                mMerchantFragment = MerchantFragment.newInstance(MerchantFragment.class, null);
                transaction.add(R.id.content_layout, mMerchantFragment);
            } else {
                transaction.show(mMerchantFragment);
            }
        } else if (index == 2) {
            //交流
            if (mCommunicationFragment == null) {
                mCommunicationFragment = CommunicationFragment.newInstance(ChatMessageListFragment.class, null);
                transaction.add(R.id.content_layout, mCommunicationFragment);
            } else {
                transaction.show(mCommunicationFragment);
            }
        } else if (index == 3) {
            //我的
            if (mMeFragment == null) {
                mMeFragment = MeFragment.newInstance(MeFragment.class, null);
                transaction.add(R.id.content_layout, mMeFragment);
            } else {
                transaction.show(mMeFragment);
            }
        }
        //开启动画
        startAnimal(mBtns.get(index));
        transaction.commit();
    }

    /**
     * 开启动画
     */
    private void startAnimal(RadioButton rb) {
        //多个动画 动画集
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rb, "scaleX", 1, 1.2f, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rb, "scaleY", 1, 1.2f, 1);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    /**
     * 隐藏所有的fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mHomePageFragment != null) {
            transaction.hide(mHomePageFragment);
        }
        if (mMerchantFragment != null) {
            transaction.hide(mMerchantFragment);
        }
        if (mCommunicationFragment != null) {
            transaction.hide(mCommunicationFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_homepage:
                //首页
                setTabSelect(0);
                break;
            case R.id.rb_merchant:
                //商户
                setTabSelect(1);
                break;
            case R.id.rb_communication:
                //交流
                setTabSelect(2);
                break;
            case R.id.rb_me:
                //我的
                setTabSelect(3);
                break;
            case R.id.iv_middle:
                //中间加号
                if (vMiddleLayout.getVisibility() == View.GONE) {
                    vMiddleLayout.setVisibility(View.VISIBLE);
                } else {
                    vMiddleLayout.setVisibility(View.GONE);
                }
                break;
        }
    }
}