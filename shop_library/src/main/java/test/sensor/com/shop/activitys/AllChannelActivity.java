package test.sensor.com.shop.activitys;

/**
 * 全部频道
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import test.sensor.com.shop.adapters.ChannelAdapter;
import test.sensor.com.shop_library.R;

public class AllChannelActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView vRecyclerView;
    private ArrayList<String> mLists = new ArrayList<>();
    private ChannelAdapter mAdapter;
    private TextView vFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_channel);
        init();
    }

    private void init(){
        //初始化控件
        vRecyclerView = findViewById(R.id.recyclerview);
        vFinish = findViewById(R.id.tv_finish);
        vFinish.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);


        mLists.add("全部频道");
        mLists.add("type1");
        mLists.add("type2");
        mLists.add("type3");
        mLists.add("type4");
        mLists.add("type5");
        mLists.add("type6");
        mLists.add("type7");
        mLists.add("type8");
        mLists.add("推荐频道");
        mLists.add("type9");
        mLists.add("type10");
        mLists.add("type11");
        mLists.add("type12");
        //初始化适配器
        mAdapter = new ChannelAdapter(this, mLists);
        //初始化布局管理器
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        //动态设置返回的列数
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //返回要显示的列表数
                return position == 0 || position == 10 ? 4 : 1;
            }
        });
        vRecyclerView.setLayoutManager(mLayoutManager);
        //关联适配器
        vRecyclerView.setAdapter(mAdapter);
        //长按处理
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new TouchCallback());
        mItemTouchHelper.attachToRecyclerView(vRecyclerView);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back){
            finish();
        }else if(v.getId() == R.id.tv_finish){
            //完成
        }
    }

    /**
     * 长按可以排序
     */
    private class TouchCallback extends ItemTouchHelper.Callback  {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int swipeFlags = 0;
            int dragFlags = 0;
            int fromPosition = viewHolder.getAdapterPosition();
            //设置不可托动的位置
            if(fromPosition == 0 || fromPosition == 10){
                return TouchCallback.makeMovementFlags(dragFlags, swipeFlags);
            }
            dragFlags = ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            return TouchCallback.makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();
            //托动到该位置还原
            if(toPosition == 0 || toPosition == 10){
                return false;
            }
            if(fromPosition<toPosition){
                for(int i=fromPosition;i<toPosition;i++){
                    Collections.swap(mLists,i,i+1);
                }
            }else{
                for(int i=fromPosition;i>toPosition;i--){
                    Collections.swap(mLists,i,i-1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition,toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        /**
         * 长按选中Item的时候开始调用
         * @param viewHolder
         * @param actionState
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        /**
         * 手指松开的时候还原
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
        }
    }
}
