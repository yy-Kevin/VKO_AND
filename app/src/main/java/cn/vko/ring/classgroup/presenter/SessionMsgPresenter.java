package cn.vko.ring.classgroup.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.adapter.SessionMsgAdapter;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.MyListView;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.message.activity.MsgSysListActivity;
import cn.vko.ring.message.adapter.MsgListAdapter;
import cn.vko.ring.message.model.BaseMsgModel;
import cn.vko.ring.message.model.MsgItemModel;
import cn.vko.ring.message.model.MsgModelData;
import cn.vko.ring.utils.ACache;

/**
 * Created by A on 2016/11/21.
 */
public class SessionMsgPresenter implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private int pageSize = 10;
    private Context mContext;
    private MyListView mMsgListView;
    private SessionMsgAdapter mAdapter;
    private Activity act;
    private VolleyUtils<MsgModelData> mVolleyUtil;
    /* 1：推荐消息 2：系统消息 */
    private int type = 1;
    private int index = 1;
    private BaseMsgModel baseMsgModel;
    private List<MsgItemModel> msgList;
    boolean isLoadMore;
    private View emptyView;
    /** 缓存首页数据 推荐消息 */
    private static String MSG_CONTENT = "msg_content";
    MsgModelData msgModelData;

    public SessionMsgPresenter(Context mContext, MyListView mMsgListView, Activity act, int type, View emptyView) {
        super();
        this.mContext = mContext;
        this.mMsgListView = mMsgListView;
        this.act = act;
        this.type = type;
        this.emptyView = emptyView;
        initData();
    }
    /**
     * @Title: initData
     * @Description: TODO
     * @return: void
     */

    private void initData() {


        mVolleyUtil = new VolleyUtils<MsgModelData>(mContext,MsgModelData.class);
        msgList = new ArrayList<MsgItemModel>();
//        mMsgListView.setPullRefreshEnable(true);
        baseMsgModel = new BaseMsgModel();
        mAdapter = new SessionMsgAdapter(msgList, mContext, act, type, mMsgListView);
        mMsgListView.setAdapter(mAdapter);
        mMsgListView.setOnItemClickListener(this);
        if (VkoContext.getInstance(mContext).getUser() != null) {
            MSG_CONTENT = MSG_CONTENT + VkoContext.getInstance(mContext).getUserId();
            msgModelData = (MsgModelData) ACache.get(mContext).getAsObject(MSG_CONTENT);
            if (msgModelData != null) {
                msgList.addAll(msgModelData.getData().getMsglist());
                mAdapter.notifyDataSetChanged();
            }
        }
        if (VkoApplication.getC() == true){
            mAdapter.notifyDataSetChanged();
        }

        refreshData();
    }
    /*
     * @Description: TODO
     */
    @Override
    public void onRefresh() {
        isLoadMore = false;
        index = 1;
        refreshData();
    }
    public void refreshData() {
        UserInfo user = VkoContext.getInstance(mContext).getUser();
        String msgUrl = "";
        if (type == 1) {
            msgUrl = ConstantUrl.VK_MESSAGE_LIST;
        } else {
            msgUrl = ConstantUrl.VK_MESSAGE_SYS_LIST;
        }
        Uri.Builder b = mVolleyUtil.getBuilder(msgUrl);
        if (user != null) {
            if (type == 1) {
                if (!TextUtils.isEmpty(user.getProvinceId())) {
                    b.appendQueryParameter("provinceId", user.getProvinceId());
                }
                if (!TextUtils.isEmpty(user.getLearn())) {
                    b.appendQueryParameter("learnId", user.getLearn());
                }
            }
            b.appendQueryParameter("token", user.getToken());
        }
        b.appendQueryParameter("pageIndex", index + "");
        b.appendQueryParameter("pageSize", pageSize + "");
        if (type == 1) {
            b.appendQueryParameter("msgType", type + "");
        }
        mVolleyUtil.sendGETRequest(true,b);
        mVolleyUtil.setUiDataListener(new UIDataListener<MsgModelData>() {
            @Override
            public void onDataChanged(MsgModelData response) {
                if (response == null) {
                    showEmptyView();
                    return;
                }
                if (response.getData() != null) {
                    if (!isLoadMore && response.getData().getMsglist().size() > 0) {
                        msgList.clear();
                        ACache.get(mContext).put(MSG_CONTENT, response);
                    }
                    if (response.getData().getMsglist() != null && response.getData().getMsglist().size() > 0) {
                        msgList.addAll(response.getData().getMsglist());
                        if (msgList.size() >= pageSize) {
//                            mMsgListView.setPullLoadEnable(true);
                        }
                        mAdapter.notifyDataSetChanged();

                        emptyView.setVisibility(View.GONE);
                    } else {
                        showEmptyView();
                    }
                } else {
                    showEmptyView();
                }
                stopUp();
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                showEmptyView();
                stopUp();
            }
        });


    }
    private void showEmptyView() {
        if (msgList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }
    public void stopUp() {
//        mMsgListView.stopLoadMore();
//        mMsgListView.stopRefresh();
//        mMsgListView.setRefreshTime(DateUtils.getCurDateStr());
    }
    @Override
    public void onLoadMore() {
        isLoadMore = true;
        index++;
        refreshData();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        // MsgItemModel item = msgLists.get(position - 1);
        // int state = msgLists.get(position).getMSG_TYPE();
        // switch (state) {
        // case BaseMsgModel.MSG_LINK_TEXT:
        // goToDetail(item, state);
        // break;
        // default:
        // break;
        // }
    }
    /**
     * @Title: goToDetail
     * @Description: 消息详情
     * @param item
     * @param state
     * @return: void
     */
    private void goToDetail(MsgItemModel item, int state) {
        switch (state) {
            case BaseMsgModel.MSG_GROUP_AUDIT:
                break;
            case BaseMsgModel.MSG_SYS_ICON:
                // 点击系统消息
                StartActivityUtil.startActivity(mContext, MsgSysListActivity.class);
                break;
            case BaseMsgModel.MSG_LINK_TEXT:
                break;
            default:
                break;
        }
    }


}
