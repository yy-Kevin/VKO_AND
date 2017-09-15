package cn.vko.ring.classgroup.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.activity.GroupDetailActivity;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.common.widget.xlv.MyListView;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.message.activity.MsgSysListActivity;
import cn.vko.ring.message.activity.RecommendMsgDetailActivity;
import cn.vko.ring.message.listener.IAuditResultListener;
import cn.vko.ring.message.model.AutoResultModel;
import cn.vko.ring.message.model.BaseMsgModel;
import cn.vko.ring.message.model.MsgItemModel;
import cn.vko.ring.message.presenter.AuditPresenter;
import cn.vko.ring.mine.model.MessageInfo;

/**
 * Created by Agree on 2016/11/21.
 */
public class SessionMsgAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = BaseMsgModel.MSG_GROUP_INFO + 1;
    private List<MsgItemModel> msgLists;
    private Context mContext;
    private LayoutInflater inflater;
    private AuditPresenter auditPresenter;
    private VolleyUtils<BaseResultInfo> mVolleyUtils;
    private Activity act;
    /* 首页有系统消息=1，否则2 */
    private int type = 1;
    MyListView listview;

    private UserInfo user;

    public SessionMsgAdapter(List<MsgItemModel> msgLists, Context context, Activity act, int type, MyListView listview) {
        super();
        this.msgLists = msgLists;
        this.mContext = context;
        this.listview = listview;
        this.act = act;
        this.type = type;
        mVolleyUtils = new VolleyUtils<BaseResultInfo>(context,BaseResultInfo.class);
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        UserInfo user = VkoContext.getInstance(act).getUser();
        if (user!=null && user.getIsSchoolUser() == 1){
            if (msgLists.size()<=4){
                return msgLists == null ? 0 : msgLists.size();
            }else {
                return msgLists == null ? 0 : 4;
            }
        }else{
            return msgLists == null ? 0 : msgLists.size();
        }

    }
    /*
     * @Description: TODO
     */
    @Override
    public int getItemViewType(int position) {
        if (type == 1) {
			/* 消息首页 */
            if (position == 0) {
                return BaseMsgModel.MSG_SYS_ICON;
            } else {
                return BaseMsgModel.MSG_LINK_TEXT;
            }
        } else {
			/* 系统消息处理 */
            MsgItemModel msgmodel = msgLists.get(position);
            if (TextUtils.isEmpty(msgmodel.getGotoo())) {
                return BaseMsgModel.MSG_GROUP_INFO;
            }
            switch (Integer.valueOf(msgmodel.getGotoo())) {
                case 1:
                    return BaseMsgModel.MSG_GROUP_AUDIT;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return BaseMsgModel.MSG_GROUP_INFO;
            }
        }
        return BaseMsgModel.MSG_LINK_TEXT;
    }
    /*
     * @Description: TODO
     */
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return VIEW_TYPE_COUNT;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return msgLists.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    ViewHolder viewHolder;
    ViewHolderSys viewHolderSys;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MsgItemModel item = msgLists.get(position);
        int type = getItemViewType(position);
        convertView = initItemView(convertView, parent, type);
        deal(item, type);
        return convertView;
    }
    /**
     * @Title: sureCall
     * @Description: 确定1 拒绝 2
     * @param item
     * @return: void
     */
    private void sureOrRefuseCall(final MsgItemModel item, final int deal) {
        auditPresenter = new AuditPresenter(mContext, item);
        auditPresenter.doOption(deal);
        auditPresenter.setOnAuditListener(new IAuditResultListener() {
            @Override
            public void auditResult(AutoResultModel autoResultModel) {
                String msg = "操作";
                int handle = 0;
                if (autoResultModel != null) {
                    if (autoResultModel.getData()) {
                        msg += "成功";
                        handle = deal;
                    } else {
                        msg += "失败";
                    }
                    int pos = msgLists.indexOf(item);
                    item.setHandle(handle);
                    updataView(pos, listview, item);
//					CommonUtil.showMsg(act, msg);
                    Toast.makeText(act,msg,Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(Constants.GROUP_AUDIT_REFRESH);
                }
            }
        });
    }
    /**
     * @Title: goToDetail
     * @Description: 消息详情
     * @param item
     * @param type
     * @return: void
     */
    private void goToDetail(MsgItemModel item, int type) {
        switch (type) {
            case BaseMsgModel.MSG_SYS_ICON:

				/* 进入系统消息 */
                if (!VkoContext.getInstance(mContext).doLoginCheckToSkip(act)) {
                    StartActivityUtil.startActivity(mContext, MsgSysListActivity.class);
                }
                break;
            case BaseMsgModel.MSG_LINK_TEXT:
				/* 进入推荐消息详情 */
                goToLinkMsgDetail(item);
                break;
            case BaseMsgModel.MSG_GROUP_AUDIT:
//				StartActivityUtil.startActivity(mContext, MsgGroupAuditActivity.class);
                break;
            case BaseMsgModel.MSG_GROUP_INFO:
				/* 和群相关，以及问题 需要跳转的处理 */
                dealNotGroupMsgSkip(item);
                dealIsGroupMsgSkip(item);
                break;
            default:
                break;
        }
    }
    /**
     *
     * @Title: dealIsGroupMsgSkip
     * @Description:处理和群相关的跳转
     * @param item
     * @return: void
     */
    private void dealIsGroupMsgSkip(MsgItemModel item) {
        if (!TextUtils.isEmpty(item.getGotoo())) {
            switch (Integer.parseInt(item.getGotoo())) {
                case 2:
                    gotoGroupDetail(item);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * @Title: dealNotGroupMsgSkip
     * @Description:处理非群信息的跳转
     * @param item
     * @return: void
     */
    private void dealNotGroupMsgSkip(MsgItemModel item) {
        switch (Integer.parseInt(item.getType())) {
            case 3:
                if (TextUtils.isEmpty(item.getQuestionId())) {
                    return;
                }
//				gotoProblemActivity(item);
                break;
            default:
                break;
        }
    }
    /**
     * 跳转到群页面
     * @Title: gotoGroupDetail
     * @Description: TODO
     * @param item
     * @return: void
     */
    private void gotoGroupDetail(MsgItemModel item) {
        Bundle bundle = new Bundle();
        bundle.putString("GROUPID", item.getGroupId());
        StartActivityUtil.startActivity(mContext, GroupDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
    private void goToLinkMsgDetail(MsgItemModel item) {
        MessageInfo info = new MessageInfo();

        if (!TextUtils.isEmpty(item.getLinkUrl())) {
            info.setLinkUrl(item.getLinkUrl());
        }

        if (!TextUtils.isEmpty(item.getTitle())) {
            info.setTitle(item.getTitle());
        }

        if (!TextUtils.isEmpty(item.getContent())) {
            info.setContent(item.getContent());
        }
        Bundle b = new Bundle();
        b.putSerializable("MESSAGE", info);
        b.putInt("TYPE", 1);
        StartActivityUtil.startActivity(mContext, RecommendMsgDetailActivity.class, b, Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    private void deal(final MsgItemModel item, final int type) {
        switch (type) {
            case BaseMsgModel.MSG_SYS_ICON:
                viewHolderSys.msgTitle.setText("系统消息");
                if (!TextUtils.isEmpty(item.getMsg())) {
                    viewHolderSys.msgInfo.setText(item.getMsg() + "");
                    viewHolderSys.msgTime.setText(item.getCtime() + "");
                }
                if (item.isNew()) {
                    viewHolderSys.msgNewIcon.setVisibility(View.GONE);
                } else {
                    viewHolderSys.msgNewIcon.setVisibility(View.GONE);
                }
                viewHolderSys.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDetail(item, type);
                    }
                });
                break;
            case BaseMsgModel.MSG_LINK_TEXT:
//                viewHolder.msgLeftIcon.setVisibility(View.GONE);
//                viewHolder.sfLay.setVisibility(View.GONE);
//                viewHolder.msgInfo.setSingleLine(false);
//                viewHolder.msgInfo.setMaxLines(2);
                viewHolder.msgTitle.setText(item.getTitle());
                viewHolder.msgInfo.setText(item.getContent());
                viewHolder.msgTime.setText(item.getCtime());
//                viewHolder.linkbg.setVisibility(View.VISIBLE);
//                if (!TextUtils.isEmpty(item.getImgUrl())) {
//                    ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.linkbg,
//                            R.drawable.msg_rec_bg, R.drawable.msg_rec_bg);
//                    mVolleyUtils.getImageLoader().get(item.getImgUrl(), listener);
//                } else {
//                    viewHolder.linkbg.setVisibility(View.GONE);
//                }
                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDetail(item, type);
                    }
                });
                break;
            case BaseMsgModel.MSG_GROUP_INFO:
//                viewHolder.msgLeftIcon.setVisibility(View.GONE);
//                viewHolder.msgTitle.setVisibility(View.GONE);
//                viewHolder.lineView.setVisibility(View.GONE);
//                viewHolder.msgDetail.setVisibility(View.GONE);
                viewHolder.msgInfo.setSingleLine(false);
                viewHolder.msgInfo.setTextColor(mContext.getResources().getColor(R.color.common_dark_text_color));
//                viewHolder.sfLay.setVisibility(View.GONE);
                // viewHolder.msgInfo.setMaxLines(2);
                viewHolder.msgInfo.setText(item.getMsg());
                viewHolder.msgTime.setText(item.getCtime());
                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDetail(item, type);
                    }
                });
                break;
            case BaseMsgModel.MSG_GROUP_AUDIT:
                viewHolder.msgLeftIcon.setVisibility(View.VISIBLE);
                viewHolder.msgTitle.setVisibility(View.VISIBLE);
//                viewHolder.lineView.setVisibility(View.GONE);
//                viewHolder.msgDetail.setVisibility(View.GONE);
                viewHolder.msgInfo.setSingleLine(false);
                dealSureAndRefuseView(viewHolder, item);
                viewHolder.msgTitle.setText(item.getGroupName());
                viewHolder.msgInfo.setText(item.getMsg());
                viewHolder.msgTime.setText(item.getCtime());
                if (!TextUtils.isEmpty(item.getImgUrl())) {
                    ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.msgLeftIcon,
                            R.drawable.default_group_head, R.drawable.default_group_head);
                    mVolleyUtils.getImageLoader().get(item.getImgUrl(), listener, 72, 72);
                } else {
                    viewHolder.msgLeftIcon.setImageResource(R.drawable.default_group_head);
                }
//                viewHolder.refuseBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sureOrRefuseCall(item, 2);
//                    }
//                });
//                viewHolder.sureBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sureOrRefuseCall(item, 1);
//                    }
//                });
//                viewHolder.msgDetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        goToDetail(item, type);
//                    }
//                });
                break;
        }
    }
    private void dealSureAndRefuseView(ViewHolder holder, final MsgItemModel item) {
//        if (item.getHandle() == 0) {
//            holder.sfLay.setVisibility(View.VISIBLE);
//            holder.tvRefuse.setVisibility(View.GONE);
//            holder.tvSure.setVisibility(View.GONE);
//        } else if (item.getHandle() == 1) {
//            holder.sfLay.setVisibility(View.GONE);
//            holder.tvSure.setVisibility(View.VISIBLE);
//            holder.tvRefuse.setVisibility(View.GONE);
//        } else if (item.getHandle() == 2) {
//            holder.sfLay.setVisibility(View.GONE);
//            holder.tvSure.setVisibility(View.GONE);
//            holder.tvRefuse.setVisibility(View.VISIBLE);
//        }
    }
    private View initItemView(View convertView, ViewGroup parent, int type) {
        if (convertView == null) {
            switch (type) {
                case BaseMsgModel.MSG_SYS_ICON:
                    viewHolderSys = new ViewHolderSys();
                    convertView = inflater.inflate(R.layout.item_session_mymsg1, null);
                    viewHolderSys.msgLeftIcon = (ImageView) convertView.findViewById(R.id.left_msg_icon1);
                    viewHolderSys.msgTitle = (TextView) convertView.findViewById(R.id.msg_sys_title);
                    viewHolderSys.msgTime = (TextView) convertView.findViewById(R.id.msg_sys_time);
                    viewHolderSys.msgInfo = (TextView) convertView.findViewById(R.id.msg_sys_detail);
                    viewHolderSys.msgNewIcon = (ImageView) convertView.findViewById(R.id.new_msg_point_icon);

                    viewHolderSys.container = convertView;
                    convertView.setTag(viewHolderSys);
                    break;
                case BaseMsgModel.MSG_LINK_TEXT:
                case BaseMsgModel.MSG_GROUP_INFO:
                case BaseMsgModel.MSG_GROUP_AUDIT:
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_session_mymsg1, parent, false);
                    viewHolder.msgTitle = (TextView) convertView.findViewById(R.id.msg_sys_title);
                    viewHolder.msgInfo = (TextView) convertView.findViewById(R.id.msg_sys_detail);
//                    viewHolder.tvSure = (TextView) convertView.findViewById(R.id.tv_sure);
//                    viewHolder.tvRefuse = (TextView) convertView.findViewById(R.id.tv_refuse);
                    viewHolder.msgTime = (TextView) convertView.findViewById(R.id.msg_sys_time);
                    viewHolder.msgLeftIcon = (ImageView) convertView.findViewById(R.id.left_msg_icon1);
                    viewHolderSys.msgNewIcon = (ImageView) convertView.findViewById(R.id.new_msg_point_icon);
//                    viewHolder.lineView = convertView.findViewById(R.id.line_view);
//                    viewHolder.refuseBtn = (Button) convertView.findViewById(R.id.btn_refuse);
//                    viewHolder.sureBtn = (Button) convertView.findViewById(R.id.btn_sure);
//                    viewHolder.msgDetail = (TextView) convertView.findViewById(R.id.goto_detail);
//                    viewHolder.sfLay = (LinearLayout) convertView.findViewById(R.id.sure_and_refuse_lay);
//                    viewHolder.linkbg = (ImageView) convertView.findViewById(R.id.link_img);
                    viewHolder.container = convertView;
                    convertView.setTag(viewHolder);
                    break;
            }
        } else {
            switch (type) {
                case BaseMsgModel.MSG_SYS_ICON:
                    viewHolderSys = (ViewHolderSys) convertView.getTag();
                    break;
                case BaseMsgModel.MSG_LINK_TEXT:
                case BaseMsgModel.MSG_GROUP_INFO:
                case BaseMsgModel.MSG_GROUP_AUDIT:
                    viewHolder = (ViewHolder) convertView.getTag();

                    break;
            }
        }
//        Log.e("===========>TYPE", String.valueOf(type));
        if (type == BaseMsgModel.MSG_SYS_ICON){
            viewHolderSys.msgLeftIcon.setImageResource(R.drawable.im_class_system);
        }else if (type == BaseMsgModel.MSG_LINK_TEXT){

            viewHolder.msgLeftIcon.setImageResource(R.drawable.im_class_push);
        }

        return convertView;
    }
    public void updataView(int posi, ListView listView, MsgItemModel item) {
        posi += 1;
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();
            dealSureAndRefuseView(holder, item);
        } else {
            notifyDataSetChanged();
        }
    }

    class ViewHolderSys {
        TextView msgTitle;
        TextView msgInfo;
        TextView msgTime;
        ImageView msgNewIcon;
        ImageView msgLeftIcon;
        View container;
    }

    class ViewHolder {
        TextView msgTitle;
        TextView msgInfo;
//        TextView tvSure;
//        TextView tvRefuse;
        TextView msgTime;
        ImageView msgLeftIcon;
//        View lineView;
//        Button refuseBtn;
//        Button sureBtn;
//        TextView msgDetail;
//        /* 确定与拒绝 */
//        LinearLayout sfLay;
        View container;
//        ImageView linkbg;
    }
}
