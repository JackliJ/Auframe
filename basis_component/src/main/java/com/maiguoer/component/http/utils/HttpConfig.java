package com.maiguoer.component.http.utils;

import android.content.Context;

import com.blankj.utilcode.util.EncryptUtils;
import com.maiguoer.component.http.BuildConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用于配置基础网络参数的帮助类
 * Create by www.lijin@foxmail.com on 2018/12/16 0016.
 * <br/>
 */

public class HttpConfig {

    /**
     * 域名
     */
    //http
    public static final String BASE_URL = BuildConfig.API_HOST;
    //https
    public static final String BASE_URL_S = BuildConfig.API_HOST_S;

    public static String HTTP_URL = BASE_URL;

    public static final int HTTP_TYPE = 1; // 1 http 2 https

    public static final int APP_LANG_CN = 0;//0.简体中文
    public static final String APP_LANG_CN_STR = "";//.简体中文 暂时为空

    public static final int APP_LANG_EN = 1;//1.English
    public static final String APP_LANG_EN_STR = "_en";//_en.English

    /**
     * 设置当前设置的Http类型
     */
    public static void setHttpType() {
        if (HTTP_TYPE == 1) {
            HTTP_URL = BASE_URL;
        } else if (HTTP_TYPE == 2) {
            HTTP_URL = BASE_URL;
        }
    }

    /**
     * 封装网络请求通用参数
     *
     * @param path 请求地址，去掉域名
     * @return
     */
    public static Map<String, String> Builder(Context mContext, String path) {
        int uid = SharedPreferencesUtils.GetLoginUid(mContext);
        long timestamp = System.currentTimeMillis() / 1000;
        String token = SharedPreferencesUtils.GetAppToken(mContext);
        String uuid = UUID.randomUUID().toString();
        Map<String, String> params = new LinkedHashMap<>();
        String sign = "clientType=" + 0 + "&lang=" + SharedPreferencesUtils.getLanguage(mContext) + "&network=" + Utils.GetNetworkStatus(mContext) +
                "&timestamp=" + timestamp + "&uid=" + uid + "&version=" + BuildConfig.VERSION_NAME + "token=" + token +
                "uuid=" + uuid + "action=" + path;
        params.put("uid", String.valueOf(uid));
        params.put("version", BuildConfig.VERSION_NAME);
        params.put("clientType", "0");
        params.put("network", String.valueOf(Utils.GetNetworkStatus(mContext)));
        params.put("lang", String.valueOf(SharedPreferencesUtils.getLanguage(mContext)));
        params.put("timestamp", String.valueOf(timestamp));
        params.put("sign", Utils.stringToMD5(sign));
        params.put("uuid", uuid);
        return params;
    }
}
