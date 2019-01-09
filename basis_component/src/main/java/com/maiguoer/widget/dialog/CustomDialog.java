package com.maiguoer.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.maiguoer.component.http.R;
import com.maiguoer.widget.ListViewForScrollView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;


/**
 * 自定义通用dialog,继承系统Dialog
 * Created by Jungle on 2017/5/24.
 */
public class CustomDialog extends Dialog implements AdapterView.OnItemClickListener {
    /**
     * 列表类型
     */
    public static final int MODE_LIST = 0;
    /**
     * 消息类型
     */
    public static final int MODE_MESSAGE = 1;
    /**
     * 自定义View
     */
    public static final int MODE_CUSTOM = 2;
    /**
     * 确认按键
     */
    public TextView vTvConfirm;
    /**
     * 取消按键
     */
    public TextView vTvCancel;
    /**
     * 标题容器
     */
    LinearLayout vLayoutTitleContainer;
    /**
     * 自定义视图容器
     */
    FrameLayout vLayoutContainer;
    /**
     * 标题控件
     */
    TextView vTvTitle;
    /**
     * 分割线
     */
    View vDivider;
    /**
     * 消息模式，消息控件
     */
    TextView vTvMessage;
    /**
     * 确认取消按键容器
     */
    LinearLayout vLayoutBtnsContainer;
    /**
     * 列表模式下，用于最为item容器
     */
    ListViewForScrollView vLv;
    /**
     * dilog最外层布局
     */
    AutoLinearLayout vAutoLinearlayout;
    /**
     * 自定义对话框构造参数
     */
    private Params mParams;
    /**
     * 内部持有实例
     */
    private CustomDialog dialog;
    private SimpleAdapter adapter;

    private CustomDialog(@NonNull Context context) {
        this(context, R.style.CustomStyleDialog);
    }

    private CustomDialog(@NonNull Context context, Params params) {
        this(context, R.style.CustomStyleDialog);
        this.mParams = params;
    }

    private CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_customdialog_root);

        vTvConfirm = (TextView) findViewById(R.id.tv_custom_dialog_confirm);
        vTvCancel = (TextView) findViewById(R.id.tv_custom_dialog_cancel);
        vLayoutTitleContainer = (LinearLayout) findViewById(R.id.layout_custom_dialog_title_container);
        vLayoutContainer = (FrameLayout) findViewById(R.id.layout_custom_dialog_container);
        vTvTitle = (TextView) findViewById(R.id.tv_custom_dialog_title);
        vDivider = (View) findViewById(R.id.view_custom_dialog_divider);
        vTvMessage = (TextView) findViewById(R.id.tv_custom_dialog_message);
        vLayoutBtnsContainer = (LinearLayout) findViewById(R.id.layout_btn_container);
        vLv = (ListViewForScrollView) findViewById(R.id.lv_custom_dialog);
        vAutoLinearlayout = (AutoLinearLayout) findViewById(R.id.dialog_layout);


        switch (mParams.mode) {
            //列表模式
            case MODE_LIST:
                if (mParams.itemDisplays != null && mParams.itemDisplays.size() > 0) {
                    adapter = new SimpleAdapter(getContext(), mParams.itemDisplays, mParams.callback, !TextUtils.isEmpty(mParams.title), (!TextUtils.isEmpty(mParams.confirmDisplay) || !TextUtils.isEmpty(mParams.cancelDisplay)));
                    vLv.setAdapter(adapter);
                    vLv.setOnItemClickListener(this);
                }
//                if (mParams.adapter != null) {
//                    vLv.setAdapter(mParams.adapter);
//                }
                vLv.setVisibility(View.VISIBLE);
                break;

            case MODE_MESSAGE:
                if (mParams.isSingleLineMsg) {
                    vTvMessage.setGravity(Gravity.CENTER);
                    vTvMessage.setEllipsize(TextUtils.TruncateAt.END);
                    vTvMessage.setSingleLine();
                    vTvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    LogUtils.d(getContext().getResources().getDimension(R.dimen.sp_15) + "");
                    vTvMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.sp_15));
                }
                if (mParams.isMessageAutoLink) {
                    vTvMessage.setAutoLinkMask(Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
                }
                if (!TextUtils.isEmpty(mParams.messageSpanned)) {
                    vTvMessage.setText(mParams.messageSpanned);
                } else if (!TextUtils.isEmpty(mParams.message)) {
//                    int width = getContext().getResources().getDisplayMetrics().widthPixels;
//                    ViewGroup.LayoutParams params = vTvMessage.getLayoutParams();
//                    params.width = width * 2 / 3;
//                    vTvMessage.setLayoutParams(params);
                    vTvMessage.setText(mParams.message);
                    //消息是否已居中显示
                    if (mParams.isMessageCenter) {
                        vTvMessage.setGravity(Gravity.CENTER);
                        vTvMessage.setEllipsize(TextUtils.TruncateAt.END);
                        vTvMessage.setSingleLine();
                        LogUtils.d(getContext().getResources().getDimension(R.dimen.sp_15) + "");
                        vTvMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.sp_15));
                    }else{
                        //主要判断显示的TV 内容只为一行则居中显示，内容超过两行 则左对齐
                        setTextViewGravity(vTvMessage);
                    }

                }
//                if(!TextUtils.isEmpty(mParams.messageSpanned)||!TextUtils.isEmpty(mParams.message)) {
                vTvMessage.setVisibility(View.VISIBLE);
//                }else {
//                    vTvMessage.setVisibility(View.GONE);
//                }
                break;
            case MODE_CUSTOM:
                if (mParams.view != null) {
                    vLayoutContainer.removeAllViews();
                    vLayoutContainer.addView(mParams.view);
                }
                break;
        }
        //设置资源文件作为dialog背景
        if (mParams.drawRes != -1) {
            vAutoLinearlayout.setBackgroundResource(mParams.drawRes);
        }
        if (!mParams.isCanckl) {
            this.setCanceledOnTouchOutside(false);
        }

        if (!TextUtils.isEmpty(mParams.confirmDisplay)) {
            vTvConfirm.setText(mParams.confirmDisplay);
        } else {
            vTvConfirm.setVisibility(View.GONE);
        }
        //  添加 自定义 确定 按钮字体颜色 判断
        if (mParams.confirmTextViewColor != -1) {
            vTvConfirm.setTextColor(mParams.confirmTextViewColor);
        }
        if (mParams.onConfirmCallback != null) {
            vTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        mParams.onConfirmCallback.onClick(dialog);
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(mParams.cancelDisplay)) {
            vTvCancel.setText(mParams.cancelDisplay);
        } else {
            vTvCancel.setVisibility(View.GONE);
        }
        //  添加 自定义取消按钮字体颜色 判断
        if (mParams.cancelTextViewColor != -1) {
            vTvCancel.setTextColor(mParams.cancelTextViewColor);
        }
        if (mParams.onCancelCallback != null) {
            vTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        mParams.onCancelCallback.onClick(dialog);
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(mParams.title)) {
            vTvTitle.setText(mParams.title);
            vTvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            vLayoutTitleContainer.setVisibility(View.GONE);
        }

        if (mParams.isSingleLineMsg) {
            vLayoutTitleContainer.setVisibility(View.GONE);
        }

        if (vTvConfirm.getVisibility() == View.GONE && vTvCancel.getVisibility() == View.GONE) {
            vLayoutBtnsContainer.setVisibility(View.GONE);
        } else if (vTvConfirm.getVisibility() == View.GONE && vTvCancel.getVisibility() == View.VISIBLE) {
            vTvCancel.setBackgroundResource(R.drawable.bg_customdialog_single);
            vDivider.setVisibility(View.GONE);
        } else if (vTvConfirm.getVisibility() == View.VISIBLE && vTvCancel.getVisibility() == View.GONE) {
            vTvConfirm.setBackgroundResource(R.drawable.bg_customdialog_single);
            vDivider.setVisibility(View.GONE);
        }
    }

    /**
     * @param tv * 主要判断显示的TV 内容只为一行则居中显示，内容超过两行 则左对齐
     */
    public static void setTextViewGravity(final TextView tv) {
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (tv.getLineCount() == 1) {
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setGravity(Gravity.LEFT | Gravity.CENTER);
                }
                return true;
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 在这里，拦截或者监听Android系统的返回键事件。
            // return将拦截。
            // 不做任何处理则默认交由Android系统处理。
            if (!mParams.isCanckl) {
                return true;
            } else {
                this.dismiss();
                return false;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setIndex(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * 自定义dialog的Builder
     */
    public static class Builder {
        /**
         * 实例
         */
        private CustomDialog instance;
        private Context mContext;
        /**
         * 实例参数
         */
        private Params mParams;

        public Builder(Context context, int mode) {
            this.mContext = context;
            this.mParams = new Params();
            this.mParams.mode = mode;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            mParams.title = title;
            return this;
        }

        /**
         * 设置圆角值
         *
         * @param drawRes
         * @return
         */
        public Builder setBackground(int drawRes) {
            mParams.drawRes = drawRes;
            return this;
        }

        /**
         * 自定义View，当MODE_CUSTOM模式下生效
         *
         * @param view
         * @return
         */
        public Builder setView(View view) {
            mParams.view = view;
            return this;
        }

        /**
         * 设置确认按钮显示内容及点击事件回调
         *
         * @param confirmDisplay
         * @param callback
         * @return
         */
        public Builder setConfirm(String confirmDisplay, DlgCallback callback) {
            mParams.confirmDisplay = confirmDisplay;
            mParams.onConfirmCallback = callback;
            return this;
        }

        /**
         * @param color 自定义 确认 按钮字体颜色值
         * @return
         */
        public Builder setConfirmTextViewColor(int color) {
            mParams.confirmTextViewColor = color;
            return this;
        }


        /**
         * 设置取消按钮显示内容及点击事件回调
         *
         * @param cancelDisplay
         * @param callback
         * @return
         */
        public Builder setCancel(String cancelDisplay, DlgCallback callback) {
            mParams.cancelDisplay = cancelDisplay;
            mParams.onCancelCallback = callback;
            return this;
        }

        /**
         * @param color 自定义取消按钮字体颜色值
         * @return
         */
        public Builder setCancelTextViewColor(int color) {
            mParams.cancelTextViewColor = color;
            return this;
        }


        /**
         * 设置消息内容，当MODE_MESSAGE模式下生效
         *
         * @param message 消息内容
         * @return
         */
        public Builder setMessage(String message) {
            mParams.message = message;
            return this;
        }

        /**
         * 设置消息内容，当MODE_MESSAGE模式下生效
         *
         * @param isMessageCenter 消息内容 是否居中
         * @return
         */
        public Builder setMessageCenter(boolean isMessageCenter) {
            mParams.isMessageCenter = isMessageCenter;
            return this;
        }

        /**
         * 设置消息内容，当MODE_MESSAGE模式下生效
         *
         * @param message 消息内容（例如Html.fromHtml）
         * @return
         */
        public Builder setMessage(Spanned message) {
            mParams.messageSpanned = message;
            return this;
        }

        /**
         * 是否可以取消
         *
         * @param isOff 默认能取消， true 是能取消 fasle 是不能取消
         * @return
         */
        public Builder isCanckl(boolean isOff) {
            mParams.isCanckl = isOff;
            return this;
        }

        /**
         * 消息是否以单行显示
         *
         * @param isSingleLineMsg
         * @return
         */
        public Builder setSingleLineMsg(boolean isSingleLineMsg) {
            mParams.isSingleLineMsg = isSingleLineMsg;
            return this;
        }

        /**
         * 消息是否 显示autoLink
         *
         * @param isMessageAutoLink
         * @return
         */
        public Builder setMessageAutoLink(boolean isMessageAutoLink) {
            mParams.isMessageAutoLink = isMessageAutoLink;
            return this;
        }

        /**
         * 建造对话框
         *
         * @return
         */
        public CustomDialog build() {
            instance = new CustomDialog(this.mContext, this.mParams);
            instance.dialog = instance;
            return instance;
        }

        /**
         * 设置菜单，当MODE_LIST模式下生效
         *
         * @param itemDisplays 显示的菜单item
         * @param callback     item点击事件回调
         * @return
         */
        public Builder setItems(List<String> itemDisplays, SimpleAdapter.Callback callback) {
//            mParams.adapter = new SimpleAdapter(this.mGlobalConfigContext, itemDisplays, callback);
            mParams.itemDisplays = itemDisplays;
            mParams.callback = callback;
            return this;
        }

        /**
         * 设置菜单，当MODE_LIST模式下生效
         *
         * @param itemDisplays 显示的菜单item
         * @param callback     item点击事件回调
         * @return
         */
        public Builder setItems(String[] itemDisplays, SimpleAdapter.Callback callback) {
            setItems(Arrays.asList(itemDisplays), callback);
            return this;
        }
    }

    /**
     * 自定义dialog构造参数类
     */
    static class Params {
        /**
         * DIALOG类型
         */
        int mode;
        //        BaseAdapter adapter;
        /**
         * 选项
         */
        List<String> itemDisplays;
        /**
         * 回调
         */
        SimpleAdapter.Callback callback;
        /**
         * 标题
         */
        String title;
        /**
         * 显示消息
         */
        String message;
        /**
         * 显示消息，用于显示HTML
         */
        Spanned messageSpanned;
        /**
         * 确定按钮，显示的字符串
         */
        String confirmDisplay;
        /**
         * 取消按钮显示的字符串
         */
        String cancelDisplay;
        /**
         * 自定义View
         */
        View view;
        /**
         * 确认按钮回调
         */
        DlgCallback onConfirmCallback;
        /**
         * 取消按钮回调
         */
        DlgCallback onCancelCallback;
        /**
         * 消息是否以单行显示
         */
        boolean isSingleLineMsg = false;
        /**
         * 是否可以取消
         */
        boolean isCanckl = true;
        /**
         * 消息是否居中显示
         */
        boolean isMessageCenter = false;
        /**
         * 消息是否 显示autoLink  ，默认显示
         */
        boolean isMessageAutoLink = true;
        /**
         * 设置圆角
         */
        int drawRes = -1;

        /**
         * 取消 按钮自定义颜色
         */
        int cancelTextViewColor = -1;
        /**
         * 确定 按钮自定义颜色
         */
        int confirmTextViewColor = -1;
    }

    /**
     * 确认及取消事件回调借口
     */
    public interface DlgCallback {
        void onClick(CustomDialog dialog);
    }

    /**
     * List模式下，item适配器
     */
    public static class SimpleAdapter extends BaseAdapter {
        List<String> strings;
        Context mContext;
        Callback mCallback;
        boolean hasTitle;
        boolean hasBottom;
        //当前选中的索引
        int mIndex;

        //设置当前选择中的索引
        public void setIndex(int index) {
            this.mIndex = index;
        }

        public SimpleAdapter(Context context, List<String> strings, Callback callback, boolean hasTilte, boolean hasBottom) {
            this.mContext = context;
            this.strings = strings;
            this.mCallback = callback;
            this.hasTitle = hasTilte;
            this.hasBottom = hasBottom;
        }

        @Override
        public int getCount() {
            return strings == null ? 0 : strings.size();
        }

        @Override
        public Object getItem(int position) {
            return strings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewhoder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_customdialog_item, parent, false);
                viewhoder = new ViewHolder();
                viewhoder.vTvItem = (TextView) convertView.findViewById(R.id.tv_custom_dialog_item_text);
                viewhoder.vSelectIcon = (ImageView) convertView.findViewById(R.id.select_icon);
                convertView.setTag(viewhoder);
                AutoUtils.autoSize(convertView);
            } else {
                viewhoder = (ViewHolder) convertView.getTag();
            }

            if (!hasTitle && !hasBottom) {
                if (getCount() == 1) {
                    viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_single);
                } else {
                    if (position == getCount() - 1) {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_bottom);
                    } else if (position == 0) {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_top);
                    } else {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_center);
                    }
                }
            } else if (!hasTitle && hasBottom) {
                if (getCount() == 1) {
                    viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_top);
                } else {
                    if (position == 0) {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_top);
                    } else {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_center);
                    }
                }
            } else if (hasTitle && !hasBottom) {
                if (getCount() == 1) {
                    viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_bottom);
                } else {
                    if (position == getCount() - 1) {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_bottom);
                    } else {
                        viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_center);
                    }
                }
            } else {
                viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_center);
            }

            if (!TextUtils.isEmpty(strings.get(position))) {
                viewhoder.vTvItem.setText(strings.get(position));
//                viewhoder.vTvItem.setBackgroundResource(R.drawable.bg_bottomsheet_item_center);
                viewhoder.vTvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallback != null) {
                            mCallback.itemCallback(position);
                        }
                    }
                });
                if (position == mIndex) {
                    viewhoder.vSelectIcon.setVisibility(View.VISIBLE);
                } else {
                    viewhoder.vSelectIcon.setVisibility(View.INVISIBLE);
                }
            }
            return convertView;
        }

        public class ViewHolder {
            TextView vTvItem;
            ImageView vSelectIcon;
        }

        /**
         * 列表模式Item点击事件回调接口
         */
        public interface Callback {
            void itemCallback(int position);
        }

    }
}
