package com.chat.business.library.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chat.business.library.R;
import com.chat.business.library.model.ConversationBean;
import com.chat.business.library.util.ChatItemClickListener;
import com.chat.business.library.util.EmotionUtils;
import com.chat.business.library.util.ItemTouchHelperViewHolder;
import com.chat.business.library.util.SpanStringUtils;
import com.chat.business.library.util.TimeUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.maiguoer.component.http.utils.CalendarUtil;
import com.maiguoer.component.http.utils.Constant;
import com.maiguoer.component.http.utils.ImageUtils;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;
import com.maiguoer.widget.ShapedImageView;


import java.util.List;

/**
 * Created by Sky on 2017/8/30.
 * <p>
 * 消息列表Adapter
 */

public class ChatMessageListAdapter extends RecyclerView.Adapter<ChatMessageListAdapter.ViewHolder> {

    /**
     * 头部，包含搜索，小秘书，脉友圈
     */
    private static final int CHAT_MESSAGE_HEAD = 0X001;
    /**
     * 消息item
     */
    private static final int CHAT_MESSAGE_ITEM = 0X002;


    Context mContext;
    public List<ConversationBean> mConverdata;
    private ChatItemClickListener mListener;


    public ChatMessageListAdapter(Context context, List<ConversationBean> mConverdata) {
        this.mContext = context;
        this.mConverdata = mConverdata;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CHAT_MESSAGE_HEAD;
        } else {
            return CHAT_MESSAGE_ITEM;
        }
    }

    @Override
    public ChatMessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewRes = -1;
        switch (viewType) {
            case CHAT_MESSAGE_HEAD: {
                viewRes = R.layout.row_chat_message_list_head;
            }
            break;
            case CHAT_MESSAGE_ITEM: {
                viewRes = R.layout.row_chat_message_list;
            }
            break;
        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewRes, parent, false), mListener);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(ChatItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case CHAT_MESSAGE_HEAD:
                break;
            case CHAT_MESSAGE_ITEM://列表
                //文本
                //=================================================群聊========================================================
                holder.vTdelete.setText(mContext.getResources().getString(R.string.chat_cp_delete));
                holder.vItemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.vTdelete.getText().toString().equals(mContext.getResources().getString(R.string.chat_cp_delete_ok))) {
                            //http://blog.csdn.net/iamdingruihaha/article/details/73274010
                            //由于notifyItemRangeChanged是多线程的 快速删除也可能导致越界，上面的解决方式不够优雅 可以参考
                            try {
                                EMClient.getInstance().chatManager().deleteConversation(mConverdata.get(holder.getAdapterPosition() - 1).getUuid(), true);
                                mConverdata.remove(holder.getAdapterPosition() - 1);
                                if (mConverdata.size() == 1) {
                                    notifyDataSetChanged();
                                } else {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), mConverdata.size());
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            holder.vTdelete.setText(mContext.getResources().getString(R.string.chat_cp_delete_ok));
                        }
                    }
                });
                holder.vLContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position);
                        }
                    }
                });
                if (mConverdata.size() > 0 && mConverdata != null && !(position > mConverdata.size())) {
                    if (mConverdata.get(position - 1).getChatType().toString().equals(EMMessage.ChatType.GroupChat.toString())) {
                        //设置红点颜色
                        holder.vMessageCountTv.setBackgroundResource(R.drawable.message_oval_red);

                        //====================================================单聊===================================================================================
                    } else if (mConverdata.get(position - 1).getChatType().toString().equals(EMMessage.ChatType.Chat.toString())) {
                        //设置红点颜色
                        holder.vMessageCountTv.setBackgroundResource(R.drawable.message_oval_red);
                        //判断扩展中是否有值
                        if (!TextUtils.isEmpty(mConverdata.get(position - 1).getUsercontext())) {
                            //头像点击名片跳转
                            holder.vMessageAvatarIv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            //获取缓存中的草稿
                            String draft = (String) SharedPreferencesUtils.getParam(mContext, mConverdata.get(position - 1).getUuid(), "");
                            if (!TextUtils.isEmpty(draft)) {
                                //显示草稿图标
                                holder.vImageDraft.setVisibility(View.VISIBLE);
                                holder.vMessageContentTv.setText(SpanStringUtils.getEmotionContentText(EmotionUtils.EMOTION_TOTAL, mContext, holder.vMessageContentTv, draft));
                            } else {
                                //隐藏草稿图标
                                holder.vImageDraft.setVisibility(View.GONE);
                                if (EMMessage.Type.TXT.toString().equals(mConverdata.get(position - 1).getType())) {//文本
                                    if (!TextUtils.isEmpty(mConverdata.get(position - 1).getTextType())) {
                                        String reqResult = mConverdata.get(position - 1).getUsercontext();
                                        String reg = "\"";
                                        //解析完成的扩展数组 详细匹配规则参考文档或头部注释
                                        String[] getSignInfo = reqResult.split(reg);
                                        //获取当前的类型
                                        if (mConverdata.get(position - 1).getTextType().equals(Constant.EXTENSION_FIELD_10)) {//名片推荐(推荐好友)

                                        }
                                    } else {//如果扩展字段为空 则为纯文本类型
                                        String reqResult = mConverdata.get(position - 1).getUsercontext();
                                        String reg = "\"";
                                        String[] getSignInfo = reqResult.split(reg);
                                        if (mConverdata.get(position - 1).getUserunread() != 0) {
                                            holder.vMessageContentTv.setText(SpanStringUtils.getEmotionContentText(
                                                    EmotionUtils.EMOTION_TOTAL, mContext,
                                                    holder.vMessageContentTv,
                                                    "[" + mConverdata.get(position - 1).getUserunread() + mContext.getResources().getString(R.string.chat_tiao_kuang) + getSignInfo[1]));
                                        } else {
                                            holder.vMessageContentTv.setText(SpanStringUtils.getEmotionContentText(EmotionUtils.EMOTION_TOTAL, mContext, holder.vMessageContentTv, getSignInfo[1]));
                                        }
                                    }
                                } else if (EMMessage.Type.VOICE.toString().equals(mConverdata.get(position - 1).getType())) {//语音
                                    if (mConverdata.get(position - 1).getUserunread() != 0) {
                                        holder.vMessageContentTv.setText("[" + mConverdata.get(position - 1).getUserunread() + "]" + mContext.getResources().getString(R.string.chat_send_tyle_content));
                                    } else {
                                        holder.vMessageContentTv.setText(mContext.getResources().getString(R.string.chat_send_tyle_content));
                                    }
                                }
                            }
                        }
                        //加载时间
                        holder.vMessageTimeTv.setText(CalendarUtil.getHour(TimeUtils.GetTimerubbing(mConverdata.get(position - 1).getEndtime())));
                        if (mConverdata.get(position - 1).getUseravatar() != null && !"".equals(mConverdata.get(position - 1).getUseravatar())) {
                            //头像
                            ImageUtils.loadImage(mContext, mConverdata.get(position - 1).getUseravatar(), holder.vMessageAvatarIv);
                        }
                        if (mConverdata.get(position - 1).getUserunread() == 0) {
                            holder.vMessageCountTv.setVisibility(View.GONE);
                        } else {
                            holder.vMessageCountTv.setVisibility(View.VISIBLE);
                            if (mConverdata.get(position - 1).getUserunread() > 99) {
                                holder.vMessageCountTv.setText("99+");
                            } else {
                                holder.vMessageCountTv.setText(mConverdata.get(position - 1).getUserunread() + "");
                            }

                        }
                    }
                }


                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mConverdata.size() + 1;//+1展示小秘书和脉友圈
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        /** begin 头部View    **/

        /**
         * 小秘书
         */
        RelativeLayout vSecretaryRl;
        /**
         * 小秘书头像
         */
        ShapedImageView vSecretartIv;
        /**
         * 小秘书消息数
         */
        TextView vDynamicNotificationTv;
        /**
         * 公告通知
         */
        RelativeLayout vAnnouncement;
        /**
         * 公告未读消息数量
         */
        TextView vTAnnNum;
        /**
         * 公告消息内容
         */
        TextView vTAnnContent;

        /** end 头部View  **/

        /**
         * 消息头像
         */
        ShapedImageView vMessageAvatarIv;
        /**
         * 对方名称或群名称
         */
        TextView vMessageNameTv;
        /**
         * Vip状态
         */
        ImageView vVIPStatusIv;
        /**
         * 草稿图标
         */
        TextView vImageDraft;
        /**
         * 消息内容
         */
        TextView vMessageContentTv;
        /**
         * 有人@我
         */
        TextView vMessageMarking;
        /**
         * 消息时间
         */
        TextView vMessageTimeTv;
        /**
         * 消息数量
         */
        TextView vMessageCountTv;
        /**
         * 系统消息模块
         */
        TextView vSystemMessage;
        /**
         * 侧滑删除按钮
         */
        LinearLayout vItemDelete;
        /**
         * 主布局区域
         */
        LinearLayout vLContent;
        TextView vTdelete;


        public ViewHolder(View itemView, ChatItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            itemView.setOnClickListener(this);
            vTdelete = itemView.findViewById(R.id.chat_message_delete_tv);
            vLContent = itemView.findViewById(R.id.chat_message_content);
            vItemDelete = itemView.findViewById(R.id.chat_del);
            vSystemMessage = itemView.findViewById(R.id.system_message_convertion);
            vMessageCountTv = itemView.findViewById(R.id.tv_message_count);
            vMessageTimeTv = itemView.findViewById(R.id.tv_message_time);
            vMessageMarking = itemView.findViewById(R.id.tv_message_marking);
            vMessageContentTv = itemView.findViewById(R.id.tv_message_content);
            vImageDraft = itemView.findViewById(R.id.tv_message_draft);
            vVIPStatusIv = itemView.findViewById(R.id.iv_vip_status);
            vMessageNameTv = itemView.findViewById(R.id.tv_message_name);
            vMessageAvatarIv = itemView.findViewById(R.id.iv_message_avatar);
            vTAnnContent = itemView.findViewById(R.id.tv_annount_content);
            vTAnnNum = itemView.findViewById(R.id.tv_annount_count);
            vAnnouncement = itemView.findViewById(R.id.chat_announcement);
            vDynamicNotificationTv = itemView.findViewById(R.id.tv_secretary_message_count);
            vSecretartIv = itemView.findViewById(R.id.iv_secretary);
            vSecretaryRl = itemView.findViewById(R.id.rl_secretary);
        }


        private ChatItemClickListener mListener;

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
            if (v.getId() == R.id.rl_frend_system) {

            } else if (v.getId() == R.id.rl_secretary) {

            } else if (v.getId() == R.id.chat_announcement) {

            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }


}
