package cn.vko.ring.home.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.MD5Utils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.RegResultInfo;

import java.util.HashMap;
import java.util.Map;

public class SetNewPwdActivity extends BaseActivity {

	@Bind(R.id.ed_newpwd)
	public EditText edNewPwd;
	@Bind(R.id.hide_show)
	public ImageView ivEye;
	private String mobile;
	private boolean isShow;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_set_newpwd2;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if (getIntent() != null) {
			mobile = getIntent().getStringExtra("mobile");
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

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
			edNewPwd.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		} else {
			isShow = true;
			ivEye.setImageResource(R.drawable.password_show);
			// 显示密码
			edNewPwd.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
		}
	}

	@OnClick(R.id.success)
	public void ivSuccess() {
		String newPwd = edNewPwd.getText().toString().trim();
		if (TextUtils.isEmpty(newPwd)) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
			return;
		}
		getResetPwdTask();
	}

	private void getResetPwdTask() {
		String url = ConstantUrl.VKOIP + "/user/rstpwd";
		VolleyUtils<RegResultInfo> volleyUtils = new VolleyUtils<RegResultInfo>(this,RegResultInfo.class);
		Map<String,String> params = new HashMap<String,String>();
		params.put("mobile", mobile);
		params.put("pwd", MD5Utils.md5(edNewPwd.getText().toString().trim().toLowerCase()));
		volleyUtils.sendPostRequest(true,url,params);
		volleyUtils.setUiDataListener(new UIDataListener<RegResultInfo>() {
			@Override
			public void onDataChanged(RegResultInfo t) {
				if (t != null) {
					if (t.getCode() == 0 && t.isData()) {
						// 重置成功
						startActivity(new Intent(
								SetNewPwdActivity.this,
								LoginActivity.class));
						finish();
						Toast.makeText(SetNewPwdActivity.this,
								"密码修改成功", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(SetNewPwdActivity.this,
								t.getMsg(), Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});

	}

}
