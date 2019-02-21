package com.smallvideo.maiguo.aliyun.bean;

/**
 * Created by zhangxiaodong on 2018/10/31 15:52.
 * <br/>
 * 我的购买、我的店铺选择后发送的eventbus实体封装
 */

public class GoodBeanEvent {
    /**
     * 商品id
     */
    private int id;
    /**
     *商品名称
     */
    private String goodsName;
    /**
     *商品规格
     */
    private String specContent;
    /**
     *价格
     */
    private String price;
    /**
     *PV
     */
    private String backPV;
    /**
     *图片url
     */
    private String image;
    /**
     *是否标志已删除
     */
    private boolean isDel;
    /**
     *分享佣金
     */
    private String shareCommission;

    public GoodBeanEvent(int id, String goodsName, String specContent, String price, String backPV, String image, String shareCommission){
        this.id = id;
        this.goodsName = goodsName;
        this.specContent = specContent;
        this.price = price;
        this.backPV = backPV;
        this.image = image;
        this.shareCommission = shareCommission;
    }
    public GoodBeanEvent(int id, String image){
        this.id = id;
        this.image = image;
    }

    public String getShareCommission() {
        return shareCommission;
    }

    public int getId() {
        return id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getSpecContent() {
        return specContent;
    }

    public String getPrice() {
        return price;
    }

    public String getBackPV() {
        return backPV;
    }

    public String getImage() {
        return image;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }
}
