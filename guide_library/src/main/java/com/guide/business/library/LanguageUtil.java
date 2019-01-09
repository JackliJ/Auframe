package com.guide.business.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.maiguoer.component.http.utils.SharedPreferencesUtils;

import java.util.Locale;

/**
 * Created by zhangxiaodong on 2017/5/17.
 * <br/>
 * 语言工具类
 */

public class LanguageUtil {
    /**
     * 拿到当前对象的单例
     */
    private static LanguageUtil instance = new LanguageUtil();

    public static final String TAG = LanguageUtil.class.getSimpleName();
    private static final String LANGUAGE = "Language";
    private static final String COUNTRY = "country";
    private static final String SCRIPT = "script";

    private LanguageUtil() {
    }

    public static LanguageUtil getInstance() {
        return instance;
    }


    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context,
                                          Locale locale) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public Context changeAppLanguage(Context context, Locale locale) {
        saveLocale(context, locale);
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }

        return updateResourcesLegacy(context, locale);
    }

  /*  public Context changeAppLanguage(Context context, String language) {
        Locale locale;
        if("en".equals(language)){
            locale = Locale.ENGLISH;
        }else{
            locale = Locale.CHINA;
        }
        saveLocale(context, locale);
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }

        return updateResourcesLegacy(context, locale);
    }*/

    //保存地理位置信息
    private static void saveLocale(Context context, Locale locale) {
        SharedPreferences preferences = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //保存语言
        editor.putString(LANGUAGE, locale.getLanguage());
        //保存国家
        editor.putString(COUNTRY, locale.getCountry());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editor.putString(SCRIPT, locale.getScript());
        }
        editor.apply();
    }

    //获取app保存的语言
    public String getAppLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE,Locale.getDefault().getLanguage());
    }


    //语言切换
    //切换指定的语言
    public static void switchLanguage(Locale local,Context mContext){
        LanguageUtil.getInstance().changeAppLanguage2(local,mContext);

    }



    //切换app语言
    public static void changeAppLanguage2(Locale locale,Context context) {
        //保存当前语言
        SharedPreferencesUtils.saveAppLanguage(context,locale.getLanguage());
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }

    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return updateResourcesLegacy(context,getLocale(context,language));
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    public static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = getLocale(context,language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    /**
     * 通过语言获取相应的Locale
     * @param context
     * @param language
     * @return
     */
    public static Locale getLocale(Context context,String language){
        Locale locale = null;
        if("en".equals(language)){
            locale = Locale.ENGLISH;
        }else{
            locale = Locale.CHINA;
        }
        return locale;
    }
}
