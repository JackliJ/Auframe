package com.chat.business.library.util;

import android.content.Context;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.maiguoer.component.http.data.User;


/**
 * Created by www.lijin@foxmail.com on 2017/9/26 0026.
 * <br/>
 * 发送消息帮助类
 */
public class SendUtil {

    //==========================================Chat====================================================

    /**
     * 发送文本消息
     *
     * @param Content 文本
     * @param HXID    环信ID
     */
    public static final void SendText(Context context,
                                      String Content,
                                      String HXID,
                                      String otherUid,
                                      String otherUsername,
                                      String otherUserAvatar,
                                      String otherVipLevel,
                                      String otherAuthStatus,
                                      String otherBusinessAuthStatus,
                                      String otherNamgeCardBgImage,
                                      String text_type
//            ,EMCallBack callBack
    ) {
        User user = User.GetLoginedUser(context);
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(Content, HXID);
        message.setTo(HXID);
        message.setChatType(EMMessage.ChatType.Chat);
        if (text_type != null) {
            message.setAttribute("text_type", text_type);
        }
        //设置离线推送 (仅针对小米，华为机型)
        message.setAttribute("em_force_notification", true);
//        message.setAttribute("uid", user.uid);
//        message.setAttribute("username", user.username);
//        message.setAttribute("userAvatar", user.avatar);
//        message.setAttribute("nameCardBgImage", user.bgImage);
//        message.setAttribute("otherUid", otherUid);
//        message.setAttribute("otherUsername", otherUsername);
//        message.setAttribute("otherUserAvatar", otherUserAvatar);
//        message.setAttribute("otherNameCardBgImage", otherNamgeCardBgImage);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
//                EventBus.getDefault().post(new ChatReceivedEventBus());
            }

            @Override
            public void onError(int i, String s) {
//                EventBus.getDefault().post(new ChatReceivedEventBus());
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }


    /**
     * 重发消息
     *
     * @param message
     */
    public static void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 语音消息
     *
     * @param HXID     环信ID
     * @param filePath 语音路径
     * @param length   长度
     * @return
     */
    public final static void SendVoice(Context context,
                                       String HXID,
                                       String otherUid,
                                       String filePath,
                                       int length,
                                       String otherUsername,
                                       String otherUserAvatar,
                                       String otherVipLevel,
                                       String otherAuthStatus,
                                       String otherBusinessAuthStatus,
                                       String otherNamgeCardBgImage
//            ,EMCallBack callBack
    ) {
        User user = User.GetLoginedUser(context);
        //filePath为语音文件路径，length为录音时间(秒)
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, HXID);
        message.setTo(HXID);
        message.setChatType(EMMessage.ChatType.Chat);
        //设置离线推送 (仅针对小米，华为机型)
        message.setAttribute("em_force_notification", true);
//        message.setAttribute("uid", user.uid);
//        message.setAttribute("username", user.username);
//        message.setAttribute("userAvatar", user.avatar);
//        message.setAttribute("namgeCardBgImage", user.bgImage);
//        message.setAttribute("otherUid", otherUid);
//        message.setAttribute("otherUsername", otherUsername);
//        message.setAttribute("otherUserAvatar", otherUserAvatar);
//        message.setAttribute("otherNamgeCardBgImage", otherNamgeCardBgImage);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
//                EventBus.getDefault().post(new ChatReceivedEventBus());
            }

            @Override
            public void onError(int i, String s) {
//                EventBus.getDefault().post(new ChatReceivedEventBus());
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
//        message.setMessageStatusCallback(callBack);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

}
