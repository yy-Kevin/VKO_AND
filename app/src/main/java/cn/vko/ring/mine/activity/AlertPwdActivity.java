package cn.vko.ring.mine.activity;

import android.net.Uri.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import cn.shikh.utils.MD5Utils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.mine.model.BaseResultInfo;


import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class AlertPwdActivity extends BaseActivity implements OnClickListener,
		UIDataListener<BaseResultInfo> {
	@Bind(R.id.tv_title)
	public TextView tvLogin;
	private ImageView ivLeft;
	@Bind(R.id.but_bind)
	public Button butBind;
	private EditText edPwdOld, edPwdNew, edPwdNewAgain;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_alert_pwd;
	}

	@Override
	public void initView() {
		ivLeft = (ImageView) findViewById(R.id.iv_back);
		edPwdOld = (EditText) findViewById(R.id.ed_pwd_old);
		edPwdNew = (EditText) findViewById(R.id.ed_pwd_new);
		edPwdNewAgain = (EditText) findViewById(R.id.ed_pwd_new_again);

		ivLeft.setOnClickListener(this);
		butBind.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvLogin.setText("修改密码");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 判断登录
		if (VkoContext.getInstance(this).doLoginCheckToSkip(this)) {
			return;
		}
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.but_bind:
			// if (TextUtils.isEmpty(edPwdOld.getText().toString().trim())) {
			// Toast.makeText(this, "请输入原始密码", 0).show();
			// return;
			// }
			if (TextUtils.isEmpty(edPwdNew.getText().toString().trim())) {
				Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(edPwdNewAgain.getText().toString().trim())) {
				Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!(edPwdNew.getText().toString().trim().equals(edPwdNewAgain
					.getText().toString().trim()))) {
				Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			// if (edPwdOld.getText().toString().trim()
			// .equals(edPwdNew.getText().toString().trim())) {
			// Toast.makeText(this, "新密码不可与旧密码相同", Toast.LENGTH_SHORT).show();
			// return;
			// }
			getAlterPwdTask();
			break;
		}

	}

	private void getAlterPwdTask() {
		String url = ConstantUrl.VKOIP + "/user/chgpwd";
		VolleyUtils<BaseResultInfo> volleyUtils = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
//		Builder builder = volleyUtils.getBuilder(url);
		// String oldPass = MD5Utils.md5(edPwdOld.getText().toString().trim())
		// .toLowerCase();
		Map<String,String> params = new HashMap<String,String>();
		String newPass = MD5Utils.md5(edPwdNew.getText().toString().trim()).toLowerCase();
		// builder.appendQueryParameter("oldPwd", oldPass);
		params.put("pwd", newPass);
		params.put("token", VkoContext.getInstance(this).getToken());
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(true,url,params);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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
		hideSoftInput(this,edPwdNew , true);
	}

	@Override
	public void onDataChanged(BaseResultInfo t) {
		try {
			if (t.getCode() == 0 && t != null) {
				if (t.isData()) {
					// 成功
					Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
					this.finish();
				}
			}
		} catch (NullPointerException e) {
			Toast.makeText(this, t.getMsg(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
