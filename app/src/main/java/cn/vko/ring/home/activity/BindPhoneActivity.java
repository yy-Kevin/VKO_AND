package cn.vko.ring.home.activity;

import android.content.Intent;
import android.net.Uri.Builder;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.shikh.utils.RegexUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.BindResultInfo;
import cn.vko.ring.home.model.UserInfo;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class BindPhoneActivity extends BaseActivity implements OnClickListener {

	private Button butBind;
	private ImageView ivLeft;
	private TextView tvTitle;
	private TextView tvSend;
	private EditText edPhone; // 手机号
	private EditText edVertical; // 验证码
	private MTimeCount mTimeCount;
	private String Ident;// LOGIN BIND UPDATE
	private UserInfo user;
	private String phone;
	private boolean isBindPhone;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_bindphone;
	}

	@Override
	public void initView() {
		mTimeCount = new MTimeCount(60000, 1000);
		ivLeft = (ImageView) this.findViewById(R.id.iv_back);
		ivLeft.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		butBind = (Button) this.findViewById(R.id.but_bind);
		tvSend = (TextView) this.findViewById(R.id.tv_send);
		edPhone = (EditText) this.findViewById(R.id.ed_phone);
		edVertical = (EditText) this.findViewById(R.id.ed_vertical);
		ivLeft.setOnClickListener(this);
		butBind.setOnClickListener(this);
		tvSend.setOnClickListener(this);
	}

	@Override
	public void initData() {
		user = VkoContext.getInstance(this).getUser();
		Ident = getIntent().getExtras().getString("BIND");
		if (TextUtils.isEmpty(VkoContext.getInstance(this).getUser()
				.getMobile())) {
			tvTitle.setText("绑定手机号");
			isBindPhone=true;
		} else {
			tvTitle.setText("更换手机号");
			isBindPhone=false;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.but_bind:
			if (TextUtils.isEmpty(edVertical.getText().toString().trim())) {
				Toast.makeText(getApplicationContext(), "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 绑定
			getBindMobileTask();
			break;
		case R.id.tv_send:
			if (TextUtils.isEmpty(edPhone.getText().toString().trim())) {
				Toast.makeText(getApplicationContext(), "请输入手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!RegexUtils.checkMobile(edPhone.getText().toString().trim())) {
				Toast.makeText(getApplicationContext(), "号码输入错误",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 发送验证码
			tvSend.setClickable(false);
			mTimeCount.start();
			getBindSendTask();
			break;
		}
	}

	// 绑定
	private void getBindMobileTask() {
		String url = ConstantUrl.VKOIP + "/user/bind";
		VolleyUtils<BindResultInfo> volleyUtils = new VolleyUtils<BindResultInfo>(this,BindResultInfo.class);
		Map<String,String> params = new HashMap<String,String>();
//		volleyUtils.setFinish(true);
//		Builder builder = volleyUtils.getBuilder(url);
		params.put("code", edVertical.getText().toString().trim());
		params.put("token", VkoContext.getInstance(this).getUser().getToken());
		volleyUtils.sendPostRequest(true,url,params);
		volleyUtils.setUiDataListener(new UIDataListener<BindResultInfo>() {
			@Override
			public void onDataChanged(BindResultInfo t) {
					if (t != null) {
						if (t.isData()) {
							// 成功
							if (Ident.equals("UPDATE")) {
								Intent intent = new Intent();
								intent.putExtra("newBindPhone", edPhone.getText().toString().trim());
								setResult(9, intent);
							}
							StartActivityUtil.startActivity(BindPhoneActivity.this, MainActivity.class);
							finish();
						} else {
							// 失败
//							Toast.makeText(getApplicationContext(), "0.0" + t.getMsg(), Toast.LENGTH_SHORT).show();
						}
					}


			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	// 发送验证码
	private void getBindSendTask() {
		// tvSend.setBackgroundResource(R.drawable.shape_qianblack_corners);
		String url = "";
		if (isBindPhone) {
			
		 url = ConstantUrl.VKOIP + "/user/sendBcode";
		}else{
			url = ConstantUrl.VKOIP + "/user/sendBcodeMod";
			
		}
		VolleyUtils<BindResultInfo> volleyUtils = new VolleyUtils<BindResultInfo>(this,BindResultInfo.class);
		Builder builder = volleyUtils.getBuilder(url);
		phone = edPhone.getText().toString().trim();
		builder.appendQueryParameter("mobile", phone);
		builder.appendQueryParameter("token", VkoContext.getInstance(this).getUser().getToken());
		volleyUtils.sendGETRequest(true,builder);
		volleyUtils.setUiDataListener(new UIDataListener<BindResultInfo>() {
			@Override
			public void onDataChanged(BindResultInfo data) {

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
			tvSend.setText((millisUntilFinished / 1000) + "秒后重发");
		}

		@Override
		public void onFinish() {
			resetVerify();
		}
	}

	private void resetVerify() {
		mTimeCount.cancel();
		tvSend.setClickable(true);
		tvSend.setFocusableInTouchMode(true);
		// tvSend.setBackgroundResource(R.drawable.login_rectangle);
		tvSend.setText("发送验证码");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated meth od stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
		hideSoftInput(this,edPhone, true);
	}
	// 如果按返回键时，没有完善信息或者保存的话，不能返回
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Ident.equals("LOGIN")) {
				if (!TextUtils.isEmpty(user.getSchool())
						&& Integer.parseInt(user.getGradeId()) >= 0) {
					StartActivityUtil.startActivity(this, MainActivity.class);
				} else {
					StartActivityUtil.startActivity(this, RegisteTwoActivity.class);
				}
				overridePendingTransition(0, R.anim.bottom_out);
				finish();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
