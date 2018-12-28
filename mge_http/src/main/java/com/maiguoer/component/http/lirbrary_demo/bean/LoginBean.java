package com.maiguoer.component.http.lirbrary_demo.bean;

import com.maiguoer.component.http.app.BaseRequestBean;

/**
 * 登录bean
 * Create by www.lijin@foxmail.com on 2018/12/16 0016.
 * <br/>
 */

public class LoginBean extends BaseRequestBean {

    private LoginDataBean data;

    public LoginDataBean getData() {
        return data;
    }

    public void setData(LoginDataBean data) {
        this.data = data;
    }

    public static class LoginDataBean {

        private loginInfoData loginInfo;

        public loginInfoData getLoginInfo() {
            return loginInfo;
        }

        public void setLoginInfo(loginInfoData loginInfo) {
            this.loginInfo = loginInfo;
        }

        public static class loginInfoData {
            private int uid;
            private String avatar;
            private String hxname;
            private String hxpwd;
            private int hasPut;
            private int isSetSecPassword;
            private int authStatus;
            private int businessAuthStatus;
            private String datetime;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getHxname() {
                return hxname;
            }

            public void setHxname(String hxname) {
                this.hxname = hxname;
            }

            public String getHxpwd() {
                return hxpwd;
            }

            public void setHxpwd(String hxpwd) {
                this.hxpwd = hxpwd;
            }

            public int getHasPut() {
                return hasPut;
            }

            public void setHasPut(int hasPut) {
                this.hasPut = hasPut;
            }

            public int getIsSetSecPassword() {
                return isSetSecPassword;
            }

            public void setIsSetSecPassword(int isSetSecPassword) {
                this.isSetSecPassword = isSetSecPassword;
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

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }
        }
    }
}
