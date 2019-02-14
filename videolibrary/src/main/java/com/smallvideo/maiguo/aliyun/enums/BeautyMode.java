package com.smallvideo.maiguo.aliyun.enums;

/**
 * 美颜模式
 */
public enum BeautyMode {
    /**
     * 普通
     */
    Normal(0),

    /**
     * 高级
     */
    Advanced(1);

    private int value;

    BeautyMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
