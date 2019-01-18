package com.chat.business.library.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chat.business.library.R;
import com.chat.business.library.util.ChatItemClickListener;
import com.chat.business.library.util.EmotionUtils;
import com.chat.business.library.util.SendUtil;
import com.chat.business.library.util.SpanStringUtils;
import com.chat.business.library.util.TimeUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.maiguoer.component.http.utils.Constant;
import com.maiguoer.component.http.utils.ImageUtils;

import java.util.Date;
import java.util.List;

/**
 * Create by www.lijin@foxmail.com on 2019/1/4 0004.
 * <br/>
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    Context mContext;
    List<EMMessage> mEMMessage;
    View.OnClickListener clickListener;

    /**
     * 接收方文字
     */
    private static final int RECEIVE_TXT_MESSAGE_VIEW = 0x01;
    /**
     * 接收方图片
     */
    private static final int RECEIVE_IMAGE_MESSAGE_VIEW = 0x02;
    /**
     * 接收方语音
     */
    private static final int RECEIVE_VOICE_MESSAGE_VIEW = 0x03;
    /**
     * 接收方视频消息
     */
    private static final int RECEIVE_VOIDE_MESSAGE_VIEW = 0x04;
    /**
     * 接收方位置消息
     */
    private static final int RECEIVE_ADDRESS_MESSAGE_VIEW = 0x05;
    /**
     * 发送方文字
     */
    private static final int SEND_TXT_MESSAGE_VIEW = 0x10;
    /**
     * 发送方图片
     */
    private static final int SEND_IMAGE_MESSAGE_VIEW = 0x11;
    /**
     * 发送方语音消息
     */
    private static final int SEND_VOICE_MESSAGE_VIEW = 0x12;
    /**
     * 发送方视频消息
     */
    private static final int SEND_VOIDE_MESSAGE_VIEW = 0x13;
    /**
     * 发送方位置消息
     */
    private static final int SEND_ADDRESS_MESSAGE_VIEW = 0x15;


    /**
     * 文本扩展字段
     */
    private static final int RECEIVE_TXT_TEXTTYPE_01_VIEW = 0x21;
    private static final int SEND_TXT_TEXTTYPE_01_VIEW = 0x31;

    private ChatItemClickListener mListener;

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(ChatItemClickListener listener) {
        this.mListener = listener;
    }


    public ChatMessageAdapter(Context mContext, List<EMMessage> mEMMessage, View.OnClickListener clickListener) {
        this.mContext = mContext;
        this.mEMMessage = mEMMessage;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = mEMMessage.get(position);
        String type = message.getStringAttribute("text_type", "-1");
        if (message.getType() == EMMessage.Type.TXT) {
            if (type.equals(Constant.EXTENSION_FIELD_01)) {
                return -1;
            } else {
                return message.direct() == EMMessage.Direct.RECEIVE ? RECEIVE_TXT_MESSAGE_VIEW : SEND_TXT_MESSAGE_VIEW;
            }
        } else if (message.getType() == EMMessage.Type.IMAGE) {
            // 图片消息
            return message.direct() == EMMessage.Direct.RECEIVE ? RECEIVE_IMAGE_MESSAGE_VIEW : SEND_IMAGE_MESSAGE_VIEW;
        } else if (message.getType() == EMMessage.Type.VOICE) {
            // 语音消息
            return message.direct() == EMMessage.Direct.RECEIVE ? RECEIVE_VOICE_MESSAGE_VIEW : SEND_VOICE_MESSAGE_VIEW;
        } else if (message.getType() == EMMessage.Type.VIDEO) {
            //视频消息
            return message.direct() == EMMessage.Direct.RECEIVE ? RECEIVE_VOIDE_MESSAGE_VIEW : SEND_VOIDE_MESSAGE_VIEW;
        } else if (message.getType() == EMMessage.Type.LOCATION) {
            //位置信息
            return message.direct() == EMMessage.Direct.RECEIVE ? RECEIVE_ADDRESS_MESSAGE_VIEW : SEND_ADDRESS_MESSAGE_VIEW;
        } else {
            return -1;
        }
    }

    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewRes = -1;
        switch (viewType) {
            case RECEIVE_TXT_MESSAGE_VIEW: {
                // 文字消息
                viewRes = R.layout.row_receive_chat_txt_message;
            }
            break;
            case RECEIVE_VOICE_MESSAGE_VIEW: {
                //语音消息接收方
                viewRes = R.layout.row_receive_chat_voice_message;
            }
            break;
            case SEND_TXT_MESSAGE_VIEW: {
                // 文字消息
                viewRes = R.layout.row_send_chat_txt_message;
            }
            break;
            case SEND_VOICE_MESSAGE_VIEW: {
                //语音消息发送方
                viewRes = R.layout.row_send_chat_voice_message;
            }
            break;

        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewRes, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(ChatMessageAdapter.ViewHolder holder, int position) {
        final EMMessage message = mEMMessage.get(position);
        long time = message.getMsgTime();
        boolean showTime = true;
        if (position > 0) {
            showTime = time - mEMMessage.get(position - 1).getMsgTime() > 300000;
        }

        if (showTime) {
            holder.vMessageTimeTv.setVisibility(View.VISIBLE);
            holder.vMessageTimeTv.setText(TimeUtils.distanceBeforeNow(new Date(time), mContext));
        } else {
            holder.vMessageTimeTv.setVisibility(View.GONE);
        }

        switch (message.direct()) {
            // ===============================================左(收到的消息)===================================
            case RECEIVE: {
                switch (getItemViewType(position)) {
                    case RECEIVE_TXT_MESSAGE_VIEW://文字消息
                        // 消息内容
                        String messageContent = ((EMTextMessageBody) message.getBody()).getMessage();
                        if (!TextUtils.isEmpty(messageContent)) {
                            holder.vMessageContentTv.setText(SpanStringUtils.getEmotionContentText(EmotionUtils.EMOTION_TOTAL, mContext, holder.vMessageContentTv, messageContent));
                        }
                        break;
                    case RECEIVE_VOICE_MESSAGE_VIEW://文字消息
                        ReceiveAvatar(message, holder.vAvatarIv);
                        //截取本地路径
                        String reqResult = message.getBody() + "";
                        String length = null;
                        try {
                            String[] getSignInfo = reqResult.split(",");
                            String getlenth = getSignInfo[3];
                            String[] getlengths = getlenth.split(":");
                            //截取语音长度
                            length = getlengths[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if (!TextUtils.isEmpty(length)) {
                            holder.vMessageContentTv.setText("    " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                        }
                        //判断语音是否已听
                        if (message.isListened()) {
                            holder.vImgRead.setVisibility(View.INVISIBLE);
                        } else {
                            holder.vImgRead.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
            break;
            // ===============================================右(发出的消息)===================================
            case SEND: {
                switch (getItemViewType(position)) {
                    case SEND_TXT_MESSAGE_VIEW://文字消息
                        MessageStatus(message, holder, position);
                        // 消息内容
                        String messageContent = ((EMTextMessageBody) message.getBody()).getMessage();
                        if (!TextUtils.isEmpty(messageContent)) {
                            holder.vMessageContentTv.setText(SpanStringUtils.getEmotionContentText(EmotionUtils.EMOTION_TOTAL, mContext, holder.vMessageContentTv, messageContent));
                        }
                        break;
                    case SEND_VOICE_MESSAGE_VIEW:
                        MessageStatus(message, holder, position);
                        //截取本地路径
                        String length = null;
                        try {
                            String reqResult = message.getBody() + "";
                            String[] getSignInfo = reqResult.split(",");
                            String getlenth = getSignInfo[3];
                            String[] getlengths = getlenth.split(":");
                            //截取语音长度
                            length = getlengths[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //根据语音长度设置框的长度
                        if (!TextUtils.isEmpty(length)) {
                            if (Integer.parseInt(length) >= 0 && Integer.parseInt(length) <= 20) {
                                holder.vMessageContentTv.setText("    " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            } else if (Integer.parseInt(length) > 20 && Integer.parseInt(length) <= 50) {
                                holder.vMessageContentTv.setText("        " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            } else if (Integer.parseInt(length) > 50 && Integer.parseInt(length) <= 80) {
                                holder.vMessageContentTv.setText("             " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            } else if (Integer.parseInt(length) > 80 && Integer.parseInt(length) < 110) {
                                holder.vMessageContentTv.setText("                 " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            } else if (Integer.parseInt(length) > 110 && Integer.parseInt(length) < 140) {
                                holder.vMessageContentTv.setText("                           " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            } else if (Integer.parseInt(length) > 140 && Integer.parseInt(length) < 180) {
                                holder.vMessageContentTv.setText("                                   " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            } else {
                                holder.vMessageContentTv.setText("                                            " + TimeUtils.LongGetMinute(Integer.parseInt(length)) + "”    ");
                            }

                        }
                        //判断语音是否已听
                        if (message.isListened()) {
                            holder.vImgRead.setVisibility(View.INVISIBLE);
                        } else {
                            holder.vImgRead.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return mEMMessage.size();
    }

    String mAvatar;

    private void ReceiveAvatar(final EMMessage message, ImageView vAvatarIv) {
        mAvatar = message.getStringAttribute(Constant.STRING_ATTRIBUTE_GROUP_USERAVATAR, null);
        if (!TextUtils.isEmpty(mAvatar)) {
//            ImageDisplayUtils.displayWithTransform(mContext, mAvatar, vAvatarIv, new CircleTransform());
            ImageUtils.loadImage(mContext, mAvatar, vAvatarIv);
        }
        vAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //获取的是自己本身的UID
//                    int otherUid = Integer.parseInt(message.getStringAttribute("uid", ""));
//                    CardActivity.selectCardActivity(mContext, otherUid);
                } catch (Exception e) {
//                    MgeToast.showErrorToast(mContext, mContext.getResources().getString(R.string.chat_getuser_avatar), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * 赋值消息状态
     *
     * @param message
     * @param holder
     * @param position
     */
    private void MessageStatus(final EMMessage message, ChatMessageAdapter.ViewHolder holder, final int position) {
        //赋值消息状态
        if (message.status() == EMMessage.Status.SUCCESS) {
            holder.vLiStatus.setVisibility(View.GONE);
        } else if (message.status() == EMMessage.Status.FAIL) {
            holder.vLiStatus.setVisibility(View.VISIBLE);
            holder.vImgStatusError.setVisibility(View.VISIBLE);
            holder.vStatusProgress.setVisibility(View.GONE);
        } else {
            holder.vLiStatus.setVisibility(View.VISIBLE);
            holder.vImgStatusError.setVisibility(View.GONE);
            holder.vStatusProgress.setVisibility(View.VISIBLE);
        }
        holder.vLiStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.status() == EMMessage.Status.FAIL) {
                    //进行该条消息重发
                    SendUtil.resendMessage(message);
                    //刷新消息状态
                    notifyItemChanged(position);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // 延迟执行的代码
//                            EventBus.getDefault().post(new ChatReceivedEventBus());
                        }
                    }, 1000);// 1000毫秒执行，1秒
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        /**
         * 消息时间
         */
        TextView vMessageTimeTv;
        /**
         * 消息头像
         */
        ImageView vAvatarIv;
        /**
         * 文字消息内容
         */
        TextView vMessageContentTv;
        /**
         * 文字消息对话气泡
         */
        RelativeLayout vMessageBgRl;
        /**
         * 消息状态布局
         */
        LinearLayout vLiStatus;
        /**
         * 消息发送失败
         */
        ImageView vImgStatusError;
        /**
         * 消息发送中
         */
        ProgressBar vStatusProgress;
        /**
         * 消息的红点
         */
        ImageView vImgRead;


        private ChatItemClickListener mListener;

        public ViewHolder(View itemView, ChatItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            vMessageTimeTv = itemView.findViewById(R.id.tv_message_time);
            vAvatarIv = itemView.findViewById(R.id.iv_avatar);
            vMessageContentTv = itemView.findViewById(R.id.tv_message_content);
            vMessageBgRl = itemView.findViewById(R.id.rl_message_bg);
            vLiStatus = itemView.findViewById(R.id.msg_status_li);
            vImgStatusError = itemView.findViewById(R.id.msg_status_error);
            vStatusProgress = itemView.findViewById(R.id.msg_status_progress);
            vImgRead = itemView.findViewById(R.id.img_voice_read);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null) {
                mListener.onLongClick(vMessageBgRl, getPosition());
            }
            return true;
        }
    }
}
