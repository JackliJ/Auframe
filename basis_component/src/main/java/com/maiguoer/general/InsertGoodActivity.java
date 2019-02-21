package com.maiguoer.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.maiguoer.component.http.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Created by zhangxiaodong on 2018/10/26 16:58.
 * <br/>
 * 发布文章--插入商品
 */

public class InsertGoodActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    /**
     * 状态栏
     */
    View vStatusBarV;
    /**
     * viewpager
     */
    ViewPager mViewPager;
    /**
     *我的购买、我的商店
     */
    TabLayout mTabLayout;
    /**
     * 确定添加
     */
    TextView vAdd;
    /**
     * 保存页面fragment
     */
    private ArrayList<Fragment> mFragmentLists = new ArrayList<>();
    ArrayList<String> mTitles = new ArrayList<>();
    /**
     * 我的购买
     */
    MyBuyGoodFragment mBuyFragment;
    /**
     * 我的店铺
     */
    MyStoreGoodFragment mStoreFragment;
    /**
     * 已选择的商品ID
     */
    private ArrayList<Integer> mSelectGoodIds;
    /**
     * 最大选择数量
     */
    private int mMax ;
    /**
     *我的购买、我的店铺共用体
     */
    public static void navigateToInsertGoodActivity(Context context,int canSelect,ArrayList<Integer> goodIds) {
        Intent intent = new Intent(context, InsertGoodActivity.class);
        intent.putExtra("canSelect",canSelect);
        intent.putIntegerArrayListExtra("goodIds",goodIds);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_good);
        initViews();
        init();
    }

    private void initViews(){
        vStatusBarV = findViewById(R.id.v_status_bar);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabLayout);
        vAdd = findViewById(R.id.tv_confirm);
    }

    /**
     * 初始化
     */
    private void init(){
        //监听有商品被选择时，改变底部按扭颜色状态
        EventBus.getDefault().register(this);
        mMax = getIntent().getIntExtra("canSelect",0);
        mSelectGoodIds = getIntent().getIntegerArrayListExtra("goodIds");
        //我的购买
        mTitles.add(getResources().getString(R.string.dynamic_my_buy));
        //我的店铺
        mTitles.add(getResources().getString(R.string.dynamic_my_store));
        //关联ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        //我的购买商品
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("goodIds",mSelectGoodIds);
        mBuyFragment = MyBuyGoodFragment.newInstance(MyBuyGoodFragment.class,bundle);
        mStoreFragment = MyStoreGoodFragment.newInstance(MyStoreGoodFragment.class,bundle);
        //首次进入第一个fragmeng时设置最大可选择数
        mBuyFragment.setCanSelectNum(mMax,mMax);
        mFragmentLists.add(mBuyFragment);
        //我的店铺商品
        mFragmentLists.add(mStoreFragment);

        MyViewPageAdapter pageAdapter = new MyViewPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pageAdapter);
        //缓存个数
        mViewPager.setOffscreenPageLimit(mFragmentLists.size());
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
        //反射修改TabLayout指示器的宽度
        reflex(mTabLayout);
    }


    @Override
    protected void onResume() {
        super.onResume();
       /* setTranslucent(this);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
            if(position == 0){
                //获取我的店铺是否有选择的数据
//                mBuyFragment.setCanSelectNum(mMax-mStoreFragment.getSelectGoods().size(),mMax);
            }else{
                //获取我的购买是否有选择的数据
//                mStoreFragment.setCanSelectNum(mMax-mBuyFragment.getSelectGoods().size(),mMax);
            }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * viewpager适配器
     */
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

    /**
     * 反射修改TabLayout指示器的宽度
     *
     * @param tabLayout
     */
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

//                    int dp30 = (int) ApplicationUtils.TranslateDpiToPx(InsertGoodActivity.this, 8);
                    int dp30 = (int) TranslateDpiToPx(InsertGoodActivity.this, 8);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp30;
                        params.rightMargin = dp30;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 改变底部颜色
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 60)
    public void onChangeBoddomBtnColor(/*RefreshColorEvent*/ String event) {
        //从搜索页确定时这里需要关闭
        /*if(event.ismClose()){
            onComfirm();
        }else{
            //我的购买、我的店铺选择商品时调用
            if(mBuyFragment.getSelectGoods().size() > 0 || mStoreFragment.getSelectGoods().size() > 0){
                vAdd.setBackgroundResource(R.drawable.bg_b1_radius_0dp);
                vAdd.setEnabled(true);
            }else{
                vAdd.setBackgroundResource(R.drawable.bg_b5_radius_0dp);
                vAdd.setEnabled(false);
            }
        }*/
    }

    /**
     * 确定
     */
    private void onComfirm(){

    }

    public  float TranslateDpiToPx(Context context, float dpi) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi,context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
