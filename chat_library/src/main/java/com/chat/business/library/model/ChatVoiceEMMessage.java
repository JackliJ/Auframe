package com.chat.business.library.model;

import com.hyphenate.chat.EMMessage;

import java.io.Serializable;

/**
 * Create by www.lijin@foxmail.com on 2017/12/18 0018.
 * <br/>
 * 用于保存播放语音的数组
 */

public class ChatVoiceEMMessage implements Serializable {

    private EMMessage emMessage;
    private int position;

    public EMMessage getEmMessage() {
        return emMessage;
    }

    public void setEmMessage(EMMessage emMessage) {
        this.emMessage = emMessage;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
