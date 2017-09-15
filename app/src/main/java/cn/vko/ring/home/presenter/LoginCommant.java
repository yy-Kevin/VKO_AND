package cn.vko.ring.home.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.home.activity.BindPhoneActivity;
import cn.vko.ring.home.activity.MainActivity;
import cn.vko.ring.home.activity.RegisteTwoActivity;
import cn.vko.ring.home.listener.ThirdLoginCallBack;
import cn.vko.ring.home.model.BaseUserInfo;

public class LoginCommant implements UMAuthListener{

	private Activity mContaxt;
	private String access_token;
	private String uid;
	private cn.vko.ring.home.model.UserInfo user;
	private String headUrl;
	private String QQname;
	private int flag;

	private UMShareAPI mShareAPI = null;
	private ThirdLoginCallBack callBack;
	public LoginCommant(Activity mContaxt,ThirdLoginCallBack callBack,UMShareAPI mShareAPI) {
		super();
		this.mContaxt = mContaxt;
		this.callBack = callBack;
		/**begin invoke umeng api**/
		this.mShareAPI = mShareAPI;
	}

	/**
	 * QQ登陆
	 */
	public void loginByQQ(int falg) {
		this.flag = falg;

		if(!mShareAPI.isInstall(mContaxt, SHARE_MEDIA.QQ)){
			Toast.makeText(mContaxt,"未安装QQ",Toast.LENGTH_SHORT).show();
			return;
		}
		mShareAPI.doOauthVerify(mContaxt, SHARE_MEDIA.QQ,this);
		VkoConfigure.getConfigure(mContaxt).put("THCLogin", "loginByQQ");
	}

	/**
	 * 微信登陆
	 */
	public void loginByWX(int flag) {
		this.flag = flag;
		if(!mShareAPI.isInstall(mContaxt, SHARE_MEDIA.WEIXIN)){
//			Toast.makeText(mContaxt,"请先安装微信",Toast.LENGTH_SHORT).show();
			return;
		}
		mShareAPI.doOauthVerify(mContaxt, SHARE_MEDIA.WEIXIN,this);
		VkoConfigure.getConfigure(mContaxt).put("THCLogin", "loginByWX");
	}

	private void getThdTask(Object arg0, int index, final ThirdLoginCallBack callBack) {
		String url = ConstantUrl.VKOIP + "/user/thdLogin";
		VolleyUtils<BaseUserInfo> volleyUtils = new VolleyUtils<BaseUserInfo>(mContaxt,BaseUserInfo.class);
		Map<String,String> params = new HashMap<String,String>();
		params.put("uid", uid);
		params.put("token", access_token);
		params.put("json", arg0.toString().trim());
		params.put("type", 1 + "");
		System.out.println("uid= " + uid + " token= " +access_token +" json="+arg0.toString().trim());

//		Log.e("=====登陆参数up","type= " + index + "");
		volleyUtils.sendPostRequest(true,url,params);
		volleyUtils.setUiDataListener(new UIDataListener<BaseUserInfo>() {
			@Override
			public void onDataChanged(BaseUserInfo t) {
				if (t != null) {
					// Toast.makeText(mContaxt, t.toString(),
					// Toast.LENGTH_SHORT).show();
					if (t.getCode() == 0 && t.getData() != null) {
						// 保存用户信息
						user = t.getData();
						user.setBface(headUrl);
						user.setSface(headUrl);
						user.setName(QQname);
						if (user.isSignInState()) {
							user.setSignInTime(t.getStime());
						}
						VkoContext.getInstance(mContaxt).setUser(user);
						callBack.login(true);

						String mobile=user.getMobile();
						Log.e("============mobile","=============="+mobile);
						if (mobile==null||mobile.isEmpty()){
							if (flag == 0||flag==1) {
//							BindPhoneDialog();
							goBindPhone();
							} else {
								EventBus.getDefault().post(Constants.LOGIN_REFRESH);
							}
//							goBindPhone();
						}else {
							StartActivityUtil.startActivity(mContaxt, MainActivity.class);
						}

//						EaseHelper.getInstance().logout(true, null);
//						Log.e("============走到这里结束了？","==============");
//						mContaxt.finish();
						mContaxt.overridePendingTransition(0, R.anim.bottom_out);

					} else {
						 Toast.makeText(mContaxt, "t.getData()为空", Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	public void goBindPhone(){
//		if (!TextUtils.isEmpty(user.getSchoolId())
//				&& Integer.parseInt(user.getGradeId()) >= 0) {
//			StartActivityUtil.startActivity(mContaxt, MainActivity.class);
//		} else {
//			StartActivityUtil.startActivity(mContaxt, RegisteTwoActivity.class);
//		}
		Bundle bundle = new Bundle();
		bundle.putString("BIND", "LOGIN");
		StartActivityUtil.startActivity(mContaxt, BindPhoneActivity.class,
				bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mContaxt.overridePendingTransition(0, R.anim.bottom_in);
		mContaxt.finish();
	}



	private SweetAlertDialog dialog;

	protected void BindPhoneDialog() {
		// TODO Auto-generated method stub
		dialog = new SweetAlertDialog(mContaxt);
		dialog.setCancelable(false);
		dialog.setTitleText("温馨提示");
		dialog.setContentText("未绑定手机号只能免费使用二天,是否绑定手机号");
		dialog.setConfirmText("立即绑定");
		dialog.setCancelText("暂不绑定");
		dialog.setCancelClickListener(new OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(user.getSchoolId())
						&& Integer.parseInt(user.getGradeId()) >= 0) {
					StartActivityUtil.startActivity(mContaxt, MainActivity.class);
				} else {
					StartActivityUtil.startActivity(mContaxt, RegisteTwoActivity.class);
				}
				mContaxt.finish();
				dialog.dismiss();
			}
		});
		dialog.setConfirmClickListener(new OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("BIND", "LOGIN");
				StartActivityUtil.startActivity(mContaxt, BindPhoneActivity.class,
						bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				mContaxt.overridePendingTransition(0, R.anim.bottom_in);
				mContaxt.finish();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * sina微博登陆
	 */
	public void loginBySina() {
		// VkoConfigure.getConfigure(mContaxt).put("THCLogin", "loginBySina");
		Toast.makeText(mContaxt, "正在开发中...", Toast.LENGTH_LONG).show();
	}



	@Override
	public void onComplete(SHARE_MEDIA share_media, int status, Map<String, String> info) {
		Toast.makeText(mContaxt, "授权完成", Toast.LENGTH_SHORT).show();
//		Log.e("=======授权完成","==================");
		if (status == 0 && info != null) {

			StringBuilder sb = new StringBuilder();
			Set<String> keys = info.keySet();
			for (String key : keys) {
				sb.append(key + "=" + info.get(key).toString() + "\r\n");
			}
			// Toast.makeText(mContaxt,
			// sb.toString(),0).show();
			/*Iterator<Map.Entry<String, String>> it = info.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				Toast.makeText(mContaxt,"key= " + entry.getKey() + " and value= " + entry.getValue(),Toast.LENGTH_LONG).show();
			}*/

			access_token = info.get("access_token");
			if(share_media == SHARE_MEDIA.WEIXIN){//微信5.0 暂无uid 先用openid 代替
				uid = info.get("openid");
			}else{
				uid = info.get("uid");
			}
			Log.i("TestData", access_token +" uid = "+ uid);
			headUrl = info.get("headimgurl");
			QQname =  info.get("nickname");
			getThdTask(sb.toString(), flag,callBack);
		} else {
			Log.d("TestData", "发生错误：" + status);
		}
	}

	@Override
	public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
		Toast.makeText(mContaxt, "授权错误", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onCancel(SHARE_MEDIA share_media, int i) {
		Toast.makeText(mContaxt, "授权取消", Toast.LENGTH_SHORT)
				.show();
	}
}
