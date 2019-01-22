package com.chat.business.library.eventbus;

/**
 * Created by Sky on 2017/11/7.
 * <br/>
 * 消息数量显示Event
 */

public class ShowMainMessageCountEvent {

    public int messageCount;

    public ShowMainMessageCountEvent(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getMessageCount() {
        return messageCount;
    }
}
