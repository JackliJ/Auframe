package com.maiguoer.component.http.lirbrary_demo.bean;

import com.maiguoer.component.http.app.BaseRequestBean;

import java.util.List;

/**
 * Create by www.lijin@foxmail.com on 2018/12/16 0016.
 * <br/>
 */

public class UserBean extends BaseRequestBean {

    /**
     * data : {"memberInfo":{"uid":7159444,"storeId":102595,"storeName":"明年","hxname":"7159444","avatar":"http://app.img.maiguoer.com/member/2018/04/23/1524474795620954.jpg","username":"莫尼卡住了","unionLevel":0,"unionCredit":0,"vipLevel":4,"authStatus":1,"businessAuthStatus":1,"gender":1,"mobile":"1300000000","occupation":"","hobby":"","birthDate":"19921206","bornDate":"1992年12月06日","birthPeriod":"90后","constellation":"射手座","company":"神州共途信息系统有限公司","position":"测试","isSingle":3,"mobileVisible":1,"bornArea":"湖南.衡南县.衡山县","resideArea":"北京.海淀区","resideCity":"北京","bgImage":"http://app.img.maiguoer.com/member/2017/12/08/1512717328682730.jpg","bgLikeCount":"8","bgLikeStatus":1,"userNote":"","isStaff":1,"isChairman":1,"isUnionMerchant":1,"isMaker":1,"makerLevel":1,"isWelfare":0,"isFruitToken":1,"vipStatus":1,"zoneVodDuration":"60","valueLevel":0,"realname":"哈哈哈哈哈哈哈哈哈哈","fansCount":"53","followCount":"29","viewCount":"83","inviteCount":"0","flowerCount":"7","isSetSecPassword":1,"contactsCount":0,"orderCount":0,"impressionCount":2,"userAvatars":["http://app.img.maiguoer.com/member/2017/12/08/1512712174625748.jpg"],"isHost":1,"inviteUid":7159444},"showActivityUrl":""}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * memberInfo : {"uid":7159444,"storeId":102595,"storeName":"明年","hxname":"7159444","avatar":"http://app.img.maiguoer.com/member/2018/04/23/1524474795620954.jpg","username":"莫尼卡住了","unionLevel":0,"unionCredit":0,"vipLevel":4,"authStatus":1,"businessAuthStatus":1,"gender":1,"mobile":"1300000000","occupation":"","hobby":"","birthDate":"19921206","bornDate":"1992年12月06日","birthPeriod":"90后","constellation":"射手座","company":"神州共途信息系统有限公司","position":"测试","isSingle":3,"mobileVisible":1,"bornArea":"湖南.衡南县.衡山县","resideArea":"北京.海淀区","resideCity":"北京","bgImage":"http://app.img.maiguoer.com/member/2017/12/08/1512717328682730.jpg","bgLikeCount":"8","bgLikeStatus":1,"userNote":"","isStaff":1,"isChairman":1,"isUnionMerchant":1,"isMaker":1,"makerLevel":1,"isWelfare":0,"isFruitToken":1,"vipStatus":1,"zoneVodDuration":"60","valueLevel":0,"realname":"哈哈哈哈哈哈哈哈哈哈","fansCount":"53","followCount":"29","viewCount":"83","inviteCount":"0","flowerCount":"7","isSetSecPassword":1,"contactsCount":0,"orderCount":0,"impressionCount":2,"userAvatars":["http://app.img.maiguoer.com/member/2017/12/08/1512712174625748.jpg"],"isHost":1,"inviteUid":7159444}
         * showActivityUrl :
         */

        private MemberInfoBean memberInfo;
        private String showActivityUrl;

        public MemberInfoBean getMemberInfo() {
            return memberInfo;
        }

        public void setMemberInfo(MemberInfoBean memberInfo) {
            this.memberInfo = memberInfo;
        }

        public String getShowActivityUrl() {
            return showActivityUrl;
        }

        public void setShowActivityUrl(String showActivityUrl) {
            this.showActivityUrl = showActivityUrl;
        }

        public static class MemberInfoBean {
            /**
             * uid : 7159444
             * storeId : 102595
             * storeName : 明年
             * hxname : 7159444
             * avatar : http://app.img.maiguoer.com/member/2018/04/23/1524474795620954.jpg
             * username : 莫尼卡住了
             * unionLevel : 0
             * unionCredit : 0
             * vipLevel : 4
             * authStatus : 1
             * businessAuthStatus : 1
             * gender : 1
             * mobile : 1300000000
             * occupation :
             * hobby :
             * birthDate : 19921206
             * bornDate : 1992年12月06日
             * birthPeriod : 90后
             * constellation : 射手座
             * company : 神州共途信息系统有限公司
             * position : 测试
             * isSingle : 3
             * mobileVisible : 1
             * bornArea : 湖南.衡南县.衡山县
             * resideArea : 北京.海淀区
             * resideCity : 北京
             * bgImage : http://app.img.maiguoer.com/member/2017/12/08/1512717328682730.jpg
             * bgLikeCount : 8
             * bgLikeStatus : 1
             * userNote :
             * isStaff : 1
             * isChairman : 1
             * isUnionMerchant : 1
             * isMaker : 1
             * makerLevel : 1
             * isWelfare : 0
             * isFruitToken : 1
             * vipStatus : 1
             * zoneVodDuration : 60
             * valueLevel : 0
             * realname : 哈哈哈哈哈哈哈哈哈哈
             * fansCount : 53
             * followCount : 29
             * viewCount : 83
             * inviteCount : 0
             * flowerCount : 7
             * isSetSecPassword : 1
             * contactsCount : 0
             * orderCount : 0
             * impressionCount : 2
             * userAvatars : ["http://app.img.maiguoer.com/member/2017/12/08/1512712174625748.jpg"]
             * isHost : 1
             * inviteUid : 7159444
             */

            private int uid;
            private int storeId;
            private String storeName;
            private String hxname;
            private String avatar;
            private String username;
            private int unionLevel;
            private int unionCredit;
            private int vipLevel;
            private int authStatus;
            private int businessAuthStatus;
            private int gender;
            private String mobile;
            private String occupation;
            private String hobby;
            private String birthDate;
            private String bornDate;
            private String birthPeriod;
            private String constellation;
            private String company;
            private String position;
            private int isSingle;
            private int mobileVisible;
            private String bornArea;
            private String resideArea;
            private String resideCity;
            private String bgImage;
            private String bgLikeCount;
            private int bgLikeStatus;
            private String userNote;
            private int isStaff;
            private int isChairman;
            private int isUnionMerchant;
            private int isMaker;
            private int makerLevel;
            private int isWelfare;
            private int isFruitToken;
            private int vipStatus;
            private String zoneVodDuration;
            private int valueLevel;
            private String realname;
            private String fansCount;
            private String followCount;
            private String viewCount;
            private String inviteCount;
            private String flowerCount;
            private int isSetSecPassword;
            private int contactsCount;
            private int orderCount;
            private int impressionCount;
            private int isHost;
            private int inviteUid;
            private List<String> userAvatars;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getStoreId() {
                return storeId;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getHxname() {
                return hxname;
            }

            public void setHxname(String hxname) {
                this.hxname = hxname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getUnionLevel() {
                return unionLevel;
            }

            public void setUnionLevel(int unionLevel) {
                this.unionLevel = unionLevel;
            }

            public int getUnionCredit() {
                return unionCredit;
            }

            public void setUnionCredit(int unionCredit) {
                this.unionCredit = unionCredit;
            }

            public int getVipLevel() {
                return vipLevel;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            public int getAuthStatus() {
                return authStatus;
            }

            public void setAuthStatus(int authStatus) {
                this.authStatus = authStatus;
            }

            public int getBusinessAuthStatus() {
                return businessAuthStatus;
            }

            public void setBusinessAuthStatus(int businessAuthStatus) {
                this.businessAuthStatus = businessAuthStatus;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getOccupation() {
                return occupation;
            }

            public void setOccupation(String occupation) {
                this.occupation = occupation;
            }

            public String getHobby() {
                return hobby;
            }

            public void setHobby(String hobby) {
                this.hobby = hobby;
            }

            public String getBirthDate() {
                return birthDate;
            }

            public void setBirthDate(String birthDate) {
                this.birthDate = birthDate;
            }

            public String getBornDate() {
                return bornDate;
            }

            public void setBornDate(String bornDate) {
                this.bornDate = bornDate;
            }

            public String getBirthPeriod() {
                return birthPeriod;
            }

            public void setBirthPeriod(String birthPeriod) {
                this.birthPeriod = birthPeriod;
            }

            public String getConstellation() {
                return constellation;
            }

            public void setConstellation(String constellation) {
                this.constellation = constellation;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public int getIsSingle() {
                return isSingle;
            }

            public void setIsSingle(int isSingle) {
                this.isSingle = isSingle;
            }

            public int getMobileVisible() {
                return mobileVisible;
            }

            public void setMobileVisible(int mobileVisible) {
                this.mobileVisible = mobileVisible;
            }

            public String getBornArea() {
                return bornArea;
            }

            public void setBornArea(String bornArea) {
                this.bornArea = bornArea;
            }

            public String getResideArea() {
                return resideArea;
            }

            public void setResideArea(String resideArea) {
                this.resideArea = resideArea;
            }

            public String getResideCity() {
                return resideCity;
            }

            public void setResideCity(String resideCity) {
                this.resideCity = resideCity;
            }

            public String getBgImage() {
                return bgImage;
            }

            public void setBgImage(String bgImage) {
                this.bgImage = bgImage;
            }

            public String getBgLikeCount() {
                return bgLikeCount;
            }

            public void setBgLikeCount(String bgLikeCount) {
                this.bgLikeCount = bgLikeCount;
            }

            public int getBgLikeStatus() {
                return bgLikeStatus;
            }

            public void setBgLikeStatus(int bgLikeStatus) {
                this.bgLikeStatus = bgLikeStatus;
            }

            public String getUserNote() {
                return userNote;
            }

            public void setUserNote(String userNote) {
                this.userNote = userNote;
            }

            public int getIsStaff() {
                return isStaff;
            }

            public void setIsStaff(int isStaff) {
                this.isStaff = isStaff;
            }

            public int getIsChairman() {
                return isChairman;
            }

            public void setIsChairman(int isChairman) {
                this.isChairman = isChairman;
            }

            public int getIsUnionMerchant() {
                return isUnionMerchant;
            }

            public void setIsUnionMerchant(int isUnionMerchant) {
                this.isUnionMerchant = isUnionMerchant;
            }

            public int getIsMaker() {
                return isMaker;
            }

            public void setIsMaker(int isMaker) {
                this.isMaker = isMaker;
            }

            public int getMakerLevel() {
                return makerLevel;
            }

            public void setMakerLevel(int makerLevel) {
                this.makerLevel = makerLevel;
            }

            public int getIsWelfare() {
                return isWelfare;
            }

            public void setIsWelfare(int isWelfare) {
                this.isWelfare = isWelfare;
            }

            public int getIsFruitToken() {
                return isFruitToken;
            }

            public void setIsFruitToken(int isFruitToken) {
                this.isFruitToken = isFruitToken;
            }

            public int getVipStatus() {
                return vipStatus;
            }

            public void setVipStatus(int vipStatus) {
                this.vipStatus = vipStatus;
            }

            public String getZoneVodDuration() {
                return zoneVodDuration;
            }

            public void setZoneVodDuration(String zoneVodDuration) {
                this.zoneVodDuration = zoneVodDuration;
            }

            public int getValueLevel() {
                return valueLevel;
            }

            public void setValueLevel(int valueLevel) {
                this.valueLevel = valueLevel;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public String getFansCount() {
                return fansCount;
            }

            public void setFansCount(String fansCount) {
                this.fansCount = fansCount;
            }

            public String getFollowCount() {
                return followCount;
            }

            public void setFollowCount(String followCount) {
                this.followCount = followCount;
            }

            public String getViewCount() {
                return viewCount;
            }

            public void setViewCount(String viewCount) {
                this.viewCount = viewCount;
            }

            public String getInviteCount() {
                return inviteCount;
            }

            public void setInviteCount(String inviteCount) {
                this.inviteCount = inviteCount;
            }

            public String getFlowerCount() {
                return flowerCount;
            }

            public void setFlowerCount(String flowerCount) {
                this.flowerCount = flowerCount;
            }

            public int getIsSetSecPassword() {
                return isSetSecPassword;
            }

            public void setIsSetSecPassword(int isSetSecPassword) {
                this.isSetSecPassword = isSetSecPassword;
            }

            public int getContactsCount() {
                return contactsCount;
            }

            public void setContactsCount(int contactsCount) {
                this.contactsCount = contactsCount;
            }

            public int getOrderCount() {
                return orderCount;
            }

            public void setOrderCount(int orderCount) {
                this.orderCount = orderCount;
            }

            public int getImpressionCount() {
                return impressionCount;
            }

            public void setImpressionCount(int impressionCount) {
                this.impressionCount = impressionCount;
            }

            public int getIsHost() {
                return isHost;
            }

            public void setIsHost(int isHost) {
                this.isHost = isHost;
            }

            public int getInviteUid() {
                return inviteUid;
            }

            public void setInviteUid(int inviteUid) {
                this.inviteUid = inviteUid;
            }

            public List<String> getUserAvatars() {
                return userAvatars;
            }

            public void setUserAvatars(List<String> userAvatars) {
                this.userAvatars = userAvatars;
            }
        }
    }

}
