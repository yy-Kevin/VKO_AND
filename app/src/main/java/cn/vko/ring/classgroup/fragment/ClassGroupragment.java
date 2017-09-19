package cn.vko.ring.classgroup.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.reminder.ReminderManager;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.recent.RecentContactsCallback;
import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.activity.CircleHomeActivity;
import cn.vko.ring.classgroup.activity.MyClassGroupActivity;
import cn.vko.ring.classgroup.adapter.UserTeamAdapter;
import cn.vko.ring.classgroup.model.UserGroupTeamData;
import cn.vko.ring.classgroup.presenter.GetUserGroupTeamPresenterImp;
import cn.vko.ring.classgroup.presenter.SessionMsgPresenter;
import cn.vko.ring.classgroup.view.UserTeamView;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.widget.pop.ClassGroupPop;
import cn.vko.ring.common.widget.xlv.MyListView;
import cn.vko.ring.home.activity.AdActivity;
import cn.vko.ring.home.activity.ScanActivity;
import cn.vko.ring.home.model.AD;
import cn.vko.ring.home.presenter.AdPresenter;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.login.LogoutHelper;
import cn.vko.ring.im.main.activity.MultiportActivity;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.NewTestAttachment;
import cn.vko.ring.im.session.extension.RemindAttachment;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.mine.activity.RecommendActivity;
import cn.vko.ring.study.presenter.CusetemPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassGroupragment extends BaseFragment implements UserTeamView {

    @Bind(R.id.iv_session_ad)
    ImageView ivAd;
    @Bind(R.id.status_desc_label)
    TextView statusDescLabel;
    @Bind(R.id.status_notify_bar)
    LinearLayout statusNotifyBar;
    @Bind(R.id.multiport_desc_label)
    TextView multiportDescLabel;
    @Bind(R.id.multiport_notify_bar)
    LinearLayout multiportNotifyBar;
    @Bind(R.id.my_class_btn)
    TextView myClassBtn;
    @Bind(R.id.my_class_group)
    ListView myClassGroup;
    @Bind(R.id.session_btn)
    TextView sessionBtn;
    @Bind(R.id.messages_fragment)
    LinearLayout messagesFragment;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.imagebtn)
    ImageView imagebtn;
    @Bind(R.id.my_group)
    ImageView my_group;
    @Bind(R.id.session_relative)
    RelativeLayout session_relative;
    @Bind(R.id.session_msglistview)
    MyListView msgListView;
    @Bind(R.id.empty)
    View empty;

    private ClassGroupPop pop;
    private Drawable dewUp,dewDown;

    SessionMsgPresenter listPresenter;

    private VkoContext vkoContext;


    private View notifyBar;

    private TextView notifyBarText;

    // 同时在线的其他端的信息
    private List<OnlineClient> onlineClients;

    private View multiportBar;
    private RecentContactsFragment fragment;
    private GetUserGroupTeamPresenterImp getUserGroupTeamPresenterImp;
    private static final String TAG = "ClassGroupragment";
    private UserTeamAdapter userTeamAdapter;
    private List<UserGroupTeamData.DataEntity> teamData;

    private int i = 3;

    @Override
    public int setContentViewId() {
        return R.layout.session_list;

    }



    // broadcast receiver
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend"))
            {
                Log.e("========>","接受成功了@！");
                listPresenter.refreshData();
            }
        }
    };

    @Override
    public void initView(View root) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);


        findViews(root);
        tvTitle.setText("动态");
//        tvRight.setText("+");
//        tvRight.setTextSize(20);
        tvRight.setVisibility(View.VISIBLE);
        toggle(false);
        tvRight.setCompoundDrawablePadding(10);
        ivBack.setVisibility(View.GONE);
        myClassGroup.setVisibility(View.GONE);
        registerObservers(true);
        addRecentContactsFragment();
        teamData = new ArrayList<>();
        myClassGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (teamData.size() > position)
                    checkedImToken();
                    SessionHelper.startTeamSession(getActivity(), teamData.get(position).getTid() + "");
            }
        });
    }
    public void toggle(boolean isOpen) {
        isOpen = !isOpen;
        if (isOpen) {
            switchDrawable(tvRight, dewUp);
        } else {
            switchDrawable(tvRight, dewDown);
        }
    }
    public void switchDrawable(TextView tv, Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(0, 100, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setCompoundDrawables(null, null, drawable, null);// 画在右边
    }

    @OnClick(R.id.tv_right)
    void onFilterDteail(){
        toggle(true);
        if (pop == null) {
            pop = new ClassGroupPop(getActivity());
            pop.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String filter = "-1";
                    if(view.getId() == R.id.tv_yaoq){
                        filter = "1";
//                        tvRight.setText("邀请好友");
                        StartActivityUtil.startActivity(atx, RecommendActivity.class);
                    }else if(view.getId() ==R.id.tv_sao){
                        filter = "-1";
//                        tvRight.setText("扫一扫");
                        StartActivityUtil.startActivity(atx, ScanActivity.class);
                    }else{
                        tvRight.setText("+");
                    }
//                    getToWhere(filter);
                    pop.dismiss();
                }
            });
            pop.update(0, 0, ViewUtils.getScreenWidth(getActivity())/ 4,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggle(false);
                }
            });
        }
        pop.showAsDropDown(tvRight);
    }

    @OnClick(R.id.my_group)
    void toMyGroup(){
        StartActivityUtil.startActivity(atx, MyClassGroupActivity.class);
    }

    @Override
    public void initData() {
        super.initData();
        getUserTeem();
        initIM();

        listPresenter = new SessionMsgPresenter(getContext(), msgListView, getActivity(), 1,empty);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                getAd();
            }
        }, 1000);

    }
    private void getAd() {
        // TODO Auto-generated method stub
        new AdPresenter(atx).getAds(callBack);
    }
    AdPresenter.ADCallBack callBack = new AdPresenter.ADCallBack() {

        @Override
        public void onAdResult(AD.ADactivity.ADitem item) {
            // TODO Auto-generated method stub
            if (item != null && !TextUtils.isEmpty(item.getActId())) {
                dealAd();
            }
        }
    };


    public void dealAd() {
        if(TextUtils.isEmpty(VkoConfigure
                .getConfigure(getActivity()).getString(AD.AD_START_TIME))||TextUtils.isEmpty(VkoConfigure.getConfigure(getActivity())
                .getString(AD.AD_END_TIME))){
            return;
        }
        long startTime = Long.parseLong(VkoConfigure
                .getConfigure(getActivity()).getString(AD.AD_START_TIME));
        long endTime = Long.parseLong(VkoConfigure.getConfigure(getActivity())
                .getString(AD.AD_END_TIME));
        long nowTime = System.currentTimeMillis();
        if(ivAd==null)return;
        if (nowTime > startTime && nowTime < endTime) {
            ivAd.setVisibility(View.VISIBLE);
            refreshAdState();
        } else {
            ivAd.setVisibility(View.GONE);
        }
    }

    public void refreshAdState() {
        if (!VkoConfigure.getConfigure(getActivity()).getBoolean(Constants.HAS_LOOK, false)) {
            ivAd.setImageResource(R.drawable.ad_point);
        }else{
            ivAd.setImageResource(R.drawable.ad_no_point);
        }
    }




    private void checkedImToken() {
        if(VkoContext.getInstance(atx).getUser().getImToken() != null){
            if(DemoCache.getAccount() == null){
                if(getLoginInfo() == null){
                    new CusetemPresenter(atx);
                }
            }
        }else{
            new CusetemPresenter(atx);
        }
    }
    public LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }
    private void initIM() {
        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                DialogMaker.dismissProgressDialog();
            }
        });

//		Log.i(TAG, "sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(atx, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        }

    }
    private void getUserTeem() {
        if (getUserGroupTeamPresenterImp == null) {
            getUserGroupTeamPresenterImp = new GetUserGroupTeamPresenterImp(getActivity(), this);
        }else {
            getUserGroupTeamPresenterImp.getUserTeam();
        }
    }

    @OnClick(R.id.session_relative)
    public void getToCircle(){
        StartActivityUtil.startActivity(atx, CircleHomeActivity.class);
    }

    @OnClick(R.id.iv_session_ad)
    public void onAdClicked(){
        StartActivityUtil.startActivity(getActivity(), AdActivity.class);
    }

    @OnClick(R.id.my_class_btn)
    public void onMyClassClicked() {
        Log.d(TAG, "onMyClassClicked() called with: " + "");
        if (myClassGroup.getVisibility() == View.VISIBLE) {
            myClassGroup.setVisibility(View.GONE);

        } else {
            if (teamData.size() == 0) {
                getUserTeem();
            }
            myClassGroup.setVisibility(View.VISIBLE);


        }


    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();

//        LoginActivity.start(getActivity(), true);
//        getActivity().finish();
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    private void findViews(View view) {
        notifyBar = view.findViewById(R.id.status_notify_bar);
        notifyBarText = (TextView) view.findViewById(R.id.status_desc_label);
        notifyBar.setVisibility(View.GONE);

        multiportBar = view.findViewById(R.id.multiport_notify_bar);
        multiportBar.setVisibility(View.GONE);
        multiportBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiportActivity.startActivity(getActivity(), onlineClients);
            }
        });
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.net_broken);
                } else if (code == StatusCode.UNLOGIN) {
//                    notifyBar.setVisibility(View.VISIBLE);
//                    notifyBarText.setText(R.string.nim_status_unlogin);
//                    LoginImPresenter.getInstance(getActivity()).logInWYIm();
                } else {
                    notifyBar.setVisibility(View.GONE);
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        Preferences.saveUserToken("");

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
            if (getActivity() != null){
//                Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            Log.d(TAG, "onEvent: ");
            ClassGroupragment.this.onlineClients = onlineClients;
            if (onlineClients == null || onlineClients.size() == 0) {

                multiportBar.setVisibility(View.GONE);
            } else {
                multiportBar.setVisibility(View.VISIBLE);
                TextView status = (TextView) multiportBar.findViewById(R.id.multiport_desc_label);
                OnlineClient client = onlineClients.get(0);
                switch (client.getClientType()) {
//                    case ClientType.Windows:
//                        status.setText(getString(R.string.multiport_logging) + getString(R.string.computer_version));
//                        break;
//                    case ClientType.Web:
//                        status.setText(getString(R.string.multiport_logging) + getString(R.string.web_version));
//                        break;
//                    case ClientType.iOS:
//                    case ClientType.Android:
//                        status.setText(getString(R.string.multiport_logging) + getString(R.string.mobile_version));
//                        break;
                    default:
                        multiportBar.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();

        fragment.setContainerId(R.id.messages_fragment);


//        final TActionBarActivity activity = (TActionBarActivity) getActivity();
//
//        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
//        fragment = (RecentContactsFragment) activity.addFragment(fragment);

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
                Log.d(TAG, "onRecentContactsLoaded: ");
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                Log.d(TAG, "onUnreadCountChange: ");
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        SessionHelper.startP2PSession(getActivity(), recent.getContactId());
                        break;
                    case Team:
                        SessionHelper.startTeamSession(getActivity(), recent.getContactId());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public String getDigestOfAttachment(MsgAttachment attachment) {
                Log.d(TAG, "getDigestOfAttachment: ");
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。

              if (attachment instanceof CourseAttachment) {
                    return "[课程预习]";
                }else if (attachment instanceof RemindAttachment) {
                    return "[任务提醒]";
                }
              else if (attachment instanceof NewTestAttachment) {
                  return "[练习测评]";
              }

                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                Toast.makeText(getActivity(), recent.getFromAccount() + "", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "getDigestOfTipMsg() called with: " + "recent = [" + recent + "]");
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }

                return null;
            }
        });
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.messages_fragment, fragment);
        transaction.commit();
    }

    @Override
    public void getUserTeam(final UserGroupTeamData userGroupTeamData) {

        Log.e("TAG", "getUserTeam() called with: " + "userGroupTeamData = [" + userGroupTeamData + "]");
        if (userGroupTeamData != null && userGroupTeamData.getData() != null) {

            teamData.addAll(userGroupTeamData.getData());
            userTeamAdapter = new UserTeamAdapter(getActivity());
            userTeamAdapter.setList(userGroupTeamData.getData());
            myClassGroup.setAdapter(userTeamAdapter);
            userTeamAdapter.notifyDataSetChanged();
        } else {
//            i--;
//            if(i>=0)
//            getUserTeem();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);

    }
}
