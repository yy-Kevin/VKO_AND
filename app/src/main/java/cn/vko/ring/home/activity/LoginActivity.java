package cn.vko.ring.home.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.MD5Utils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.listener.ThirdLoginCallBack;
import cn.vko.ring.home.model.BaseUserInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.home.presenter.LoginCommant;
import cn.vko.ring.home.presenter.LoginImPresenter;

public class LoginActivity extends BaseActivity {
	@Bind(R.id.iv_back)
	public ImageView ivLeft;
	@Bind(R.id.hide_show)
	public ImageView ivEye;
	@Bind(R.id.ed_phone)
	public EditText edPhone; // 手机号
	@Bind(R.id.ed_password)
	public EditText edPassword; // 密码
	private String md5PassWord;
	private boolean isShow;
	private String name;
//	private UMSocialService mController;
	private LoginCommant commant;
//	private UmengShare share;
	private int flag;
	private AbortableFuture<LoginInfo> loginRequest;
	private VolleyUtils<BaseUserInfo> volleyUtils;
	private static final String TAG = "LoginActivity";
	private UMShareAPI mShareAPI = null;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_login1;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		tintManager.setStatusBarTintResource(R.color.transparent);
		edPhone.setText(VkoConfigure.getConfigure(this).getString(Constants.USERNAME));
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mShareAPI = UMShareAPI.get(this);
		flag = getIntent().getIntExtra("exit", 1);
		if (flag == 0) {
			ivLeft.setVisibility(View.INVISIBLE);
		}
	}

	// 点击登录
	@OnClick(R.id.but_login)
	public void butLoginClick() {
		if (TextUtils.isEmpty(edPassword.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		md5PassWord = MD5Utils.md5(edPassword.getText().toString().trim()).toLowerCase();
		name = edPhone.getText().toString().trim();
		loginTask( name, md5PassWord);
		VkoConfigure.getConfigure(this).put("isThirdLoginForAlertPwd", false);
	}

	private void loginTask(final String name, final String md5PassWord) {
		String url = ConstantUrl.VKOIP + "/user/login";
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", name);
		params.put("pwd", md5PassWord);

		if(volleyUtils == null){
			volleyUtils = new VolleyUtils<BaseUserInfo>(this,BaseUserInfo.class);
		}
		volleyUtils.setUiDataListener(new UIDataListener<BaseUserInfo>() {
			@Override
			public void onDataChanged(BaseUserInfo t) {
				if (t != null && t.getData() != null) {
					// 保存用户的用户名和密码
					VkoConfigure.getConfigure(LoginActivity.this).put(Constants.USERNAME, name);
					VkoConfigure.getConfigure(LoginActivity.this).put(Constants.PWD, md5PassWord);
					UserInfo user = t.getData();
					if(user.isSignInState()){
						user.setSignInTime(t.getStime());
					}
					VkoContext.getInstance(LoginActivity.this).setUser(user);
					LoginImPresenter.getInstance(LoginActivity.this).logInWYIm();
					Log.e("======>flag",flag+"");
					if(flag == 0|| flag==1) {
						StartActivityUtil.startActivity(LoginActivity.this,MainActivity.class);
					}else {
						EventBus.getDefault().post(Constants.LOGIN_REFRESH);
					}
//					EaseHelper.getInstance().logout(true, null);
					finish();
					overridePendingTransition(0, R.anim.bottom_out);
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
		volleyUtils.sendPostRequest(true,url,params);
	}

	@OnClick(R.id.iv_back)
	public void ivImgFinish() {
		this.finish();
		overridePendingTransition(0, R.anim.bottom_out);
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		this.finish();
		overridePendingTransition(0, R.anim.bottom_out);
	}

	@OnClick(R.id.hide_show)
	public void hideOrShow() {
		if (isShow) {
			isShow = false;
			ivEye.setImageResource(R.drawable.password_hide);
			// 隐藏密码
			edPassword.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		} else {
			isShow = true;
			ivEye.setImageResource(R.drawable.password_show);
			// 显示密码
			edPassword.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
		}
	}

	// 注册
	@OnClick(R.id.regist_user)
	public void RegistVkoUser() {
		StartActivityUtil.startActivity(this, RegisterFirstActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// finish();
	}

	// 快速登录
	@OnClick(R.id.login_by_phone_msg)
	public void loginByPhoneMsg() {
		// Toast.makeText(LoginActivity.this, "快速登录暂未开通", Toast.LENGTH_SHORT)
		// .show();
		Intent i = new Intent(LoginActivity.this, PhoneLoginActivity.class);
		i.putExtra("exit", 0);
		startActivity(i);
	}

	// QQ登录
	@OnClick(R.id.login_by_qq_user)
	public void loginByQQUser() {
		if (commant == null) {
			commant = new LoginCommant(this,callBack,mShareAPI);
		}
		commant.loginByQQ(flag);
		VkoConfigure.getConfigure(this).put("isThirdLoginForAlertPwd", true);
	}

	// 微信登录
	@OnClick(R.id.login_by_weixin_user)
	public void loginByWeixinUser() {
		if (commant == null) {
			commant = new LoginCommant(this,callBack,mShareAPI);
		}
		commant.loginByWX(flag);
		VkoConfigure.getConfigure(this).put("isThirdLoginForAlertPwd", true);
	}

	// 忘记密码
	@OnClick(R.id.tv_forget)
	public void forgetClick() {
		StartActivityUtil.startActivity(this, ForgetPwdActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			if (msg.equals(Constants.LOGIN_REFRESH)) {
				finish();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}



	ThirdLoginCallBack callBack = new ThirdLoginCallBack() {
		@Override
		public void login(boolean isOk) {
			if (isOk ){
				LoginImPresenter.getInstance(LoginActivity.this).logInWYIm();
			}

		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}
}
