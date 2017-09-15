package cn.vko.ring.im.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.uikit.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.team.model.TeamExtras;
import com.netease.nim.uikit.team.model.TeamRequestCode;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.activity.TaskListActivity;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.im.contact.activity.UserProfileActivity;
import cn.vko.ring.im.session.action.CourseAction;
import cn.vko.ring.im.session.action.FileAction;
import cn.vko.ring.im.session.action.LuPinAction;
import cn.vko.ring.im.session.action.TaskAction;
import cn.vko.ring.im.session.action.TipAction;
import cn.vko.ring.im.session.activity.MessageInfoActivity;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.CustomAttachParser;
import cn.vko.ring.im.session.extension.CustomAttachment;
import cn.vko.ring.im.session.extension.NewTestAttachment;
import cn.vko.ring.im.session.extension.RemindAttachment;
import cn.vko.ring.im.session.extension.StickerAttachment;
import cn.vko.ring.im.session.extension.TaskAttachment;
import cn.vko.ring.im.session.viewholder.MsgViewHolderCourse;
import cn.vko.ring.im.session.viewholder.MsgViewHolderDefCustom;
import cn.vko.ring.im.session.viewholder.MsgViewHolderFile;
import cn.vko.ring.im.session.viewholder.MsgViewHolderRemind;
import cn.vko.ring.im.session.viewholder.MsgViewHolderSticker;
import cn.vko.ring.im.session.viewholder.MsgViewHolderTask;
import cn.vko.ring.im.session.viewholder.MsgViewHolderTest;
import cn.vko.ring.im.session.viewholder.MsgViewHolderTip;
import cn.vko.ring.im.utils.DemoCache;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {

    private static final int ACTION_HISTORY_QUERY = 0;
    private static final int ACTION_SEARCH_MESSAGE = 1;
    private static final int ACTION_CLEAR_MESSAGE = 2;
    private static final int ACTION_TASK_LIST = 3;
    private static final int ACTION_GROUP_MEMBER = 4;

    private static SessionCustomization p2pCustomization;
    private static SessionCustomization teamCustomization;
    private static SessionCustomization myP2pCustomization;

    private static NIMPopupMenu popupMenu;
    private static List<PopupMenuItem> menuItemList;
    private static Context mContext;
    private static final String TAG = "SessionHelper";

    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();
    }

    public static void startP2PSession(Context context, String account) {
        if (!DemoCache.getAccount().equals(account)) {
            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getP2pCustomization());
        } else {
            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getMyP2pCustomization());
        }
    }

    public static void startTeamSession(Context context, String tid) {
        mContext = context;
        NimUIKit.startChatting(context, tid, SessionTypeEnum.Team, getTeamCustomization());
    }

    // 打开群聊界面(用于 UIKIT 中部分界面跳转回到指定的页面)
    public static void startTeamSession(Context context, String tid, Class<? extends Activity> backToClass) {
        NimUIKit.startChatting(context, tid, SessionTypeEnum.Team, getTeamCustomization(), backToClass);
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    private static SessionCustomization getP2pCustomization() {
        if (p2pCustomization == null) {
            p2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(activity, requestCode, resultCode, data);

                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
//            actions.add(new AVChatAction(AVChatType.AUDIO));
//            actions.add(new AVChatAction(AVChatType.VIDEO));
//            actions.add(new RTSAction());
//            actions.add(new SnapChatAction());
//            actions.add(new GuessAction());
//            actions.add(new FileAction());
//            actions.add(new TipAction());
//            actions.add(new CourseAction());
//            actions.add(new TestAction());
            p2pCustomization.actions = actions;
            p2pCustomization.withSticker = true;

            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
            SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.P2P);
                }
            };
            cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;

            SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    MessageInfoActivity.startActivity(context, sessionId); //打开聊天信息
                }
            };

            infoButton.iconId = R.drawable.nim_ic_message_actionbar_p2p_add;


            SessionCustomization.OptionsButton plusButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.Team);
                }
            };
            plusButton.iconId = R.drawable.add_icon;

//            buttons.add(cloudMsgButton);
//            buttons.add(infoButton);
//            buttons.add(plusButton);
//            p2pCustomization.buttons = buttons;
        }

        return p2pCustomization;
    }

    /**
     * 单人聊天
     *
     * @return
     */
    private static SessionCustomization getMyP2pCustomization() {
        if (myP2pCustomization == null) {
            myP2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                    if (requestCode == TeamRequestCode.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                        String result = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                        if (result == null) {
                            return;
                        }
                        if (result.equals(TeamExtras.RESULT_EXTRA_REASON_CREATE)) {
                            String tid = data.getStringExtra(TeamExtras.RESULT_EXTRA_DATA);
                            if (TextUtils.isEmpty(tid)) {
                                return;
                            }

                            startTeamSession(activity, tid);
                            activity.finish();
                        }
                    }
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
//            actions.add(new SnapChatAction());
//            actions.add(new GuessAction());
//            actions.add(new FileAction());
//            actions.add(new CourseAction());
//            actions.add(new TestAction());
//            myP2pCustomization.actions = actions;
//            myP2pCustomization.withSticker = true;
            // 定制ActionBar右边的按钮，可以加多个
//            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
//            SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
//                @Override
//                public void onClick(Context context, View view, String sessionId) {
//                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.P2P);
//                }
//            };
//
//            cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;

//            buttons.add(cloudMsgButton);
//            myP2pCustomization.buttons = buttons;
        }
        return myP2pCustomization;
    }

    /**
     * 群操作
     *
     * @return
     */
    private static SessionCustomization getTeamCustomization() {

        if (teamCustomization == null) {
            teamCustomization = new SessionCustomization() {
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                    if (requestCode == TeamRequestCode.REQUEST_CODE) {
                        if (resultCode == Activity.RESULT_OK) {
                            String reason = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                            boolean finish = reason != null && (reason.equals(TeamExtras
                                    .RESULT_EXTRA_REASON_DISMISS) || reason.equals(TeamExtras.RESULT_EXTRA_REASON_QUIT));
                            if (finish) {
                                activity.finish(); // 退出or解散群直接退出多人会话
                            }
                        }
                    }
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
//            actions.add(new GuessAction());

            try {
                UserInfo user = VkoContext.getInstance(mContext).getUser();
                if (user!=null){
                    Log.d(TAG, "user called with: " + user.getIsTeacher());
                    if (user.getIsTeacher().equals("true")) {
                        actions.add(new FileAction());

                        actions.add(new CourseAction());
//                        actions.add(new TipAction());
                        actions.add(new LuPinAction());
//                        actions.add(new TaskAction());

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            teamCustomization.actions = actions;

            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
//            SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
//                @Override
//                public void onClick(Context context, View view, String sessionId) {
//                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.Team);
//                }
//            };
//            cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;
//
//            SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
//                @Override
//                public void onClick(Context context, View view, String sessionId) {
//                    Team team = TeamDataCache.getInstance().getTeamById(sessionId);
//                    if (team != null && team.isMyTeam()) {
//                        NimUIKit.startTeamInfo(context, sessionId);
//                    } else {
//                        Toast.makeText(context, R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };
//            infoButton.iconId = R.drawable.nim_ic_message_actionbar_team;

            SessionCustomization.OptionsButton plusButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    initPopuptWindow(context, view, sessionId, SessionTypeEnum.Team);
                }
            };
            plusButton.iconId = R.drawable.add_icon;
//            buttons.add(cloudMsgButton);
//            buttons.add(infoButton);
            buttons.add(plusButton);
            teamCustomization.buttons = buttons;

            teamCustomization.withSticker = true;
        }

        return teamCustomization;
    }

    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(FileAttachment.class, MsgViewHolderFile.class);
//        NimUIKit.registerMsgItemViewHolder(AVChatAttachment.class, MsgViewHolderAVChat.class);
//        NimUIKit.registerMsgItemViewHolder(GuessAttachment.class, MsgViewHolderGuess.class);
        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, MsgViewHolderDefCustom.class);
        NimUIKit.registerMsgItemViewHolder(StickerAttachment.class, MsgViewHolderSticker.class);
//        NimUIKit.registerMsgItemViewHolder(SnapChatAttachment.class, MsgViewHolderSnapChat.class);
//        NimUIKit.registerMsgItemViewHolder(RTSAttachment.class, MsgViewHolderRTS.class);
        NimUIKit.registerMsgItemViewHolder(CourseAttachment.class, MsgViewHolderCourse.class);
        NimUIKit.registerMsgItemViewHolder(TaskAttachment.class, MsgViewHolderTask.class);
        NimUIKit.registerMsgItemViewHolder(NewTestAttachment.class, MsgViewHolderTest.class);
        NimUIKit.registerMsgItemViewHolder(RemindAttachment.class, MsgViewHolderRemind.class);
//        NimUIKit.registerMsgItemViewHolder(TestAttachment.class, MsgViewHolderTest.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面
                UserProfileActivity.start(context, message.getFromAccount());
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };

        NimUIKit.setSessionListener(listener);
    }

    private static void initPopuptWindow(Context context, View view, String sessionId, SessionTypeEnum sessionTypeEnum) {
        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(context, menuItemList, listener);
        }
        menuItemList.clear();
        menuItemList.addAll(getMoreMenuItems(context, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private static NIMPopupMenu.MenuItemClickListener listener = new NIMPopupMenu.MenuItemClickListener() {
        @Override
        public void onItemClick(final PopupMenuItem item) {
            switch (item.getTag()) {
//                case ACTION_HISTORY_QUERY:
//                    MessageHistoryActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum()); // 漫游消息查询
//                    break;
//                case ACTION_SEARCH_MESSAGE:
//                    SearchMessageActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum());
//                    break;
//                case ACTION_CLEAR_MESSAGE:
//                    EasyAlertDialogHelper.createOkCancelDiolag(item.getContext(), null, "确定要清空吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
//                        @Override
//                        public void doCancelAction() {
//
//                        }
//
//                        @Override
//                        public void doOkAction() {
//                            NIMClient.getService(MsgService.class).clearChattingHistory(item.getSessionId(), item.getSessionTypeEnum());
//                            MessageListPanelHelper.getInstance().notifyClearMessages(item.getSessionId());
//                        }
//                    }).show();
//                    break;

                case ACTION_TASK_LIST:
//                    MessageHistoryActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum()); // 漫游消息查询

                    Intent i = new Intent(item.getContext(), TaskListActivity.class);
                    i.putExtra("SessionId", item.getSessionId());
                    item.getContext().startActivity(i);
                    break;
                case ACTION_GROUP_MEMBER:
                    Team team = TeamDataCache.getInstance().getTeamById(item.getSessionId());
                    if (team != null && team.isMyTeam()) {
                        NimUIKit.startTeamInfo(item.getContext(), item.getSessionId());
                    } else {
                        Toast.makeText(item.getContext(), R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private static List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, SessionTypeEnum sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
//        moreMenuItems.add(new PopupMenuItem(context, ACTION_HISTORY_QUERY, sessionId,
//                sessionTypeEnum, DemoCache.getContext().getString(R.string.message_history_query)));
//        moreMenuItems.add(new PopupMenuItem(context, ACTION_SEARCH_MESSAGE, sessionId,
//                sessionTypeEnum, DemoCache.getContext().getString(R.string.message_search_title)));
//        moreMenuItems.add(new PopupMenuItem(context, ACTION_CLEAR_MESSAGE, sessionId,
//                sessionTypeEnum, DemoCache.getContext().getString(R.string.message_clear)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_TASK_LIST, R.drawable.task_list, sessionId,
                sessionTypeEnum, DemoCache.getContext().getString(R.string.message_task_list)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_GROUP_MEMBER, R.drawable.im_group_member, sessionId,
                sessionTypeEnum, DemoCache.getContext().getString(R.string.message_group_member)));
        return moreMenuItems;
    }

    public static void reset(){
        teamCustomization=null;
        p2pCustomization=null;
    }
}
