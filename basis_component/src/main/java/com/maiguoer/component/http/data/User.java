package com.maiguoer.component.http.data;

import android.content.Context;

import com.maiguoer.component.http.utils.SharedPreferencesUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 数据库User表（用户表）
 * Create by www.lijin@foxmail.com on 2018/12/20 0020.
 * <br/>
 */
@Entity
public class User {
    /**
     * 用户Id
     */
    @Id
    @Property(nameInDb = "uid")
    public int uid;

    /**
     * 店铺id
     */
    @Property(nameInDb = "storeId")
    public int storeId;

    /**
     * 环信用户名
     */
    @Property(nameInDb = "hxname")
    public String hxname;

    /**
     * 用户名
     */
    @Property(nameInDb = "username")
    public String username;

    /**
     * vip等级
     */
    @Property(nameInDb = "vipLevel")
    public int vipLevel;

    /**
     * 公司
     */
    @Property(nameInDb = "company")
    public String company;

    /**
     * 职位
     */
    @Property(nameInDb = "position")
    public String position;

    /**
     * 性别
     * <ul>
     * <li>1.男</li>
     * <li>2.女</li>
     * </ul>
     */
    @Property(nameInDb = "gender")
    public int gender;

    /**
     * 生日       例如：1980年01月01日
     */
    @Property(nameInDb = "bornDate")
    public String bornDate;

    /**
     * 星座
     */
    @Property(nameInDb = "constellation")
    public String constellation;

    /**
     * 电话是否公开
     * <ul>
     * <li>0.隐藏</li>
     * <li>1.公开/li>
     * </ul>
     */
    @Property(nameInDb = "mobileVisible")
    public int mobileVisible;

    /**
     * 头像
     */
    @Property(nameInDb = "avatar")
    public String avatar;

    /**
     * 是否单身
     * <ul>
     * <li>1.单身</li>
     * <li>2.非单身</li>
     * <li>3.保密</li>
     * </ul>
     */
    @Property(nameInDb = "isSingle")
    public int isSingle;

    /**
     * 行业
     */
    @Property(nameInDb = "occupation")
    public String occupation;

    /**
     * 兴趣
     */
    @Property(nameInDb = "hobby")
    public String hobby;

    /**
     * 电话
     */
    @Property(nameInDb = "mobile")
    public String mobile;

    /**
     * 家乡
     */
    @Property(nameInDb = "bornArea")
    public String bornArea;

    /**
     * 年龄段
     * <p/>
     * 例:90后 85后
     */
    @Property(nameInDb = "birthPeriod")
    public String birthPeriod;

    /**
     * 背景图点赞数
     * <p/>
     * 如果超出1万则单位成万，如 1.2万，保留一位小数
     */
    @Property(nameInDb = "bgLikeCount")
    public String bgLikeCount;

    /**
     * 背景图
     */
    @Property(nameInDb = "bgImage")
    public String bgImage;

    /**
     * 实名认证状态
     * <ul>
     * <li>0.审核中 </li>
     * <li>1.通过/li>
     * <li>2.未通过/li>
     * </ul>
     */
    @Property(nameInDb = "authStatus")
    public int authStatus;

    /**
     * 居住地
     */
    @Property(nameInDb = "resideArea")
    public String resideArea;
    /**
     * 居住城市
     */
    @Property(nameInDb = "resideCity")
    public String resideCity;
    /**
     * 点赞状态
     * <ul>
     * <li>0.不能点赞</li>
     * <li>1.可以点赞</li>
     * </ul>
     */
    @Property(nameInDb = "bgLikeStatus")
    public int bgLikeStatus;

    /**
     * 真实姓名
     */
    @Property(nameInDb = "realname")
    public String realname;

    /**
     * 商户认证
     * <ul>
     * <li>0.审核中 </li>
     * <li>1.通过/li>
     * <li>2.未通过/li>
     * </ul>
     */
    @Property(nameInDb = "businessAuthStatus")
    public int businessAuthStatus;

    /**
     * 联盟等级
     * <ul>
     * <li>0.为非联盟成员</li>
     * </ul>
     */
    @Property(nameInDb = "unionLevel")
    public int unionLevel;

    /**
     * 是否设置二级密码	int	例如
     * <ul>
     * <li>1.已设置</li>
     * <li>0.未设置</li>
     * </ul>
     */
    @Property(nameInDb = "isSetSecPassword")
    public int isSetSecPassword;


    /**
     * 通讯录数量
     */
    @Property(nameInDb = "contactsCount")
    public int contactsCount;

    /**
     * 订单数量
     */
    @Property(nameInDb = "orderCount")
    public int orderCount;

    /**
     * 是否专职员工
     * <ul>
     * <li>0.不是</li>
     * <li>1.是</li>
     * </ul>
     */
    @Property(nameInDb = "isStaff")
    public int isStaff;

    /**
     * 联盟积分 默认 0
     */
    @Property(nameInDb = "unionCredit")
    public int unionCredit;

    /**
     * 关注数
     */
    @Property(nameInDb = "followCount")
    public String followCount;

    /**
     * 粉丝数
     */
    @Property(nameInDb = "fansCount")
    public String fansCount;

    /**
     * 访客数
     */
    @Property(nameInDb = "viewCount")
    public String viewCount;

    /**
     * 人脉数
     */
    @Property(nameInDb = "inviteCount")
    public String inviteCount;

    /**
     * 送花数
     */
    @Property(nameInDb = "flowerCount")
    public String flowerCount;

    /**
     * 评论印象总数
     */
    @Property(nameInDb = "impressionCount")
    public int impressionCount;

    /**
     * 评论印象前三头像
     */
    @Property(nameInDb = "userAvatars")
    public String userAvatars;
    /**
     * 是否发改委：0 不是 1 是
     */
    @Property(nameInDb = "chairman")
    public int chairman;
    /**
     * 是否联盟商户：0 不是 1 是
     */
    @Property(nameInDb = "unionMerchant")
    public int unionMerchant;
    /**
     * 是否福利	int	0 不是 1 是
     */
    @Property(nameInDb = "isWelfare")
    public int isWelfare;
    /**
     * 是否果儿令	int	0：否；1：是
     */
    @Property(nameInDb = "isFruitToken")
    public int isFruitToken;
    /**
     * 价值等级
     */
    @Property(nameInDb = "valueLevel")
    public int valueLevel;
    /**
     * 录制动态视频时长	int	单位:秒
     */
    @Property(nameInDb = "zoneVodDuration")
    public int zoneVodDuration;
    /**
     * 是否1v1发布者	int 0否 1是
     */
    @Property(nameInDb = "isHost")
    public int isHost;
    /**
     * 是否创客 int 0：否；1：是
     */
    @Property(nameInDb = "isMaker")
    public int isMaker;
    /**
     * 是否 天使旅行家
     */
    @Property(nameInDb = "showActivityUrl")
    public String showActivityUrl;
    /**
     * VIP状态  0 未激活  1 已激活  2 已过期
     */
    @Property(nameInDb = "vipStatus")
    public int vipStatus;
    /**
     * 推荐ID或原导师ID
     */
    @Property(nameInDb = "inviteUid")
    public int inviteUid;
    /**
     * 创客等级
     */
    @Property(nameInDb = "makerLevel")
    public int makerLevel;

    /**
     * 获得正在登录的用户信息
     *
     * @param context
     * @return 没有登录则返回null
     */
    public static User GetLoginedUser(Context context) {
        int uid = SharedPreferencesUtils.GetLoginUid(context);
        if (uid <= 0)
            return null;
        return null;
    }


    @Generated(hash = 452260631)
    public User(int uid, int storeId, String hxname, String username, int vipLevel,
            String company, String position, int gender, String bornDate,
            String constellation, int mobileVisible, String avatar, int isSingle,
            String occupation, String hobby, String mobile, String bornArea,
            String birthPeriod, String bgLikeCount, String bgImage, int authStatus,
            String resideArea, String resideCity, int bgLikeStatus, String realname,
            int businessAuthStatus, int unionLevel, int isSetSecPassword,
            int contactsCount, int orderCount, int isStaff, int unionCredit,
            String followCount, String fansCount, String viewCount,
            String inviteCount, String flowerCount, int impressionCount,
            String userAvatars, int chairman, int unionMerchant, int isWelfare,
            int isFruitToken, int valueLevel, int zoneVodDuration, int isHost,
            int isMaker, String showActivityUrl, int vipStatus, int inviteUid,
            int makerLevel) {
        this.uid = uid;
        this.storeId = storeId;
        this.hxname = hxname;
        this.username = username;
        this.vipLevel = vipLevel;
        this.company = company;
        this.position = position;
        this.gender = gender;
        this.bornDate = bornDate;
        this.constellation = constellation;
        this.mobileVisible = mobileVisible;
        this.avatar = avatar;
        this.isSingle = isSingle;
        this.occupation = occupation;
        this.hobby = hobby;
        this.mobile = mobile;
        this.bornArea = bornArea;
        this.birthPeriod = birthPeriod;
        this.bgLikeCount = bgLikeCount;
        this.bgImage = bgImage;
        this.authStatus = authStatus;
        this.resideArea = resideArea;
        this.resideCity = resideCity;
        this.bgLikeStatus = bgLikeStatus;
        this.realname = realname;
        this.businessAuthStatus = businessAuthStatus;
        this.unionLevel = unionLevel;
        this.isSetSecPassword = isSetSecPassword;
        this.contactsCount = contactsCount;
        this.orderCount = orderCount;
        this.isStaff = isStaff;
        this.unionCredit = unionCredit;
        this.followCount = followCount;
        this.fansCount = fansCount;
        this.viewCount = viewCount;
        this.inviteCount = inviteCount;
        this.flowerCount = flowerCount;
        this.impressionCount = impressionCount;
        this.userAvatars = userAvatars;
        this.chairman = chairman;
        this.unionMerchant = unionMerchant;
        this.isWelfare = isWelfare;
        this.isFruitToken = isFruitToken;
        this.valueLevel = valueLevel;
        this.zoneVodDuration = zoneVodDuration;
        this.isHost = isHost;
        this.isMaker = isMaker;
        this.showActivityUrl = showActivityUrl;
        this.vipStatus = vipStatus;
        this.inviteUid = inviteUid;
        this.makerLevel = makerLevel;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public int getUid() {
        return this.uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getStoreId() {
        return this.storeId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public String getHxname() {
        return this.hxname;
    }
    public void setHxname(String hxname) {
        this.hxname = hxname;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getVipLevel() {
        return this.vipLevel;
    }
    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
    public String getCompany() {
        return this.company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public int getGender() {
        return this.gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public String getBornDate() {
        return this.bornDate;
    }
    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }
    public String getConstellation() {
        return this.constellation;
    }
    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }
    public int getMobileVisible() {
        return this.mobileVisible;
    }
    public void setMobileVisible(int mobileVisible) {
        this.mobileVisible = mobileVisible;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public int getIsSingle() {
        return this.isSingle;
    }
    public void setIsSingle(int isSingle) {
        this.isSingle = isSingle;
    }
    public String getOccupation() {
        return this.occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public String getHobby() {
        return this.hobby;
    }
    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getBornArea() {
        return this.bornArea;
    }
    public void setBornArea(String bornArea) {
        this.bornArea = bornArea;
    }
    public String getBirthPeriod() {
        return this.birthPeriod;
    }
    public void setBirthPeriod(String birthPeriod) {
        this.birthPeriod = birthPeriod;
    }
    public String getBgLikeCount() {
        return this.bgLikeCount;
    }
    public void setBgLikeCount(String bgLikeCount) {
        this.bgLikeCount = bgLikeCount;
    }
    public String getBgImage() {
        return this.bgImage;
    }
    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }
    public int getAuthStatus() {
        return this.authStatus;
    }
    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
    public String getResideArea() {
        return this.resideArea;
    }
    public void setResideArea(String resideArea) {
        this.resideArea = resideArea;
    }
    public String getResideCity() {
        return this.resideCity;
    }
    public void setResideCity(String resideCity) {
        this.resideCity = resideCity;
    }
    public int getBgLikeStatus() {
        return this.bgLikeStatus;
    }
    public void setBgLikeStatus(int bgLikeStatus) {
        this.bgLikeStatus = bgLikeStatus;
    }
    public String getRealname() {
        return this.realname;
    }
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public int getBusinessAuthStatus() {
        return this.businessAuthStatus;
    }
    public void setBusinessAuthStatus(int businessAuthStatus) {
        this.businessAuthStatus = businessAuthStatus;
    }
    public int getUnionLevel() {
        return this.unionLevel;
    }
    public void setUnionLevel(int unionLevel) {
        this.unionLevel = unionLevel;
    }
    public int getIsSetSecPassword() {
        return this.isSetSecPassword;
    }
    public void setIsSetSecPassword(int isSetSecPassword) {
        this.isSetSecPassword = isSetSecPassword;
    }
    public int getContactsCount() {
        return this.contactsCount;
    }
    public void setContactsCount(int contactsCount) {
        this.contactsCount = contactsCount;
    }
    public int getOrderCount() {
        return this.orderCount;
    }
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
    public int getIsStaff() {
        return this.isStaff;
    }
    public void setIsStaff(int isStaff) {
        this.isStaff = isStaff;
    }
    public int getUnionCredit() {
        return this.unionCredit;
    }
    public void setUnionCredit(int unionCredit) {
        this.unionCredit = unionCredit;
    }
    public String getFollowCount() {
        return this.followCount;
    }
    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }
    public String getFansCount() {
        return this.fansCount;
    }
    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }
    public String getViewCount() {
        return this.viewCount;
    }
    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }
    public String getInviteCount() {
        return this.inviteCount;
    }
    public void setInviteCount(String inviteCount) {
        this.inviteCount = inviteCount;
    }
    public String getFlowerCount() {
        return this.flowerCount;
    }
    public void setFlowerCount(String flowerCount) {
        this.flowerCount = flowerCount;
    }
    public int getImpressionCount() {
        return this.impressionCount;
    }
    public void setImpressionCount(int impressionCount) {
        this.impressionCount = impressionCount;
    }
    public String getUserAvatars() {
        return this.userAvatars;
    }
    public void setUserAvatars(String userAvatars) {
        this.userAvatars = userAvatars;
    }
    public int getChairman() {
        return this.chairman;
    }
    public void setChairman(int chairman) {
        this.chairman = chairman;
    }
    public int getUnionMerchant() {
        return this.unionMerchant;
    }
    public void setUnionMerchant(int unionMerchant) {
        this.unionMerchant = unionMerchant;
    }
    public int getIsWelfare() {
        return this.isWelfare;
    }
    public void setIsWelfare(int isWelfare) {
        this.isWelfare = isWelfare;
    }
    public int getIsFruitToken() {
        return this.isFruitToken;
    }
    public void setIsFruitToken(int isFruitToken) {
        this.isFruitToken = isFruitToken;
    }
    public int getValueLevel() {
        return this.valueLevel;
    }
    public void setValueLevel(int valueLevel) {
        this.valueLevel = valueLevel;
    }
    public int getZoneVodDuration() {
        return this.zoneVodDuration;
    }
    public void setZoneVodDuration(int zoneVodDuration) {
        this.zoneVodDuration = zoneVodDuration;
    }
    public int getIsHost() {
        return this.isHost;
    }
    public void setIsHost(int isHost) {
        this.isHost = isHost;
    }
    public int getIsMaker() {
        return this.isMaker;
    }
    public void setIsMaker(int isMaker) {
        this.isMaker = isMaker;
    }
    public String getShowActivityUrl() {
        return this.showActivityUrl;
    }
    public void setShowActivityUrl(String showActivityUrl) {
        this.showActivityUrl = showActivityUrl;
    }
    public int getVipStatus() {
        return this.vipStatus;
    }
    public void setVipStatus(int vipStatus) {
        this.vipStatus = vipStatus;
    }
    public int getInviteUid() {
        return this.inviteUid;
    }
    public void setInviteUid(int inviteUid) {
        this.inviteUid = inviteUid;
    }
    public int getMakerLevel() {
        return this.makerLevel;
    }
    public void setMakerLevel(int makerLevel) {
        this.makerLevel = makerLevel;
    }
  
   
}
