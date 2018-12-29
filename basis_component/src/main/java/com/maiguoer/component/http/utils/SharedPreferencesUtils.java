package com.maiguoer.component.http.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.maiguoer.component.http.BuildConfig;

/**
 * SharedPreferences 帮助类
 * Create by www.lijin@foxmail.com on 2018/12/16 0016.
 * <br/>
 */

public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 保存登录的用户id
     *
     * @param context
     * @param uid
     * @return
     */
    public static boolean SaveLoginUid(Context context, int uid) {
        boolean isLoginSave = false;
        try {
            setParam(context, "login_uid", uid);
            isLoginSave = true;
        } catch (Exception e) {
            e.printStackTrace();
            isLoginSave = false;
        }
        return isLoginSave;
    }


    /**
     * 获得现在登录的用户ID
     *
     * @param context
     * @return 没有的话返回-1
     */
    public static int GetLoginUid(Context context) {
        Object uid = getParam(context, "login_uid", 0);
        return (int) uid;
    }


    /**
     * 保存接口返回的Token
     *
     * @param context
     * @param token
     */
    public static void SaveAppToken(Context context, String token) {
        setParam(context, "app_token", token);
    }

    /**
     * 获取Token，用于网络请求，如果没有则""
     *
     * @param context
     * @return
     */
    public static String GetAppToken(Context context) {
        return (String) getParam(context, "app_token", "");
    }


    /**
     * 保存用户选取的语言环境
     *
     * @param context
     * @return
     */
    public static void SaveLanguage(Context context, int id) {
        setParam(context, "languageID", id);
    }

    /**
     * 获取用户选取的语言版本
     *
     * @param context
     * @return
     */
    public static int getLanguage(Context context) {
        int appLang;
        if ((Integer) getParam(context, "languageID", 0) != 1) {
            appLang = HttpConfig.APP_LANG_CN;
        } else {
            appLang = HttpConfig.APP_LANG_EN;
        }
        SaveLanguage(context, appLang);
        return appLang;
    }

    /**
     * 保存登录用户头像
     *
     * @param context
     * @param userAvatar 头像地址
     */
    public static void SaveLoginUserAvatar(Context context, String userAvatar) {
        setParam(context, "login_avatar", userAvatar);
    }

    /**
     * 获取之前登录用户的头像
     *
     * @param context
     * @return
     */
    public static String GetLoginUserAvatar(Context context) {
        return (String) getParam(context, "login_avatar", "");
    }


    /**
     * 保存最后一次打开客户端时候的版本号
     *
     * @param context
     */
    public static void SaveLastApplicationVersion(Context context) {
        setParam(context, "last_application_version", BuildConfig.VERSION_CODE);
    }

    /**
     * 获取最后一次打开客户端的版本号
     *
     * @param context
     */
    public static int getLastApplicationVersion(Context context) {
        return (Integer) getParam(context, "last_application_version", -1);
    }


}
