package com.guide.business.library.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.guide.business.library.R;
import com.maiguoer.component.http.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 仿QQ相册上滑发送
 */
@Route(path = "/guide/SendPhotoAlbumActivity")
public class SendPhotoAlbumActivity extends FragmentActivity implements PhotoAdapter.onToucherListener {

    View vStatusBarV;
    RecyclerView mRecyclerView;
    ArrayList<String> mImageLists = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private ImageView thumbImg;
    //记录当前下标位置
    private int mcurPosition;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_send_phone_album);
        init();
        loadData();
    }

    private void init(){
        vStatusBarV = findViewById(R.id.v_status_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        thumbImg = (ImageView)findViewById(R.id.iv_image);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1, OrientationHelper.HORIZONTAL,false));
        mAdapter = new PhotoAdapter(this,mImageLists,this);
        mRecyclerView.setAdapter(mAdapter);
        //item长按处理
        mItemTouchHelper = new ItemTouchHelper(new TouchCallback());
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void loadData(){
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514834453&di=23c876ea53952645eee915694ec93414&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fbaike%2Fc0%253Dbaike60%252C5%252C5%252C60%252C20%2Fsign%3D860626d449fbfbedc8543e2d19999c53%2F3b87e950352ac65ccc91d198f9f2b21192138a41.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514834452&di=c5e0618c302efe3118fcdee5c064de79&imgtype=0&src=http%3A%2F%2Fimg04.tooopen.com%2Fimages%2F20130203%2Ftooopen_01471485.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514834450&di=35ff3e68f1d3dd2d9043e59d8abe1475&imgtype=0&src=http%3A%2F%2Fpic29.nipic.com%2F20130531%2F8786105_102319220000_2.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514742848&di=73f886fddcb0cbf9d928e8ec8f96d77f&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01c60d562db4fc6ac725487885e3f6.jpg%401280w_1l_2o_100sh.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514625604&di=93e313fa37208ece46904fe2dddc9d7f&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f9df56fccb6c32f875a944518d4c.JPG%401280w_1l_2o_100sh.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514625595&di=dc4cbfc41f3999b0b4b2541464176c57&imgtype=0&src=http%3A%2F%2Fimg3.redocn.com%2Ftupian%2F20150106%2Faixinxiangkuang_3797284.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514742851&di=b2b6d52729a29292f413f58756eff608&imgtype=0&src=http%3A%2F%2Fhellorfimg.zcool.cn%2Fpreview%2F291148670.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514742849&di=83439c96d306d231110efb5162248093&imgtype=0&src=http%3A%2F%2Fwww.sucaitianxia.com%2Fsheji%2Fpic%2F200707%2F20070723160945961.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514625609&di=1ce92f680bc0349f38edbc19fe7321db&imgtype=0&src=http%3A%2F%2Fa3.att.hoodong.com%2F33%2F86%2F01300000083517120831868766468.jpg");
        mImageLists.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546514625610&di=58f4a83ace2e95a47fa342f196aebd6c&imgtype=0&src=http%3A%2F%2Fwww.hinews.cn%2Fpic%2F0%2F11%2F05%2F89%2F11058981_998643.jpg");
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.setTranslucent(this,R.color.b2);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = Utils.getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
    }

    @Override
    public void onToucher(PhotoAdapter.ViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    /**
     * 长按可以排序
     */
    private class TouchCallback extends ItemTouchHelper.Callback  {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int swipeFlags = 0;
            int dragFlags = 0;
            dragFlags = ItemTouchHelper.UP;
            return TouchCallback.makeMovementFlags(dragFlags, swipeFlags);
        }

        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            return false;
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
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                mcurPosition = viewHolder.getLayoutPosition();
            }
        }

        /**
         * 手指松开的时候还原
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            LogUtils.e("--松开时"+"Y==" + viewHolder.itemView.getY(),"<<<x="+ viewHolder.itemView.getX());
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            if(viewHolder.itemView.getY() < 596){
                Glide.with(SendPhotoAlbumActivity.this).load(mImageLists.get(mcurPosition)).into(thumbImg);
            }
            super.clearView(recyclerView, viewHolder);
        }
    }

}
