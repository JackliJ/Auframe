package com.chat.business.library.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.chat.business.library.R;
import com.chat.business.library.eventbus.ChatReceivedEventBus;
import com.chat.business.library.eventbus.ShowMainMessageCountEvent;
import com.chat.business.library.model.ConversationBean;
import com.chat.business.library.util.ChatItemClickListener;
import com.chat.business.library.util.OnStartDragListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.maiguoer.component.http.app.BaseHttpApplication;
import com.maiguoer.component.http.base.BasisFragment;
import com.maiguoer.component.http.data.TopDataBase;
import com.maiguoer.component.http.data.User;
import com.maiguoer.component.http.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Sky on 2017/8/30.
 * <p>
 * 交流 - 消息列表
 */

public class
ChatMessageListFragment extends BasisFragment implements ChatItemClickListener, OnStartDragListener {

    RecyclerView vChatMessageRe;

    LinearLayoutManager mLayoutManager;
    ChatMessageListAdapter mChatMessageListAdapter;
    View vRootView;
    Context mContext;
    List<ConversationBean> mData;
    ConversationBean mConversation;
    IntentFilter filter;
    ChatMessageAsyncTask mChatAsync;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        EventBus.getDefault().register(this);//订阅
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (vRootView == null) {
            vRootView = inflater.inflate(R.layout.fragment_chat_message_list, container, false);
            initAsync();
        }
        return vRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        vChatMessageRe = view.findViewById(R.id.re_chat_message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onReceivedEvent(ChatReceivedEventBus event) {
        initAsync();
    }

    private void initAsync() {
        mChatAsync = new ChatMessageAsyncTask();
        mChatAsync.execute();
    }


    private ItemTouchHelper mItemTouchHelper;
    private int MesgCount = 0;

    /**
     * 排序  置顶按照时间排序 最后添加的在最前面  普通消息按照时间排序 最新的在最前面
     *
     * @param list
     */
    private static void ListSorts(List<ConversationBean> list) {
        //先将数组分为置顶和普通消息
        List<ConversationBean> topList = new ArrayList<>();
        List<ConversationBean> orList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserTopStatus() == 1) {
                topList.add(list.get(i));
            } else {
                orList.add(list.get(i));
            }
        }
        //分别排序
        SortsTop(topList);
        SortsTime(orList);
        list.clear();
        //重新添加数据
        list.addAll(topList);
        list.addAll(orList);
    }

    private static void SortsTop(List<ConversationBean> list) {
        Collections.sort(list, new Comparator<ConversationBean>() {
            @Override
            public int compare(ConversationBean o1, ConversationBean o2) {
                if (o1.getUserTopTime() == o2.getUserTopTime()) {
                    return 0;
                } else if (o2.getUserTopTime() > o1.getUserTopTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    private static void SortsTime(List<ConversationBean> list) {
        Collections.sort(list, new Comparator<ConversationBean>() {
            @Override
            public int compare(ConversationBean o1, ConversationBean o2) {
                if (o1.getEndtime() == o2.getEndtime()) {
                    return 0;
                } else if (o2.getEndtime() > o1.getEndtime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }


    private void sdy() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        vChatMessageRe.setLayoutManager(mLayoutManager);
        mChatMessageListAdapter = new ChatMessageListAdapter(getActivity(), mData);
        mChatMessageListAdapter.setOnItemClickListener(this);
        vChatMessageRe.setHasFixedSize(true);
        vChatMessageRe.setAdapter(mChatMessageListAdapter);
        vChatMessageRe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
//                    if (null != viewCache) {
//                        viewCache.smoothClose();
//                    }
                }
                return false;
            }
        });
    }

    /**
     * 根据ID获取字段
     *
     * @param uid
     * @return
     */
    private String getusernote(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            int userid = Integer.parseInt(uid);
//            Frend frend = FrendDao.getFrend(mContext, userid);
//            if (frend != null) {
//                if (!TextUtils.isEmpty(frend.userNote)) {//昵称
//                    return frend.userNote;
//                } else {
//                    return frend.username;
//                }
//            }
            return "ces";
        }
        return null;
    }

    public EMMessage.ChatType getChatType(EMMessage message) {
        if (message.getChatType() == EMMessage.ChatType.Chat) {
            return EMMessage.ChatType.Chat;
        } else if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            return EMMessage.ChatType.GroupChat;
        }
        return EMMessage.ChatType.Chat;
    }

    /**
     * RecyclerView OnItemClick
     *
     * @param view
     * @param postion
     */
    @Override
    public void onItemClick(View view, int postion) {
        try {
            if (view.getId() != R.id.rl_secretary && postion > 0) {//排除脉友圈和小秘书和搜索
                postion = postion - 1;
                if (mData.get(postion).getChatType().toString().equals(EMMessage.ChatType.GroupChat.toString())) {//群聊
//                    Intent in = new Intent(getContext(), ChatGroupMessageActivity.class);
//                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_HXID, mData.get(postion).getUuid());
//                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_CHATTYPE, EMMessage.ChatType.GroupChat.ordinal());
//                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHRTUID, mData.get(postion).getUid());
//                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERNAME, mData.get(postion).getUsername());
//                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERAVATAR, mData.get(postion).getUseravatar());
//                    startActivity(in);
                } else if (mData.get(postion).getChatType().toString().equals(EMMessage.ChatType.Chat.toString())) {//单聊
                    Intent in = new Intent(getContext(), ChatMessageActivity.class);
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHRTUID, mData.get(postion).getUid());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_HXID, mData.get(postion).getUuid());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_CHATTYPE, EMMessage.ChatType.Chat.ordinal());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERNAME, mData.get(postion).getUsername());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERUSERAVATAR, mData.get(postion).getUseravatar());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERVIPLEVEL, String.valueOf(mData.get(postion).getUserLeavl()));
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERAURHSTATUS, mData.get(postion).getAuthStatus());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERABUSINESSAU, mData.get(postion).getBusinessAuthStatus());
                    in.putExtra(Constant.MEG_INTNT_CHATMESSAGE_OTHERNAMGECARSBGIMAGE, mData.get(postion).getOtherNamgeCardBgImage());
                    startActivity(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
    }

    private class ChatMessageAsyncTask extends AsyncTask<Void, Integer, List<ConversationBean>> {

        /**
         * 去查询所有的会话消息并返回结果
         *
         * @param voids
         * @return
         */
        @Override
        protected List<ConversationBean> doInBackground(Void... voids) {
            //读取数据库 查询保存的置顶数据
            List<TopDataBase> mTopList = BaseHttpApplication.getInstances().getDaoSession().getTopDataBaseDao().loadAll();
            if (mData != null) {
                mData.clear();
            } else {
                mData = new ArrayList<>();
            }
//            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//            if (conversations.size() > 0 && conversations != null) {
//                MesgCount = 0;
//                for (String i : conversations.keySet()) {
//                    MesgCount = MesgCount + conversations.get(i).getUnreadMsgCount();
//                    List<EMMessage> messages = conversations.get(i).getAllMessages();
//                    if (messages != null && messages.size() > 0) {
//                        if (!messages.get(messages.size() - 1).getUserName().equals("customer_service")) {//去掉小秘书
//                            mConversation = new ConversationBean();
//                            if (messages.get(messages.size() - 1).getChatType().toString().equals(EMMessage.ChatType.Chat.toString())) {//单聊
//                                if (messages.get(messages.size() - 1).direct() == EMMessage.Direct.RECEIVE) {//最后一条消息是接受方
//                                    mConversation.setUid(messages.get(messages.size() - 1).getStringAttribute("uid", null));
//                                    mConversation.setUserLeavl(messages.get(messages.size() - 1).getIntAttribute("vipLevel", 0));
//                                    String uid = messages.get(messages.size() - 1).getStringAttribute("uid", null);
//                                    mConversation.setUsername(getusernote(uid) != null ?
//                                            getusernote(uid) : messages.get(messages.size() - 1).getStringAttribute("username", null));
//                                    mConversation.setUseravatar(messages.get(messages.size() - 1).getStringAttribute("userAvatar", null));
//                                    mConversation.setAuthStatus(messages.get(messages.size() - 1).getStringAttribute("authStatus", null));
//                                    mConversation.setBusinessAuthStatus(messages.get(messages.size() - 1).getStringAttribute("businessAuthStatus", null));
//                                    mConversation.setOtherNamgeCardBgImage(messages.get(messages.size() - 1).getStringAttribute("namgeCardBgImage", null));
//                                } else { //最后一条消息是发送方
//                                    mConversation.setUid(messages.get(messages.size() - 1).getStringAttribute("otherUid", null));
//                                    mConversation.setUserLeavl(messages.get(messages.size() - 1).getIntAttribute("otherVipLevel", 0));
//                                    String uid = messages.get(messages.size() - 1).getStringAttribute("otherUid", null);
//                                    mConversation.setUsername(getusernote(uid) != null ?
//                                            getusernote(uid) : messages.get(messages.size() - 1).getStringAttribute("otherUsername", null));
//                                    mConversation.setUseravatar(messages.get(messages.size() - 1).getStringAttribute("otherUserAvatar", null));
//                                    LogUtils.d("vip--4->" + messages.get(messages.size() - 1).getStringAttribute("otherUserAvatar", null));
//                                    mConversation.setAuthStatus(messages.get(messages.size() - 1).getStringAttribute("otherAuthStatus", null));
//                                    mConversation.setBusinessAuthStatus(messages.get(messages.size() - 1).getStringAttribute("otherBusinessAuthStatus", null));
//                                    mConversation.setOtherNamgeCardBgImage(messages.get(messages.size() - 1).getStringAttribute("otherNamgeCardBgImage", null));
//                                }
//                            } else {//群聊
//                                mConversation.setUid(messages.get(messages.size() - 1).getStringAttribute("uid", null));
//                                mConversation.setUserLeavl(messages.get(messages.size() - 1).getIntAttribute("vipLevel", 0));
//                                mConversation.setUsername(messages.get(messages.size() - 1).getStringAttribute("username", null));
//                                mConversation.setUseravatar(messages.get(messages.size() - 1).getStringAttribute("userAvatar", null));
//                                mConversation.setAuthStatus(messages.get(messages.size() - 1).getStringAttribute("authStatus", null));
//                                mConversation.setBusinessAuthStatus(messages.get(messages.size() - 1).getStringAttribute("businessAuthStatus", null));
//                                mConversation.setOtherNamgeCardBgImage(messages.get(messages.size() - 1).getStringAttribute("namgeCardBgImage", null));
//                            }
//
//                            try {
//                                mConversation.setUserunread(conversations.get(i).getUnreadMsgCount());//获取未读消息的数量
//                            } catch (Exception e) {
//                                mConversation.setUserunread(0);
//                            }
//                            mConversation.setUuid(conversations.get(i).conversationId());//赋值环信用户ID
//                            mConversation.setEndtime(messages.get(messages.size() - 1).getMsgTime());//赋值最后一条消息的时间
//                            mConversation.setUsercontext(messages.get(messages.size() - 1).getBody().toString());//赋值最后一条消息
//                            mConversation.setType(messages.get(messages.size() - 1).getType().toString());//赋值消息类型
//                            mConversation.setChatType(getChatType(messages.get(messages.size() - 1)));
//                            mConversation.setTextType(messages.get(messages.size() - 1).getStringAttribute("text_type", null));//扩展类型
//                            mData.add(mConversation);
//                            ListSort(mData);
//                        }
//                    }
//                }
//            } else {
//                LogUtils.d("ChatInterfaceActivity", " is call list null ");
//            }
//            EventBus.getDefault().post(new ShowMainMessageCountEvent(MesgCount));

            for (int i = 0; i < 10; i++) {
                mConversation = new ConversationBean();
                mConversation.setUid("10000");
                mConversation.setUserLeavl(1);
                mConversation.setUseravatar("http://cdn.duitang.com/uploads/item/201409/18/20140918141220_N4Tic.jpeg");
                mConversation.setAuthStatus("1");
                mConversation.setBusinessAuthStatus("1");
                mConversation.setOtherNamgeCardBgImage("");
                mConversation.setUuid("805931" + i);//赋值环信用户ID
                mConversation.setEndtime(1548149630);//赋值最后一条消息的时间
                mConversation.setUsercontext("8059316\\\"测试消息" + i);//赋值最后一条消息
                mConversation.setType(EMMessage.Type.TXT.toString());//赋值消息类型
                mConversation.setChatType(EMMessage.ChatType.Chat);
                mConversation.setTextType(null);//扩展类型
                mConversation.setUserunread(0);
                //判断数据库数组中是否包含置顶数据
                for (int j = 0; j < mTopList.size(); j++) {
                    if (mTopList.get(j).getTagId().equals(mConversation.getUuid())) {
                        mConversation.setUserTopStatus(1);
                        mConversation.setUserTopTime(mTopList.get(j).getTagTime());
                    }
                }
                mData.add(mConversation);
                ListSorts(mData);
            }
            return mData;
        }

        /**
         * 用于更新界面loading 由于需求  暂留位置
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 根据结果 更新UI
         *
         * @param conversationBeans
         */
        @Override
        protected void onPostExecute(List<ConversationBean> conversationBeans) {
            if (mChatMessageListAdapter != null) {
                mChatMessageListAdapter.notifyDataSetChanged();
            } else {
                sdy();
            }
        }

        /**
         * 执行中断操作
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
