package cn.vko.ring.home.activity;

import android.content.Intent;
import android.net.Uri.Builder;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.MD5Utils;
import cn.shikh.utils.RegexUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.BaseUserInfo;
import cn.vko.ring.home.model.RegResultInfo;

import java.util.HashMap;
import java.util.Map;

public class RegisterFirstActivity extends BaseActivity {

	@Bind(R.id.ed_phone)
	public EditText edPhone; // 手机号
	@Bind(R.id.ed_verification)
	public EditText edVerification; // 验证码
	@Bind(R.id.ed_password)
	public EditText edPassword; // 密码
	@Bind(R.id.tv_send)
	public TextView mTvSend;
	@Bind(R.id.protocol)
	public TextView mProtocol;
	@Bind(R.id.hide_show)
	public ImageView ivEye;
	private VolleyUtils<RegResultInfo> volleyUtils;
	private MTimeCount mTimeCount;
	boolean isPhoneInfo;
	private boolean isShow;
	private String sProtocol;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_regist_first;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tintManager.setStatusBarTintResource(R.color.transparent);
		mTimeCount = new MTimeCount(60000, 1000);
		sProtocol = (String) getResources().getText(R.string.registe_argree);
		SpannableStringBuilder style = new SpannableStringBuilder(sProtocol);
		TextViewURLSpan myURLSpan = new TextViewURLSpan();
		style.setSpan(myURLSpan, 13, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mProtocol.setText(style);
		mProtocol.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private class TextViewURLSpan extends ClickableSpan {
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.blue));
			ds.setUnderlineText(true); // 是否要下划线
		}

		@Override
		public void onClick(View widget) {// 点击事件
			Intent intent = new Intent(getApplicationContext(), MyServiceProtocolActivity.class);
			startActivity(intent);
			mProtocol.setHighlightColor(getResources().getColor(
					R.color.transparent));
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hideSoftInput(this,edPhone,true);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mTimeCount != null) {
			mTimeCount.cancel();
			mTimeCount = null;
		}
	}

	// @OnClick(R.id.protocol)
	// public void onProtocolClick() {
	// ViewUtils.startActivity(this, MyServiceProtocolActivity.class);
	// }

	@OnClick(R.id.iv_back)
	public void ivImgFinish() {
		this.finish();
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

	/** 获取验证码 */
	@OnClick(R.id.tv_send)
	public void getVerdiaCodeClick() {
		getVerdiaCode(isPhoneInfo);
	}

	// 判断该用户是否已经注册
	private void getVerdiaCode(final boolean isPhone) {
		String phone = edPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.input_mobile),  Toast.LENGTH_LONG).show();
			return;
		}
		if (!RegexUtils.checkMobile(phone)) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.mpbile_lost), Toast.LENGTH_LONG).show();
			return;
		}

		volleyUtils = new VolleyUtils<RegResultInfo>(this,RegResultInfo.class);
		String url = ConstantUrl.VKOIP + "/user/regCheck/" + phone;
		volleyUtils.sendGETRequest(true,volleyUtils.getBuilder(url));
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo data) {
				if (data != null && data.isData()) {
					mTimeCount.start();
					mTvSend.setClickable(false);
					if (isPhone == true) {
						mTvSend.setTextColor(getResources().getColor(
								R.color.last_more_gray));
					}
					getRegistNext(isPhone);
				} else {
					Toast.makeText(RegisterFirstActivity.this,
							"该用户已注册",  Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});

	}

	// 发送验证码
	protected void getRegistNext(boolean isPhone) {
		if (isPhone) {
			Toast.makeText(this, "稍后来电，请接听电话", Toast.LENGTH_LONG).show();
		}
		String phone = edPhone.getText().toString().trim();
		String url = ConstantUrl.VKOIP + "/user/sendVcode/" + phone;
		volleyUtils.sendGETRequest(true,volleyUtils.getBuilder(url.concat("?voice=").concat(
				String.valueOf(isPhone))));
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo data) {
				if (data != null) {
					if (!data.isData()) {
						Toast.makeText(getApplicationContext(),
								"发送失败，请重试",  Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	// 点击注册
	@OnClick(R.id.verification)
	public void verificationClick() {
		String phone = edPhone.getText().toString().trim();
		String avaiNum = edVerification.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(avaiNum)) {
			Toast.makeText(getApplicationContext(), "请输入验证码",  Toast.LENGTH_LONG).show();
			return;
		}
		VolleyUtils<RegResultInfo> volleyUtils = new VolleyUtils<RegResultInfo>(this,RegResultInfo.class);
		String url = ConstantUrl.VKOIP + "/user/checkVcode/" + phone + "/"
				+ avaiNum;
		volleyUtils.sendGETRequest(true,volleyUtils.getBuilder(url));
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo t) {
				regNext(t);
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	protected void regNext(RegResultInfo t) {

		if (t != null) {
			if (!t.isData()) {
				Toast.makeText(this,
						getResources().getString(R.string.reg_error), Toast.LENGTH_SHORT).show();
			} else {
				String phone = edPhone.getText().toString().trim();
				String verification = edVerification.getText().toString()
						.trim();
				String password = edPassword.getText().toString().trim();
				registNextClick(phone, verification, password);
			}
		}

	}

	public void registNextClick(final String phone, String verification,
			final String password) {
		VolleyUtils<BaseUserInfo> volleyUtils = new VolleyUtils<BaseUserInfo>(this,BaseUserInfo.class);
		String url = ConstantUrl.VKOIP + "/user/reg";
		Map<String,String> params = new HashMap<String,String>();
		Builder builder = volleyUtils.getBuilder(url);
		if (!phone.isEmpty()) {
			params.put("name", phone);
		}
		if (!password.isEmpty()) {
			String md5PassWord = MD5Utils.md5(password).toLowerCase();
			params.put("pwd", md5PassWord);
		}
		volleyUtils.sendPostRequest(true,url,params);
		volleyUtils.setUiDataListener(new UIDataListener<BaseUserInfo>() {
			@Override
			public void onDataChanged(BaseUserInfo t) {
				if (t != null) {
						// 保存用户的用户名和密码
						VkoConfigure.getConfigure(RegisterFirstActivity.this).put(Constants.USERNAME, phone);
						VkoConfigure.getConfigure(RegisterFirstActivity.this).put(Constants.PWD, password);
						VkoContext.getInstance(RegisterFirstActivity.this).setUser(t.getData());
						// 跳转注册成功界面
						Intent intent = new Intent(RegisterFirstActivity.this, RegisteFinishActivity.class);
						intent.putExtra("msg", t.getData().getMsg());
						startActivity(intent);
						finish();
					}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	// 发送验证码维护的时间
	private class MTimeCount extends CountDownTimer {

		public MTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mTvSend.setText((millisUntilFinished / 1000) + "秒后重发");
			mTvSend.setClickable(false);
		}

		@Override
		public void onFinish() {
			resetVerify();
		}
	}

	private void resetVerify() {
		mTvSend.setText("语音获取验证码");
		mTvSend.setClickable(true);
		if (!isPhoneInfo) {
			isPhoneInfo = true;
		}
	}

}
