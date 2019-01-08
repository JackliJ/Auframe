package com.maiguoer.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maiguoer.component.http.R;


/**
 * 自定義Toast
 * <p/>
 * Created by MyPC on 2015/8/7.
 * 由StomHong 修改
 */
public class MgeToast {

    private Toast mToast;

//    private static MgeToast sInstance;

    public static int LENGTH_LONG = Toast.LENGTH_LONG;

    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

    /**
     * 不允许在外部初始化
     */
    private MgeToast(Context context) {
        mToast = new Toast(context);
    }

    /**
     * 初始化toast
     */
    private static MgeToast newInstance(Context context) {
        return new MgeToast(context);
    }

    /**
     * 初始化给定的view
     * @param view          给定的view
     * @param duration      显示时长
     */
    private void initView(View view,int duration) {
        mToast.setView(view);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
//        mToast.setView(null);
    }

    /**
     * 初始化成功View
     *
     * @param text 显示的文字
     */
    private void initSuccessView(Context context, CharSequence text,int duration) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.toast_mge, null);
        TextView tv = (TextView) layoutView.findViewById(R.id.tv_content);
        ImageView iv = (ImageView) layoutView.findViewById(R.id.iv_image);
        tv.setText(text);
        iv.setImageResource(R.drawable.hint_icon_success);
        mToast.setView(layoutView);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setDuration(duration);
        mToast.show();
//        mToast.setView(null);
    }

    /**
     * 初始化错误View
     *
     * @param text 显示的文字
     */
    private void initErrorView(Context context, CharSequence text,int duration) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.toast_mge, null);
        TextView tv = (TextView) layoutView.findViewById(R.id.tv_content);
        ImageView iv = (ImageView) layoutView.findViewById(R.id.iv_image);
        tv.setText(text);
        iv.setImageResource(R.drawable.hint_icon_error);
        mToast.setView(layoutView);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setDuration(duration);
        mToast.show();
//        mToast.setView(null);
    }

    /**
     * 初始化等待View
     *
     * @param text 显示的文字
     */
    private void initExpectView(Context context, CharSequence text,int duration) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.toast_mge, null);
        TextView tv = (TextView) layoutView.findViewById(R.id.tv_content);
        ImageView iv = (ImageView) layoutView.findViewById(R.id.iv_image);
        tv.setText(text);
        iv.setImageResource(R.drawable.hint_icon_expect);
        mToast.setView(layoutView);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setDuration(duration);
        mToast.show();
//        mToast.setView(null);
    }

    /**
     * 初始化成功toast,不指定时长
     *
     * @param context
     * @param text     显示的文字
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    private static void initSuccessToast(Context context, CharSequence text, int duration) {
        newInstance(context).initSuccessView(context.getApplicationContext(), text,duration);
    }

    /**
     * 初始化等待toast,不指定时长
     *
     * @param context
     * @param text     显示的文字
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    private static void initExpectToast(Context context, CharSequence text, int duration) {
        newInstance(context).initExpectView(context, text,duration);
    }

    /**
     * 初始化错误toast,不指定时长
     *
     * @param context
     * @param text     显示的文字
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    private static void initErrorToast(Context context, CharSequence text, int duration) {
        newInstance(context).initErrorView(context, text,duration);
    }

    /**
     * 显示成功toast,不指定时长
     *
     * @param context
     * @param text     显示的文字
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showSuccessToast(Context context, CharSequence text, int duration) {
        initSuccessToast(context, text, duration);
    }

    /**
     * 显示成功toast,不指定时长
     *
     * @param context
     * @param resId     显示的文字资源id
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showSuccessToast(Context context, int resId, int duration) {
        showSuccessToast(context, context.getResources().getString(resId), duration);
    }

    /**
     * 显示等待toast,不指定时长
     *
     * @param context
     * @param text     显示的文字
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showExpectToast(Context context, CharSequence text, int duration) {
        initExpectToast(context, text, duration);
    }

    /**
     * 显示等待toast,不指定时长
     *
     * @param context
     * @param resId     显示的文字资源id
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showExpectToast(Context context,int resId, int duration) {
        showExpectToast(context, context.getResources().getString(resId), duration);
    }

    /**
     * 显示错误toast,不指定时长
     *
     * @param context
     * @param text     显示的文字
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showErrorToast(Context context, CharSequence text, int duration) {
        initErrorToast(context, text, duration);
    }

    /**
     * 显示错误toast,不指定时长
     *
     * @param context
     * @param resId     显示的文字资源id
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showErrorToast(Context context, int resId, int duration) {
        showErrorToast(context, context.getResources().getString(resId), duration);
    }

    /**
     * 显示成功toast，默认时长为Toast.LENGTH_SHORT
     *
     * @param context
     * @param text    显示的文字
     */
    public static void showSuccessToast(Context context, CharSequence text) {
        showSuccessToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示成功toast，默认时长为Toast.LENGTH_SHORT
     *
     * @param context
     * @param resId    显示的文字资源id
     */
    public static void showSuccessToast(Context context, int resId) {
        showSuccessToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示等待toast，默认时长为Toast.LENGTH_SHORT
     *
     * @param context
     * @param text    显示的文字
     */
    public static void showExpectToast(Context context, CharSequence text) {
        showExpectToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示等待toast，默认时长为Toast.LENGTH_SHORT
     *
     * @param context
     * @param resId    显示的文字资源id
     */
    public static void showExpectToast(Context context, int resId) {
        showExpectToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示错误toast，默认时长为Toast.LENGTH_SHORT
     *
     * @param context
     * @param text    显示的文字
     */
    public static void showErrorToast(Context context, CharSequence text) {
        showErrorToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示错误toast    指定时长
     *
     * @param context
     * @param resId     显示的文字资源id
     */
    public static void showErrorToast(Context context, int resId) {
        showErrorToast(context, resId,Toast.LENGTH_SHORT);
    }

    /**
     * 显示指定view的toast
     *
     * @param context
     * @param view     显示的view
     * @param duration 时长 Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showToast(Context context, View view, int duration) {
        newInstance(context).initView(view,duration);
    }

    /**
     * 显示指定view的toast ，默认时长为Toast.LENGTH_SHORT
     *
     * @param context
     * @param view    显示的view
     */
    public static void showToast(Context context, View view) {
        showToast(context, view, Toast.LENGTH_SHORT);
    }

    /**
     * 显示不指定类型的toast
     *
     * @param context
     * @param text     显示的内容
     * @param duration 时长
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

}
