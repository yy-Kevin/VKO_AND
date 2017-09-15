package cn.vko.ring.home.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.RegexUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.RegResultInfo;

public class ForgetPwdActivity extends BaseActivity {

	@Bind(R.id.ed_phone)
	public EditText edPhone; // 手机号
	@Bind(R.id.ed_verification)
	public EditText edVerification; // 验证码
	@Bind(R.id.tv_send)
	public TextView mTvSend;
	@Bind(R.id.protocol)
	public TextView mProtocol;
	private MTimeCount mTimeCount;
	boolean isPhoneInfo;
	private String sProtocol;
	private VolleyUtils<RegResultInfo> volleyUtils;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_forget_password;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tintManager.setStatusBarTintResource(R.color.transparent);
		mTimeCount = new MTimeCount(60000, 1000);
		sProtocol = (String) getResources().getText(
				R.string.registe_argree_forget);
		SpannableStringBuilder style = new SpannableStringBuilder(sProtocol);
		TextViewURLSpan myURLSpan = new TextViewURLSpan();
		style.setSpan(myURLSpan, 14, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mProtocol.setText(style);
		mProtocol.setMovementMethod(LinkMovementMethod.getInstance());
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

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	// @OnClick(R.id.protocol)
	// public void onProtocolClick() {
	// ViewUtils.startActivity(this, MyServiceProtocolActivity.class);
	// }

	@OnClick(R.id.iv_back)
	public void ivImgFinish() {
		this.finish();
	}

	/** 获取验证码 */
	@OnClick(R.id.tv_send)
	public void getVerdiaCodeClick() {
		if (TextUtils.isEmpty(edPhone.getText().toString().trim())) {
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_LONG).show();
			return;
		}

		else if (!(RegexUtils.checkPhone(edPhone.getText().toString().trim()))
				|| edPhone.getText().toString().length() != 11) {
			Toast.makeText(this, "手机号码不存在", Toast.LENGTH_LONG).show();
			return;
		} else {
			getVerdiaCode();
		}
	}

	// 判断该用户是否已经注册
	private void getVerdiaCode() {
		String phone = edPhone.getText().toString().trim();
		volleyUtils = new VolleyUtils<RegResultInfo>(this,RegResultInfo.class);
		String url = ConstantUrl.VKOIP + "/user/regCheck/" + phone;
		volleyUtils.sendGETRequest(true,volleyUtils.getBuilder(url));
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo data) {
				if (data != null && data.isData()) {
					Toast.makeText(ForgetPwdActivity.this, "该用户没有注册", Toast.LENGTH_LONG)
							.show();
				} else {
					mTimeCount.start();
					mTvSend.setClickable(false);
					getForgetPwdSend();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});

	}

	private void getForgetPwdSend() {
		// mTvSend.setClickable(false);
		// mTimeCount.start();
		mTvSend.setTextColor(getResources().getColor(R.color.last_more_gray));
		String phone = edPhone.getText().toString().trim();
		String url = ConstantUrl.VKOIP + "/user/sendRcode/" + phone;
		VolleyUtils<RegResultInfo> volleyUtils = new VolleyUtils<RegResultInfo>(this,RegResultInfo.class);
		volleyUtils.sendGETRequest(true,volleyUtils.getBuilder(url));
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo data) {

			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	// 点击下一步
	@OnClick(R.id.verification)
	public void verificationClick() {
		if (TextUtils.isEmpty(edVerification.getText().toString().trim())) {
			Toast.makeText(this, "输入验证码", Toast.LENGTH_LONG).show();
			return;
		}
		if (edVerification.getText().toString().trim().length() != 5) {
			Toast.makeText(this, "验证码错误",Toast.LENGTH_LONG).show();
		}
		getForgetPwdCheckTask();
	}

	private void getForgetPwdCheckTask() {
		String phone = edPhone.getText().toString().trim();
		String check = edVerification.getText().toString().trim();
		String url = ConstantUrl.VKOIP + "/user/checkRcode/" + phone + "/" + check;
		VolleyUtils<RegResultInfo> volleyUtils = new VolleyUtils<RegResultInfo>(this,RegResultInfo.class);
		volleyUtils.sendGETRequest(true,volleyUtils.getBuilder(url));
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo t) {
				if (t != null) {
					if (t.getCode() == 0 && t.isData()) {
						// 成功
						Intent intent = new Intent(
								ForgetPwdActivity.this, SetNewPwdActivity.class);
						intent.putExtra("mobile", edPhone.getText()
								.toString().trim());
						startActivity(intent);
						finish();
					}
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
			if (!isPhoneInfo) {
				mTvSend.setText((millisUntilFinished / 1000) + "秒后重发");
			}
		}

		@Override
		public void onFinish() {
			resetVerify();
		}
	}

	private void resetVerify() {
		mTvSend.setText("获取验证码");
		mTvSend.setClickable(true);
		mTvSend.setTextColor(getResources().getColor(R.color.verdiacode));
	}

	private class TextViewURLSpan extends ClickableSpan {
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.white));
			ds.setUnderlineText(true); // 是否要下划线
		}

		@Override
		public void onClick(View widget) {// 点击事件
			Intent intent = new Intent(getApplicationContext(),
					MyServiceProtocolActivity.class);
			startActivity(intent);
			mProtocol.setHighlightColor(getResources().getColor(
					R.color.transparent));
		}
	}

}
