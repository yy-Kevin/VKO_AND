package cn.vko.ring;

import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.renderscript.Sampler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.netease.nim.uikit.ImageLoaderKit;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.contact.ContactProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.DownloadUtils;
import cn.shikh.utils.ImageCacheUtils;
import cn.vko.ring.common.listener.IMsgNotificationListener;
import cn.vko.ring.home.presenter.PushMessagePresenter;
import cn.vko.ring.im.common.util.sys.SystemUtil;
import cn.vko.ring.im.config.ExtraOptions;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.config.preference.UserPreferences;
import cn.vko.ring.im.main.activity.WelcomeActivity;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.session.activity.lupin.RecordService;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.NewTestAttachment;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.utils.ActivityUtilManager;
import cn.vko.ring.utils.CrashHandler;
import cn.vko.ring.utils.FileUtil;


public class VkoApplication extends Application {
	private static VkoApplication instance;
//		private UMSocialService mController;
	private PushAgent mPushAgent;
	private PolyvSDKClient client;
	private IMsgNotificationListener listener;
	private PushMessagePresenter mPushMessagePresenter;
	private String aeskey="VXtlHmwfS2oYm0CZ";
	private String iv="2u9gDPKdX6GyQJKU";


	private  static  boolean c=false;//是否有数据

	public static boolean getC() {
		return c;
	}

	public static void setC(boolean c) {
		VkoApplication.c = c;
	}





	public void onCreate() {
		super.onCreate();

		// 启动 Marvel service
		startService(new Intent(this, RecordService.class));
//		CrashHandler.getInstance().init(this);
		// ButterKnife.setDebug(BuildConfig.DEBUG);
		instance = this;
		VkoContext.getInstance(this);
		ImageCacheUtils.getInstance().init(this,FileUtil.getCacheDir());
		DownloadUtils.getInstance().setDownloadDir(FileUtil.getDownloadDir());
		initUM();
		initPolyvCilent();
		initIM();
		// 环信
//		 EaseHelper.getInstance().init(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	private void initIM() {
		DemoCache.setContext(this);
		NIMClient.init(this, getLoginInfo(), getOptions());
		ExtraOptions.provide();
		if (inMainProcess()) {
			// init pinyin
//            PinYin.init(this);
//            PinYin.validate();
			// 初始化UIKit模块
			initUIKit();
			// 初始化消息提醒
			NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
			// 注册语言变化监听
			registerLocaleReceiver(true);
		}
	}

	public static VkoApplication getInstance() {
		return instance;
	}

	private void initPolyvCilent() {
		if (client != null)
			return;
		client = PolyvSDKClient.getInstance();
		// client.init();
		client.setConfig("G0a1pFRUCdeVQChd2vy6A3K+kC2JiZrWd50rq24B7UdsEAUPP9pXRgTIMpubNR4VA+6ncRzNBY3TPbWhahS+AZZw4VuMLf82JWlcp2mMAbQAk4t3cra1aI4+LYKnlescohaYhV0iHiomOjeZlyaRLQ==",aeskey,iv);
//		client.setConfig("G0a1pFRUCdeVQChd2vy6A3K+kC2JiZrWd50rq24B7UdsEAUPP9pXRgTIMpubNR4VA+6ncRzNBY3TPbWhahS+AZZw4VuMLf82JWlcp2mMAbQAk4t3cra1aI4+LYKnlescohaYhV0iHiomOjeZlyaRLQ==");
		client.initDatabaseService(getApplicationContext());
		client.setDownloadDir(FileUtil.getDownloadDir());
		client.startService(getApplicationContext());

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		PolyvSDKClient client = PolyvSDKClient.getInstance();
		client.stopService(getApplicationContext());
	}

	private void initUM() {
		//各个平台的配置，建议放在全局Application或者程序入口
		PlatformConfig.setWeixin(Constants.WXAPPID, Constants.WXAPPSECRET);
		PlatformConfig.setQQZone(Constants.QQAPPID, Constants.QQAPPKEY);

		mPushAgent = PushAgent.getInstance(this);
		// mPushAgent.setDebugMode(true);
		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			@Override
			public Notification getNotification(Context arg0, UMessage arg1) {
				// TODO Auto-generated method stub
				// 广播通知
				Intent intent = new Intent();
				intent.setAction("action.refreshFriend");
				sendBroadcast(intent);

				Log.e("===========>","c是"+c );

				if (listener != null) {
					listener.onNotification(1);
				}
				return super.getNotification(arg0, arg1);
			}
		};
		mPushAgent.setMessageHandler(messageHandler);
		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {

				ActivityUtilManager.getInstance().startMActivity(getApplicationContext());
				mPushMessagePresenter = new PushMessagePresenter(context);
				mPushMessagePresenter.doMessage(msg);
				VkoApplication.setC(true);
				// 广播通知
//				Intent intent = new Intent();
//				intent.setAction("action.refreshFriend");
//				sendBroadcast(intent);
//
//				Log.e("===========>","c是"+c );
			}

			@Override
			public void launchApp(Context arg0, UMessage arg1) {
				// TODO Auto-generated method stub
				super.launchApp(arg0, arg1);
			}
		};


		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	/*public UMSocialService getUMController() {
		if(mController == null){
//			mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		}
		return mController;
	}*/

	public String getChannel() {
		return AnalyticsConfig.getChannel(getApplicationContext());
//		return ViewUtils.getAppMetaData(getApplicationContext(),"UMENG_CHANNEL");
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

	private SDKOptions getOptions() {
		SDKOptions options = new SDKOptions();

		// 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
		StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
		if (config == null) {
			config = new StatusBarNotificationConfig();
		}
		// 点击通知需要跳转到的界面
		config.notificationEntrance = WelcomeActivity.class;
		config.notificationSmallIconId = R.drawable.ic_logo;

		// 通知铃声的uri字符串
		config.notificationSound = "android.resource://cn.vko.ring/raw/msg";
		options.statusBarNotificationConfig = config;
		DemoCache.setNotificationConfig(config);
		UserPreferences.setStatusConfig(config);

		// 配置保存图片，文件，log等数据的目录
		String sdkPath = Environment.getExternalStorageDirectory() + "/vko" + "/nim";
		options.sdkStorageRootPath = sdkPath;

		// 配置数据库加密秘钥
		options.databaseEncryptKey = "NETEASE";

		// 配置是否需要预下载附件缩略图
		options.preloadAttach = true;

		// 配置附件缩略图的尺寸大小，
		options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

		// 用户信息提供者
		options.userInfoProvider = infoProvider;

		// 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
		options.messageNotifierCustomization = messageNotifierCustomization;

		return options;
	}
	public boolean inMainProcess() {
		String packageName = getPackageName();
		String processName = SystemUtil.getProcessName(this);
		return packageName.equals(processName);
	}

	/**
	 * 音视频通话配置与监听
	 */
	private void enableAVChat() {
//		setupAVChat();

	}

//	private void setupAVChat() {
//		AVChatRingerConfig config = new AVChatRingerConfig();
//		config.res_connecting = R.raw.avchat_connecting;
//		config.res_no_response = R.raw.avchat_no_response;
//		config.res_peer_busy = R.raw.avchat_peer_busy;
//		config.res_peer_reject = R.raw.avchat_peer_reject;
//		config.res_ring = R.raw.avchat_ring;
//		AVChatManager.getInstance().setRingerConfig(config); // 设置铃声配置
//	}


	private void registerLocaleReceiver(boolean register) {
		if (register) {
			updateLocale();
			IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
			registerReceiver(localeReceiver, filter);
		} else {
			unregisterReceiver(localeReceiver);
		}
	}

	private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
				updateLocale();
			}
		}
	};

	private void updateLocale() {
		NimStrings strings = new NimStrings();
		strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
		strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
		strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
		strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
		strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
		strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
		strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
		strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
		strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
		strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
		strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
		NIMClient.updateStrings(strings);
	}

	private void initUIKit() {
		// 初始化，需要传入用户信息提供者
		NimUIKit.init(this, infoProvider, contactProvider);



		// 会话窗口的定制初始化。
		SessionHelper.init();

//		// 通讯录列表定制初始化
//		ContactHelper.init();
	}

	private UserInfoProvider infoProvider = new UserInfoProvider() {
		@Override
		public UserInfo getUserInfo(String account) {
			UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
			if (user == null) {
				NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
			}
			return user;
		}

		@Override
		public int getDefaultIconResId() {
			return R.drawable.avatar_def;
		}

		@Override
		public Bitmap getTeamIcon(String teamId) {
			Drawable drawable = getResources().getDrawable(R.drawable.nim_avatar_group);
			if (drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable) drawable).getBitmap();
			}

			return null;
		}

		@Override
		public Bitmap getAvatarForMessageNotifier(String account) {
			/**
			 * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
			 */
			UserInfo user = getUserInfo(account);
			return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
		}

		@Override
		public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
			String nick = null;
			if (sessionType == SessionTypeEnum.P2P) {
				nick = NimUserInfoCache.getInstance().getAlias(account);
			} else if (sessionType == SessionTypeEnum.Team) {
				nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
				if (TextUtils.isEmpty(nick)) {
					nick = NimUserInfoCache.getInstance().getAlias(account);
				}
			}
			// 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
			if (TextUtils.isEmpty(nick)) {
				return null;
			}

			return nick;
		}
	};

	private ContactProvider contactProvider = new ContactProvider() {
		@Override
		public List<UserInfoProvider.UserInfo> getUserInfoOfMyFriends() {
			List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
			List<UserInfoProvider.UserInfo> users = new ArrayList<>(nimUsers.size());
			if (!nimUsers.isEmpty()) {
				users.addAll(nimUsers);
			}

			return users;
		}

		@Override
		public int getMyFriendsCount() {
			return FriendDataCache.getInstance().getMyFriendCounts();
		}

		@Override
		public String getUserDisplayName(String account) {
			return NimUserInfoCache.getInstance().getUserDisplayName(account);
		}
	};

	private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
		@Override
		public String makeNotifyContent(String nick, IMMessage message) {
			if (message!=null){
				MsgAttachment attachment = message.getAttachment();
				if (attachment!=null && attachment instanceof CourseAttachment){
					return "课程预习";
				}
				if (attachment!=null && attachment instanceof NewTestAttachment){
					return "作业测评";
				}

			}
			return null; // 采用SDK默认文案
		}

		@Override
		public String makeTicker(String nick, IMMessage message) {
			return null; // 采用SDK默认文案
		}
	};
}
