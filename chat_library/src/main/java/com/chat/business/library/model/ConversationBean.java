package com.chat.business.library.model;

import com.hyphenate.chat.EMMessage;


/**
 * Created by www.lijin@foxmail.com on 2017/9/25 0025.
 * <br/>
 * 消息实体
 */
public class ConversationBean {


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String uid;
    private int userLeavl;//用户VIP级别
    private String usernote;//用户备注
    private String username;//用户昵称
    private String useravatar;//用户头像
    private String usercontext;//最后一条消息
    private int userunread;//未读消息数量
    private long endtime;//会话最后一条消息的时间
    private String uuid;//用户的环信ID
    private String type;//消息类型
    private String authStatus;//用户实名认证状态
    private String businessAuthStatus;//用户企业认证状态
    private String otherNamgeCardBgImage;//对方用户名片背景图片
    private boolean isReceiveSend;//是发送方(true) 还是 接收方(false)
    private EMMessage.ChatType chatType;//聊天类型
    private String TextType;//文本扩展类型
    private int userTopStatus;//是否置顶 0没有置顶 1置顶
    private long userTopTime;//被置顶的时间

    public long getUserTopTime() {
        return userTopTime;
    }

    public void setUserTopTime(long userTopTime) {
        this.userTopTime = userTopTime;
    }

    public int getUserTopStatus() {
        return userTopStatus;
    }

    public void setUserTopStatus(int userTopStatus) {
        this.userTopStatus = userTopStatus;
    }

    public EMMessage.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(EMMessage.ChatType chatType) {
        this.chatType = chatType;
    }

    public String getTextType() {
        return TextType;
    }

    public void setTextType(String textType) {
        TextType = textType;
    }

    public boolean isReceiveSend() {
        return isReceiveSend;
    }

    public void setReceiveSend(boolean receiveSend) {
        isReceiveSend = receiveSend;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOtherNamgeCardBgImage() {
        return otherNamgeCardBgImage;
    }

    public void setOtherNamgeCardBgImage(String otherNamgeCardBgImage) {
        this.otherNamgeCardBgImage = otherNamgeCardBgImage;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getBusinessAuthStatus() {
        return businessAuthStatus;
    }

    public void setBusinessAuthStatus(String businessAuthStatus) {
        this.businessAuthStatus = businessAuthStatus;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getUsernote() {
        return usernote;
    }

    public void setUsernote(String usernote) {
        this.usernote = usernote;
    }


    public int getUserLeavl() {
        return userLeavl;
    }

    public void setUserLeavl(int userLeavl) {
        this.userLeavl = userLeavl;
    }


    public String getUsercontext() {
        return usercontext;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUsercontext(String usercontext) {
        this.usercontext = usercontext;
    }

    public int getUserunread() {
        return userunread;
    }

    public void setUserunread(int userunread) {
        this.userunread = userunread;
    }


}
