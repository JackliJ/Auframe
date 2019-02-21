package com.maiguoer.general;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.maiguoer.component.http.R;
import com.maiguoer.component.http.base.BasisFragment;


import java.util.ArrayList;

/**
 * Created by zhangxiaodong on 2018/10/30 14:55.
 * <br/>
 * 发文章->我的购买
 */

public class MyBuyGoodFragment extends BasisFragment implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private View mView;

    RecyclerView mRecyclerView;
    /**
     * 暂无商品
     */
    TextView mNotData;
      /**
     * recyclerView布局管理器
     */
    private GridLayoutManager mGridManager;
    /**
     * 适配器
     */
//    private GoodAdapter mAdapter;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 列表数据源
     */
//    private ArrayList<EmbedGoodListBean.DataBean.GoodsListBean> mLists = new ArrayList();
    /**
     * 保存已选择的商品
     */
//    private ArrayList<EmbedGoodListBean.DataBean.GoodsListBean> mSaveLists = new ArrayList();
    /**
     * 页码，第一页是1
     */
    private int mPage = 1;
    /**
     *最多只能选择数
     */
    private int mMaxNum = 3;
    /**
     * 已经选择数
     */
    private int mHasSelect = 0;
    /**
     * 已选择的商品ID集合
     */
    private ArrayList<Integer>mSelectGoodIds;
    /**
     *提示数
     */
    private int mTip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_my_buy_good, container, false);
            initView();
        }
        return mView;
    }

    /**
     * 初始化
     */
    private void initView() {
        mRecyclerView = mView.findViewById(R.id.recyclerview);
        mNotData = mView.findViewById(R.id.tv_not_data);


    }


    /**
     * 加载网络数据
     */
    private void getHttpData(){

    }

    @Override
    public void onLoadMoreRequested() {
        getHttpData();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    /**
     * 设置最大可选择数
     */
    public void setCanSelectNum(int m,int tip){
        mMaxNum =  m;
        this.mTip = tip;
    }

}
