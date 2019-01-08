package com.activity.maiguo.map.library;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.maiguoer.component.http.app.BaseSwipeBackActivity;
import com.maiguoer.component.http.utils.Utils;
import com.maiguoer.widget.MgeToast;
import com.maiguoer.widget.dialog.CustomDialog;

/**
 * Created by laotie on 2019/1/3.
 */

@Route(path = "/map/LocationActivity")
public class LocationActivity extends BaseSwipeBackActivity implements View.OnClickListener {
    /**
     * 顶替状态栏位置
     */
    View vStatusBarV;

    Button v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLightStatusBar(this, true);
        setContentView(R.layout.activity_location);
        v1 = (Button) findViewById(R.id.v_1);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        vStatusBarV = findViewById(R.id.v_status_bar);
    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.setTranslucent(this, R.color.b2);
        ViewGroup.LayoutParams mLayoutParams = vStatusBarV.getLayoutParams();
        mLayoutParams.height = Utils.getStatusBarHeight(this);
        vStatusBarV.setLayoutParams(mLayoutParams);
    }

    private void aa() {
        // 清理缓存
        CustomDialog vDialog = new CustomDialog.Builder(this, CustomDialog.MODE_MESSAGE)
                .setTitle("测试")
                .setMessage(getResources().getString(R.string.setup_clear_message))
                .isCanckl(false)
                .setConfirm(getResources().getString(R.string.confirm), new CustomDialog.DlgCallback() {
                    @Override
                    public void onClick(CustomDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setCancel(getResources().getString(R.string.cancel), new CustomDialog.DlgCallback() {
                    @Override
                    public void onClick(CustomDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        vDialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.v_1) {
            aa();
        }else if (v.getId() == R.id.v_2) {
            MgeToast.showErrorToast(LocationActivity.this,"6666666");
        }

    }
}
