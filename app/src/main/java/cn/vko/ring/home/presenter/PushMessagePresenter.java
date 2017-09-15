package cn.vko.ring.home.presenter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;

import cn.vko.ring.circle.activity.CircleHomeActivity;
import cn.vko.ring.circle.activity.GroupHomeActivity;
import cn.vko.ring.circle.activity.TopicDetailH5Activity;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.activity.MainActivity;

import cn.vko.ring.home.listener.IDeviceTokenBindListener;
import cn.vko.ring.home.listener.IUnregisterListener;
import cn.vko.ring.home.model.BindDeviceTokenModel;
import cn.vko.ring.message.activity.MsgSysListActivity;
import cn.vko.ring.message.activity.RecommendMsgDetailActivity;
import cn.vko.ring.mine.model.MessageInfo;
import cn.vko.ring.utils.ACache;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;


/**
 * 消息推送
 * @author JiaRH
 */
public class PushMessagePresenter implements UIDataListener<BindDeviceTokenModel> {
	/**
	 * @fieldName: DEVICE_TOKEN
	 * @fieldType: String
	 * @Description: TODO
	 */
	private static final String DEVICE_TOKEN = "DEVICE_TOKEN";
	private PushAgent mAgent;
	private String deviceToken;
	private Context mContext;
	private ProgressDialog dialog;
//	private UserInfo mUserInfo;
	VolleyUtils<BindDeviceTokenModel> mVolleyUtils;
	private IUnregisterListener iUnregisterListener;
	private int type;
	private IDeviceTokenBindListener deviceTokenBindListener;

	public PushMessagePresenter(Context mContext) {
		super();
		this.mContext = mContext;
	}
	/**
	 * 推送服务
	 * @Title: openPush
	 * @Description: TODO
	 * @return: void
	 */
	public void openPush() {
		// TODO Auto-generated method stub
		mAgent = PushAgent.getInstance(mContext);
		mAgent.onAppStart();
		if (VkoConfigure.getConfigure(mContext).getBoolean(Constants.STATENOTIFACATION, true)) {
			// 开启推送并设置注册的回调处理
			mAgent.enable(mRegisterCallback);
			// addTag();
			// toast("enable:"+mAgent.isEnabled()+"isResgiter:"+mAgent.isRegistered());
			updateBindDeviceTokenStatus();
		}
	}
	public void closePush() {
		if (mAgent == null) {
			mAgent = PushAgent.getInstance(mContext);
		}
		if (mAgent.isEnabled()) {
			mAgent.disable(mUnregisterCallback);
		}
	}
	public void updateBindDeviceTokenStatus() {
		// TODO Auto-generated method stub
		deviceToken = mAgent.getRegistrationId();
		
		if (!TextUtils.isEmpty(deviceToken)) {
			ACache.get(mContext).put(DEVICE_TOKEN, deviceToken);
		}
	
		deviceToken= ACache.get(mContext).getAsString(DEVICE_TOKEN);
//		if (!mUserInfo.isBindDeviceToken()) {
			if (!TextUtils.isEmpty(deviceToken) && VkoContext.getInstance(mContext).isLogin()) {
				bindDeviceToken(deviceToken);
			}
//		}
			System.out.println("deviceToken="+deviceToken);
	}
	/**
	 * 绑定deviceToken
	 * @param deviceToken
	 */
	public void bindDeviceToken(String deviceToken) {
		if (mVolleyUtils==null) {
			mVolleyUtils = new VolleyUtils<BindDeviceTokenModel>(mContext,BindDeviceTokenModel.class);
		}
		Uri.Builder builder = mVolleyUtils.getBuilder(ConstantUrl.VK_BIND_DEVICETOKEN);
		
		builder.appendQueryParameter("token", VkoContext.getInstance(mContext).getToken());
		builder.appendQueryParameter("deviceToken", deviceToken);
		if (type==0) {
			mVolleyUtils.sendGETRequest(false,builder);
		}else{
			builder.appendQueryParameter("type", type+"");
			mVolleyUtils.sendGETRequest(true,builder);
		}
		mVolleyUtils.setUiDataListener(this);
	
	}
	private void toast(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
	}
	public void showLoading() {
		if (dialog == null) {
			dialog = new ProgressDialog(mContext);
			dialog.setMessage("Loading");
		}
		dialog.show();
	}
	private void addTag() {
		if (!mAgent.isRegistered()) {
			// toast("抱歉，还未注册");
			return;
		}
		// showLoading();
		new AddTagTask("0").execute();
	}

	@Override
	public void onDataChanged(BindDeviceTokenModel response) {
		if (response != null && response.getData() !=null) {

			if (response.getData().isUnband()) {
				//解绑token
				if (deviceTokenBindListener!=null) {
					deviceTokenBindListener.unBind(true);
				}
			}else{
				if (deviceTokenBindListener!=null) {
					deviceTokenBindListener.unBind(false);
				}
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}

	class AddTagTask extends AsyncTask<Void, Void, String> {
		String tagString;
		String[] tags;

		public AddTagTask(String tag) {
			// TODO Auto-generated constructor stub
			tagString = tag;
			tags = tagString.split(",");
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				TagManager.Result result = mAgent.getTagManager().reset();
				result = mAgent.getTagManager().add(tags);
				Log.d("addTag", result.toString());
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Fail";
		}
		@Override
		protected void onPostExecute(String result) {
			// toast(result + "addtag");
		}
	}

	public Handler handler = new Handler();
	// 此处是注册的回调处理
	// 参考集成文档的1.7.10
	// http://dev.umeng.com/push/android/integration#1_7_10
	public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
		@Override
		public void onRegistered(String registrationId) {
			// TODO Auto-generated method stub
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateBindDeviceTokenStatus();
				}
			});
		}
	};
	// 此处是注销的回调处理
	// 参考集成文档的1.7.10
	// http://dev.umeng.com/push/android/integration#1_7_10
	public IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {
		@Override
		public void onUnregistered(final String registrationId) {
			// TODO Auto-generated method stub
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (iUnregisterListener != null) {
						iUnregisterListener.unRegister(registrationId);
					}
				}
			});
		}
	};

	public void doMessage(UMessage msg) {
		JSONObject msgJsonObj;
		try {
			msgJsonObj = new JSONObject(msg.extra.get("opt"));
			if (msgJsonObj != null) {
				int type = msgJsonObj.optInt("type");
				EventBus.getDefault().post(Constants.REFRESH_MSG_LIST);

				switch (type) {
					case 2:
						gotoMessageDetail(msgJsonObj);
						break;
					case 3:
						gotoAnotherActivity(msgJsonObj);
						break;
					case 5:
						gotoContentActivity(msgJsonObj);
					case 6:
						gotoSysMsgListActivity(msgJsonObj);
						break;
					default:
						goToMainActivity();
						break;
				}
			
			} else {
				// goToMainActivity();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 系统消息列表界面
	 * @Title: gotoSysMsgListActivity 
	 * @Description: TODO
	 * @param msgJsonObj
	 * @return: void
	 */
	private void gotoSysMsgListActivity(JSONObject msgJsonObj) {
		JSONObject paramJsonObj=msgJsonObj.optJSONObject("param");
			if (paramJsonObj != null) {
				int gotoActivity = paramJsonObj.optInt("gotoo");
				GroupInfo info = new GroupInfo();
				info.setId(paramJsonObj.optString("groupId"));
				info.setGradeName(paramJsonObj.optString("groupName"));
				switch (gotoActivity) {
					case 2:
					EventManager.fire(new Event<GroupInfo>(info,new Event<GroupInfo>().JOIN_EVENT ));
						break;
					case 5:
					case 6:
						EventManager.fire(new Event<GroupInfo>(info,new Event<GroupInfo>().DETEIL_EVENT));
						break;
					default:
						break;
				}
			}
			Intent i = new Intent(mContext,MsgSysListActivity.class);
			i.putExtra("needUpdate", true);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);
	}
	/**
	 * type=5 进入文本详情
	 * @Title: gotoContentActivity
	 * @Description: TODO
	 * @param msgJsonObj
	 * @return: void
	 */
	private void gotoContentActivity(JSONObject msgJsonObj) {
		MessageInfo info = new MessageInfo();
		if (!TextUtils.isEmpty(msgJsonObj.optString("txt"))) {
			info.setContent(msgJsonObj.optString("txt"));
			System.out.println("txt=" + msgJsonObj.optString("txt"));
		}
		Bundle b = new Bundle();
		b.putSerializable("MESSAGE", info);
		b.putInt("TYPE", 1);
		StartActivityUtil.startActivity(mContext, RecommendMsgDetailActivity.class, b, Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	/**
	 * @Title: goToMainActivity
	 * @Description: TODO
	 * @return: void
	 */
	private void goToMainActivity() {
		// TODO Auto-generated method stub
		Intent i = null;
		i = new Intent(mContext, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
	/**
	 * type=2; 进入消息详情 url {"type":2,"url":"http://www.baidu.com"}
	 * @Title: gotoMessageDetail
	 * @Description: TODO
	 * @param msgJsonObj
	 * @return: void
	 */
	private void gotoMessageDetail(JSONObject msgJsonObj) {
		MessageInfo info = new MessageInfo();
		if (!TextUtils.isEmpty(msgJsonObj.optString("url"))) {
			info.setLinkUrl(msgJsonObj.optString("url"));
			System.out.println("url=" + msgJsonObj.optString("url"));
		}
		Bundle b = new Bundle();
		b.putSerializable("MESSAGE", info);
		b.putInt("TYPE", 1);
		StartActivityUtil.startActivity(mContext, RecommendMsgDetailActivity.class, b, Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	private void gotoAnotherActivity(JSONObject msgJsonObj) {
		JSONArray paramJsonObjArray = msgJsonObj.optJSONArray("param");

		JSONObject paramJsonObj;
		try {
			paramJsonObj = paramJsonObjArray.getJSONObject(0);
			Log.e("==========>推送消息",paramJsonObjArray.getJSONObject(1).toString());
			Log.e("==========>推送消息",paramJsonObjArray.getJSONObject(2).toString());
			Log.e("==========>推送消息",paramJsonObjArray.getJSONObject(3).toString());
			Log.e("==========>推送消息",paramJsonObjArray.getJSONObject(0).toString());
			if (paramJsonObj != null) {
				int gotoActivity = paramJsonObj.optInt("goto");
				switch (gotoActivity) {
					case 2:
						goToProblemDetailActivity(paramJsonObj);
						break;
					case 3:
						goToDoTestFragment(paramJsonObj);
						break;
					case 7:
						goToWatchVideo(paramJsonObj);
						break;
					case 8:
						goToDoStudyCircle(paramJsonObj);
						break;
					case 9:
						goToDoException(paramJsonObj);
						break;
					default:
						break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 
	 * @Title: goToDoException 
	 * @Description: 打赏
	 * @param paramJsonObj
	 * @return: void
	 */
	private void goToDoException(JSONObject paramJsonObj) {
		// TODO Auto-generated method stub
		if (paramJsonObj != null) {

			 Bundle bundle = new Bundle();
			 System.out.println("%%%%%%%%%%%%"+paramJsonObj.toString());
			 if (TextUtils.isEmpty(paramJsonObj.optString("url"))) {
				return;
			}
			 
			 bundle.putString("URL", paramJsonObj.optString("url"));
			StartActivityUtil.startActivity(mContext, TopicDetailH5Activity.class, bundle,
			 Intent.FLAG_ACTIVITY_NEW_TASK);
		}
	}
	/** 
	 * @Title: goToDoStudyCircle 
	 * @Description: 学习圈子文章更新
	 * @param paramJsonObj
	 * @return: void
	 */
	private void goToDoStudyCircle(JSONObject paramJsonObj) {
		if (paramJsonObj != null) {

			 Bundle bundle = new Bundle();
//			 bundle.putInt("TYPE", 2);
			StartActivityUtil.startActivity(mContext, CircleHomeActivity.class, bundle,
			 Intent.FLAG_ACTIVITY_NEW_TASK);
		}
	}
	/** 
	 * @Title: goToWatchVideo 
	 * @Description:视频更新
	 * @param paramJsonObj
	 * @return: void
	 */
	private void goToWatchVideo(JSONObject paramJsonObj) {
		if (paramJsonObj != null ) {
			 Bundle bundle = new Bundle();
			 if (TextUtils.isEmpty(paramJsonObj.optString("url"))||TextUtils.isEmpty(paramJsonObj.optString("groupId"))) {
					return;
				}
			 bundle.putString("GROUPID", paramJsonObj.optString("groupId"));
			 bundle.putString("URL", paramJsonObj.optString("url"));
			StartActivityUtil.startActivity(mContext, GroupHomeActivity.class, bundle,
			 Intent.FLAG_ACTIVITY_NEW_TASK);
		}
	}
	/**
	 * goto=3 跳转到测评首页
	 * @Title: goToDoTestFragment
	 * @Description: TODO
	 * @param paramJsonObj
	 * @return: void
	 */
	private void goToDoTestFragment(JSONObject paramJsonObj) {
//		Intent i = new Intent(mContext, MainActivity.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		i.putExtra("subjectInfo", "3");
//		mContext.startActivity(i);
	}
	/**
	 * goto=2 跳转到答疑详情页
	 * @param paramJsonObj
	 * @Title: goToProblemDetailActivity
	 * @Description: TODO
	 * @return: void
	 */
	private void goToProblemDetailActivity(JSONObject paramJsonObj) {
		if (paramJsonObj != null && !TextUtils.isEmpty(paramJsonObj.optString("questionId"))) {
//			 Problem p = new Problem();
//			 p.setId(paramJsonObj.optString("questionId"));
//			 Bundle bundle = new Bundle();
//			 bundle.putSerializable("PROBLEM", p);
//			 bundle.putInt("TYPE", 2);
//			 ViewUtils.startActivity(mContext, ProblemDetailActivity.class, bundle,
//			 Intent.FLAG_ACTIVITY_NEW_TASK);
		}
	}

	public void setDeviceTokenBindListener(IDeviceTokenBindListener deviceTokenBindListener) {
		this.deviceTokenBindListener = deviceTokenBindListener;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
