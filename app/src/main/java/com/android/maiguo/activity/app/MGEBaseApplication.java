package com.android.maiguo.activity.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.maiguo.activity.BuildConfig;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.util.NetUtils;
import com.maiguoer.component.http.app.BaseHttpApplication;

import java.util.Iterator;
import java.util.List;

import static com.hyphenate.chat.EMClient.TAG;

/**
 * Create by www.lijin@foxmail.com on 2018/12/13 0013.
 * <br/>
 */

public class MGEBaseApplication extends BaseHttpApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
        initChat();
    }

    private void initSDK() {
        //阿里云路由注解初始化
        if (BuildConfig.DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog(); // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    private void initChat() {
        //初始化环信
        Context appContext = this;
//        //初始化单例
//        ChatCallTimerUtil.setBinstance(new ChatCallTimerUtil());
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置小米推送 appID 和 appKey
        options.setMipushConfig("2882303761517481214", "5221748126214");
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        //options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(false);

        //初始化环信
        EMClient.getInstance().init(appContext, options);
        //关闭环信debug模式
        EMClient.getInstance().setDebugMode(true);
//        //注册电话监听
//        initCallMonitor();

        //注册环信网络和离线监听
        initnoetowork();
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initnoetowork() {
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            Intent in;
            if (error == EMError.USER_REMOVED) {// 显示帐号已经被移除
                // 清理阿里云注册
//                ApplicationUtils.deleteAliAses();
//                in = new Intent(BroadCastReceiverConstant.BROAD_CHAT_REMOVED);
//                sendBroadcast(in);
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) { // 显示帐号在其他设备登录
//                LogUtils.tag("dx").d("------------CALL_CONNECTION_ERROR------------");
//                // 退出
//                LoginActivity.EntityType entityType = new LoginActivity.EntityType();
//                // 环信挤掉线类型
//                entityType.setType(LoginActivity.Type.isRemotelogin);
//                EventBus.getDefault().post(new ExitLoginAppEvent(entityType));
            } else if (error == EMError.CALL_CONNECTION_ERROR) {//电话没数据

            } else {
//                if (NetUtils.hasNetwork(getInstance())) {
//                    //连接不到聊天服务器
//                    in = new Intent(BroadCastReceiverConstant.BROAD_CHAT_SERVER);
//                    sendBroadcast(in);
//                } else {
//                    //当前网络不可用BROAD_CHAT_AVAILABLE
//                    in = new Intent(BroadCastReceiverConstant.BROAD_CHAT_AVAILABLE);
//                    sendBroadcast(in);
//                }
            }
        }
    }
}
