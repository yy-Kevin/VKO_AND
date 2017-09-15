package cn.vko.ring;

import java.util.HashMap;
import java.util.Map;


public class Constants{

	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
	public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
	
	//开通会员
	public static final String OPEN_MEMBER = "Open_member";
	//购买精讲视频
	public static final String PAY_VIDEO = "pay_video";
	//取消订单
	public static String ORDER_CANCEL = "cancel_orde";
	//支付完成（除了开通会员）
	public static String PAYMENT_COMPLETE= "payment_complete";
	//微信支付完成
	public static String WX_PAYMENT_COMPLETE= "wx_payment_complete";
	//提现完成
	public static String TX_OVER= "tx_finish";
	
	public static Map<String, String> LEARN_MAP = initLearnMap();

	private static Map<String, String> initLearnMap() {
		// TODO Auto-generated method stub
		Map<String, String> learnMap = new HashMap<String, String>();
		learnMap.put("52", "初中");
		learnMap.put("51", "高中");
		return learnMap;
	}

	public static String SID = "01000000";
	public static String USERNAME = "userName";//
	// public static String TOKEN = "loginToken";//
	public static String PWD = "passWord";//
	
	//解锁章节
	public static String LOCKCHAPTER = "lockchapter";
	//购买本地课
	public static String PAYLOCALCOURSE = "pay_local_course";
			
	public static String REFRESH_MSG_LIST = "MSG_LIST";
	public static String REFRESH_SYNCTEST_DATA = "REFRESH_SYNCTEST_DATA";
	/** 课程列表星星刷新 */
	public static String REFRESH_COURSE_STAR_DATA = "REFRESH_COURSE_STAR_DATA";
	public static String REFRESH_SYNCTEST_SYNC_DATA = "REFRESH_SYNCTEST_SYNC_DATA";
	public static String REFRESH_VIEWPAGER_DATA = "REFRESH_VIEWPAGER_DATA";
	/** 看课进度刷新 */
	public static String REFRESH_SYNCTEST_SYNC_DATA_VIDEO = "REFRESH_SYNCTEST_SYNC_DATA_VIDEO";
	
	public static String REFRESH_SUBJECT_SYNC_DATA_VIDEO = "REFRESH_SUBJECT_SYNC_DATA_VIDEO";
	public static String REFRESH_SUBJECT_VERISION = "REFRESH_SUBJECT_VERISION";
	public static String REFRESH_RESTORE = "REFRESH_RESTORE";
	/**
	 * @fieldName: ISFIRSTSTART
	 * @fieldType: String
	 * @Description: TODO
	 */
	public static final String ISFIRSTSTART = "ISFIRSTSTART";
	public static final String STAR_REFRESH = "star_refresh";
	public static final String SUBJECT_REFRESH = "subject_refresh";
	public static final String LOGIN_REFRESH = "login_refresh";
	public static final String EXAM_RESULT_FINISH = "EXAM_RESULT_FINISH";
	public static final String SHOW_COURSE = "showcourse";
	public static final String TYPESYNC = "TYPESYNC";
	public static final String TYPEIMP = "TYPEIMP";
	public static final String TYPECOMP = "TYPECOMP";
	public static final String TYPESYNCVIDEONUM = "TYPESYNCVIDEONUM";
	public static final String TYPEIMPVIDEONUM = "TYPEIMPVIDEONUM";
	public static final String TYPECOMPVIDEONUM = "TYPECOMPVIDEONUM";
	public static final int SYNC_IMPR_COMP = 0x100;
	public static final String SCREENSHORT = "/screenshot.jpg";
	public static final String STATENOTIFACATION = "NOTIFACATION";
	public static final String STATEDOWN = "DOWN";
	public static final String STATEWATCH = "WATCH";
	public static final String DELETECOLOR = "DELETECOLOR";
	public static final String CANCLE_COLLECT = "CANCLE_COLLECT";
	public static final String ACTION_DEAL_AD= "action_deal_ad";
	/**
	 * 是否看过广告
	 */
	public static final String HAS_LOOK = "has_look";
	// 友盟
	public static final String YOUMENG_APPKEY = "5575284c67e58e9cbf000ce5";
	public static final String YOUMENG_SHARE = "com.umeng.share";
	// appID在微信开发平台注册应用的AppID
	public static final String WXAPPID = "wxebbcb968c1531360";
	public static final String WXAPPSECRET = "ba3864d8062783ff3aba62142c657460";
	// qqID 在qq开放平台注册的appid
	public static final String QQAPPID = "1104622575";
	public static final String QQAPPKEY = "BpHAfCFQAYeOjZzs";
	// 分享地址
	public static final String FXURL = "http://m.vko.cn/reg.html?adid=";
	// public static final String FXURL = "http://www.vko.cn/";
	public static final String SHAREWINQQ = "http://m.vko.cn/play.html?";
	public static final String JIFEN = "http://static.vko.cn/m/intro/integral.html";
	public static final String DENGJI = "http://static.vko.cn/m/intro/rating.html";
	public static final String RECOMMOND_REFRESH = "recommond_knowledge_refresh";
	public static final String GROUP_AUDIT_REFRESH = "group_audit_refresh";
	
	//环信
	
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String ACCOUNT_CONFLICT = "conflict";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_MSGTYPE = "msgtype";
	
	public static final String WEICHAT_MSG = "weichat";

//	public static final String DEFAULT_COSTOMER_APPKEY = "sipsoft#sandbox";
//	public static final String DEFAULT_COSTOMER_ACCOUNT = "yuanhui";
//	public static final String DEFAULT_ACCOUNT_PWD = "123456";

	public static final int MESSAGE_TO_DEFAULT = 0;
	public static final int MESSAGE_TO_PRE_SALES = 1;
	public static final int MESSAGE_TO_AFTER_SALES = 2;
	public static final String MESSAGE_TO_INTENT_EXTRA = "message_to";

	public static final String INTENT_CODE_IMG_SELECTED_KEY = "img_selected";
	public static final int INTENT_CODE_IMG_SELECTED_DEFAULT = 0;
	public static final int INTENT_CODE_IMG_SELECTED_1 = 1;
	public static final int INTENT_CODE_IMG_SELECTED_2 = 2;
	public static final int INTENT_CODE_IMG_SELECTED_3 = 3;
	public static final int INTENT_CODE_IMG_SELECTED_4 = 4;

	public static final String MODIFY_ACTIVITY_INTENT_INDEX = "index";
	public static final String MODIFY_ACTIVITY_INTENT_CONTENT = "content";
	public static final int MODIFY_INDEX_DEFAULT = 0;
	public static final int MODIFY_INDEX_APPKEY = 1;
	public static final int MODIFY_INDEX_ACCOUNT = 2;
	public static final int MODIFY_INDEX_NICK = 3;

	//权限
	public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =0x10;//通话
	public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0x11;//阅读联系人


//学习
	public static final String EXTRA_KEY = "key";
	public static final String EXTRA_SECRET = "secret";
	public static final String EXTRA_SAMPLE = "sample";
	public static final String EXTRA_SOUND_START = "sound_start";
	public static final String EXTRA_SOUND_END = "sound_end";
	public static final String EXTRA_SOUND_SUCCESS = "sound_success";
	public static final String EXTRA_SOUND_ERROR = "sound_error";
	public static final String EXTRA_SOUND_CANCEL = "sound_cancel";
	public static final String EXTRA_INFILE = "infile";
	public static final String EXTRA_OUTFILE = "outfile";

	public static final String EXTRA_LANGUAGE = "language";
	public static final String EXTRA_NLU = "nlu";
	public static final String EXTRA_VAD = "vad";
	public static final String EXTRA_PROP = "prop";

	public static final String EXTRA_OFFLINE_ASR_BASE_FILE_PATH = "asr-base-file-path";
	public static final String EXTRA_LICENSE_FILE_PATH = "license-file-path";
	public static final String EXTRA_OFFLINE_LM_RES_FILE_PATH = "lm-res-file-path";
	public static final String EXTRA_OFFLINE_SLOT_DATA = "slot-data";
	public static final String EXTRA_OFFLINE_SLOT_NAME = "name";
	public static final String EXTRA_OFFLINE_SLOT_SONG = "song";
	public static final String EXTRA_OFFLINE_SLOT_ARTIST = "artist";
	public static final String EXTRA_OFFLINE_SLOT_APP = "app";
	public static final String EXTRA_OFFLINE_SLOT_USERCOMMAND = "usercommand";

	public static final int SAMPLE_8K = 8000;
	public static final int SAMPLE_16K = 16000;

	public static final String VAD_SEARCH = "search";
	public static final String VAD_INPUT = "input";



}
