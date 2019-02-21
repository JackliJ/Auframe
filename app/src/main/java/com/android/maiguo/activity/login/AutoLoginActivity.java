package com.android.maiguo.activity.login;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.maiguo.activity.R;
import com.blankj.utilcode.util.NetworkUtils;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;
import com.maiguoer.component.http.utils.Utils;
import com.maiguoer.widget.MgeToast;

/**
 * Created by laotie on 2019/2/20.
 * 运营商 自动 一键 登录
 */

public class AutoLoginActivity extends BaseSwipeBackActivity implements View.OnClickListener {
    /**
     * 顶替状态栏位置
     */
    View vStatusBarV;
    TextView vTipsTv;
    ImageView mOnLineSelect;
    /**
     * 默认不选中
     */
    private boolean mOnLineEnable = true;
    private boolean mOnLineIsSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLightStatusBar(this, true);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        vStatusBarV = findViewById(R.id.v_status_bar);
        vTipsTv = (TextView) findViewById(R.id.v_tips_tv);
        vTipsTv.setText(Html.fromHtml(getResources().getString(R.string.login_str5)));
        vTipsTv.setOnClickListener(this);
        mOnLineSelect = (ImageView) findViewById(R.id.iv_online_select);
        mOnLineSelect.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.setTranslucent(this, R.color.b1);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = Utils.getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
        //判断网络是否是移动数据
        if (!NetworkUtils.isMobileData()){
            MgeToast.showErrorToast(this,getResources().getString(R.string.login_str7));
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_online_select) {
            if (mOnLineEnable) {
                //是否审核通过上线 选择框
                mOnLineIsSel = !mOnLineIsSel;
                //改变选择样式
                mOnLineSelect.setSelected(mOnLineIsSel);
            }
        } else if (v.getId() == R.id.v_tips_tv) {
            // 跳协议页面

        }
    }
}
