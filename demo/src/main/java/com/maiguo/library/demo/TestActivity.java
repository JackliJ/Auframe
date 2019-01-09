package com.maiguo.library.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.maiguoer.component.http.app.BaseHttpApplication;
import com.maiguoer.component.http.app.MgeSubscriber;
import com.maiguoer.component.http.data.User;
import com.maiguoer.component.http.lirbrary_demo.ApiHttpDemo;
import com.maiguoer.component.http.lirbrary_demo.bean.LoginBean;
import com.maiguoer.component.http.lirbrary_demo.bean.UserBean;
import com.maiguoer.component.http.utils.SharedPreferencesUtils;
import com.maiguoer.component.http.utils.Utils;

import java.util.List;

import mge_data.UserDao;

/**
 * 用于演示项目基础组件用途的Demo模板
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

@Route(path = "/test/testActivity")
public class TestActivity extends Activity {

    private Button vBtn, vBtnUser;
    private TextView vTUer;
    private EditText vTAccount, vTPassword;
    private boolean isLoginSuccess;//是否登录成功
    private final static String TAG = "TestActivity_TAG";
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_demo_layout);
        mContext = this;
        initView();
        vBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录脉果儿
                ApiHttpDemo.getInstance().StartLogin(mContext, TAG, vTAccount.getText().toString().trim(),
                        Utils.stringToMD5(vTPassword.getText().toString().trim()), new MgeSubscriber<LoginBean>() {
                            @Override
                            public void onStart() {
                                Log.d(TAG, "开始请求");
                            }

                            @Override
                            public void onSuccess(LoginBean result) {
                                Log.d(TAG, "请求成功");
                                isLoginSuccess = true;
                                Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                                //设置Token
                                SharedPreferencesUtils.SaveAppToken(mContext, result.getToken());
                                //设置登录用户ID
                                SharedPreferencesUtils.SaveLoginUid(mContext, result.getData().getLoginInfo().getUid());
                                //设置登录用户头像
                                SharedPreferencesUtils.SaveLoginUserAvatar(mContext, result.getData().getLoginInfo().getAvatar());
                                ApiHttpDemo.getInstance().GetMemberShowInfo(mContext, TAG, new MgeSubscriber<UserBean>() {
                                    @Override
                                    public void onStart() {

                                    }


                                    @Override
                                    public void onSuccess(UserBean result) {
                                        if (result != null) {
                                            UserBean.DataBean.MemberInfoBean memberInfo = result.getData().getMemberInfo();
                                            User mUser = new User();
                                            // 用户Id	int
                                            mUser.uid = memberInfo.getUid();
                                            // 店铺id	int
                                            mUser.storeId = memberInfo.getStoreId();
                                            // 环信用户名	string
                                            mUser.hxname = memberInfo.getHxname();
                                            // 用户名	string
                                            mUser.username = memberInfo.getUsername();
                                            // vip等级	int
                                            mUser.vipLevel = memberInfo.getVipLevel();
                                            // 公司	string
                                            mUser.company = memberInfo.getCompany();
                                            // 职位	string
                                            mUser.position = memberInfo.getPosition();
                                            // 性别	int	1男 2女
                                            mUser.gender = memberInfo.getGender();
                                            // 生日	string	例如：1980年01月01日
                                            mUser.bornDate = memberInfo.getBornDate();
                                            // 星座	string
                                            mUser.constellation = memberInfo.getConstellation();
                                            // 电话是否公开	int	0隐藏 1公开
                                            mUser.mobileVisible = memberInfo.getMobileVisible();
                                            // 头像	string
                                            mUser.avatar = memberInfo.getAvatar();
                                            // 是否单身	int	1单身 2非单身 3保密
                                            mUser.isSingle = memberInfo.getIsSingle();
                                            // 行业	string
                                            mUser.occupation = memberInfo.getOccupation();
                                            // 兴趣	string
                                            mUser.hobby = memberInfo.getHobby();
                                            // 电话	string
                                            mUser.mobile = memberInfo.getMobile();
                                            // 家乡	string
                                            mUser.bornArea = memberInfo.getBornArea();
                                            // 出生时间段	string
                                            mUser.birthPeriod = memberInfo.getBirthPeriod();
                                            // 背景图点赞数	string	如果超出1万则单位成万，如 1.2万，保留一位小数
                                            mUser.bgLikeCount = memberInfo.getBgLikeCount();
                                            // 背景图
                                            mUser.bgImage = memberInfo.getBgImage();
                                            // 实名认证状态	int	1已认证 0未认证
                                            mUser.authStatus = memberInfo.getAuthStatus();
                                            // 居住地	string
                                            mUser.resideArea = memberInfo.getResideArea();
                                            // 居住城市	string
                                            mUser.resideCity = memberInfo.getResideCity();
                                            // 点赞状态	int	1可以点赞 0不能点赞
                                            mUser.bgLikeStatus = memberInfo.getBgLikeStatus();
                                            // 真实姓名	string
                                            mUser.realname = memberInfo.getRealname();
                                            // 商铺认证状态	int	1认证 0未认证
                                            mUser.businessAuthStatus = memberInfo.getBusinessAuthStatus();
                                            // 联盟等级	int
                                            mUser.unionLevel = memberInfo.getUnionLevel();
                                            //是否设置二级密码
                                            mUser.isSetSecPassword = memberInfo.getIsSetSecPassword();
                                            // 联系人数	int
                                            mUser.contactsCount = memberInfo.getContactsCount();
                                            // 订单数	int
                                            mUser.orderCount = memberInfo.getOrderCount();
                                            // 是否专职员工 int
                                            mUser.isStaff = memberInfo.getIsStaff();
                                            // 联盟积分 社交价值指数
                                            mUser.unionCredit = memberInfo.getUnionCredit();
                                            // 关注数	string
                                            mUser.followCount = memberInfo.getFollowCount();
                                            // 粉丝数	string
                                            mUser.fansCount = memberInfo.getFansCount();
                                            // 访客数	string
                                            mUser.viewCount = memberInfo.getViewCount();
                                            // 人脉数	string
                                            mUser.inviteCount = memberInfo.getInviteCount();
                                            // 送花数	string
                                            mUser.flowerCount = memberInfo.getFlowerCount();
                                            // 评论印象总数	int
                                            mUser.impressionCount = memberInfo.getImpressionCount();
                                            //是否发改委
                                            mUser.chairman = memberInfo.getIsChairman();
                                            //是否联盟商户
                                            mUser.unionMerchant = memberInfo.getIsUnionMerchant();
                                            //录制动态视频时长	int	单位:秒
                                            mUser.zoneVodDuration = Integer.parseInt(memberInfo.getZoneVodDuration());
                                            //是否1v1发布者	int 0否 1是
                                            mUser.isHost = memberInfo.getIsHost();
                                            //是否创客 int 0：否；1：是
                                            mUser.isMaker = memberInfo.getIsMaker();
                                            //	是否福利	int	0 不是 1 是
                                            mUser.isWelfare = memberInfo.getIsWelfare();
                                            //是否果儿令	int	0：否；1：是
                                            mUser.isFruitToken = memberInfo.getIsFruitToken();
                                            //价值等级	int
                                            mUser.valueLevel = memberInfo.getValueLevel();
                                            //是否是 天使旅行家
                                            mUser.showActivityUrl = result.getData().getShowActivityUrl();
                                            //vip状态
                                            mUser.vipStatus = memberInfo.getVipStatus();
                                            //推荐ID或原导师ID
                                            mUser.inviteUid = memberInfo.getInviteUid();
                                            //创客等级
                                            mUser.makerLevel = memberInfo.getMakerLevel();
                                            // 评论印象前三头像	array
                                            StringBuffer sb = new StringBuffer();
                                            for (int i = 0; i < memberInfo.getUserAvatars().size(); i++) {
                                                if (!TextUtils.isEmpty(sb.toString())) {
                                                    sb.append(",");
                                                }
                                                sb.append(memberInfo.getUserAvatars());
                                            }
                                            mUser.userAvatars = sb.toString();
                                            //插入数据库
                                            BaseHttpApplication.getInstances().getDaoSession().getUserDao().insertOrReplace(mUser);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {

                                    }

                                    @Override
                                    public void onEnd() {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                Log.d(TAG, code + "---" + msg);
                                isLoginSuccess = false;
                            }

                            @Override
                            public void onEnd() {
                                Log.d(TAG, "结束请求");
                            }
                        });
            }
        });


        vBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> user = BaseHttpApplication.getInstances().getDaoSession().getUserDao().loadAll();
                String jsonUser = JSONObject.toJSONString(user.get(0));
                vTUer.setText(jsonUser);
            }
        });

    }

    /**
     * 初始化组件
     */
    private void initView() {
        vBtn = findViewById(R.id.button_demo_library_demo);
        vTAccount = findViewById(R.id.test_account);
        vTPassword = findViewById(R.id.test_password);
        vBtnUser = findViewById(R.id.text_button_user);
        vTUer = findViewById(R.id.text_tv_user);
    }

}
