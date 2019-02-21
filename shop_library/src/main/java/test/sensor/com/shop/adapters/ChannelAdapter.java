package test.sensor.com.shop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import test.sensor.com.shop_library.R;

/**
 * Created by zhangxiaodong on 2019/2/20 17:47.
 * <br/>
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String>mLists ;
    public final int ITEM_TITLE = 0;
    public final int ITEM_NAME = 1;

    public ChannelAdapter(Context context,ArrayList<String> lists){
        mContext = context;
        mLists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewRes = -1;
        int type = 0;
        switch (viewType){
            case ITEM_TITLE:
                //标题
                viewRes = R.layout.row_title_layout;
                type = ITEM_TITLE;
                break;
            case ITEM_NAME:
                //频道名字
                viewRes = R.layout.row_channel_name_layout;
                type = ITEM_NAME;
                break;
        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewRes, parent, false),type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            //标题
            if(position == 0 || position == 10){
                holder.vTitle.setText(mLists.get(position));
            }else{
                //频道名字
                holder.vName.setText(mLists.get(position));
            }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == 10){
            return ITEM_TITLE;
        }else{
            return ITEM_NAME;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vTitle;
        private TextView vName;
        private ImageView vImage;

        public ViewHolder(View itemView,int type) {
            super(itemView);
            if(type == ITEM_TITLE){
                vTitle = itemView.findViewById(R.id.tv_title);
            }else if(type == ITEM_NAME){
                vName = itemView.findViewById(R.id.tv_channel_name);
                vImage = itemView.findViewById(R.id.iv_del);
                vImage.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            //删除
            if (v.getId() == R.id.iv_del){
                notifyItemChanged(getAdapterPosition());
                //获取当前要删除的元素
                String delString = mLists.get(getAdapterPosition());
                mLists.remove(getAdapterPosition());
                mLists.add(mLists.size(),delString);
                notifyDataSetChanged();
            }
        }
    }
}
