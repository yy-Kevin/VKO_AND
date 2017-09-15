package cn.vko.ring.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri.Builder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.RevisePersonMsgInfo;

public class NickNameActivity extends BaseActivity implements
		UIDataListener<RevisePersonMsgInfo> {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvRight;
	@Bind(R.id.ed_name)
	public EditText edName;
	private String name;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_nick_name;
	}

	@Override
	public void initView() {
		if (getIntent() != null) {
			name = getIntent().getStringExtra("name");
		}
		edName.setText(name);
		tvTitle.setText("姓名");
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("保存");
	}

	@Override
	public void initData() {

	}

	@OnClick(R.id.iv_back)
	public void imgFinishClick() {
		this.finish();
	}

	/** 保存信息 */
	@OnClick(R.id.tv_right)
	public void saveClick() {
		if (TextUtils.isEmpty(edName.getText().toString().trim())) {
			Toast.makeText(this, "请输入您的姓名", Toast.LENGTH_LONG).show();
			return;
		} else if (edName.getText().toString().length() > 8) {
			Toast.makeText(this, "修改后姓名长度不超过8个字符", Toast.LENGTH_LONG).show();
			return;
		}
		getNickNameTask();
	}
	
	private void getNickNameTask() {
		UserInfo userInfo = VkoContext.getInstance(this).getUser();
		String url = ConstantUrl.VKOIP + "/user/mod";
		VolleyUtils<RevisePersonMsgInfo> volleyUtils = new VolleyUtils<RevisePersonMsgInfo>(this,RevisePersonMsgInfo.class);
		name = edName.getText().toString().trim();
		Map<String,String> params = new HashMap<String,String>();
//		Builder builder = volleyUtils.getBuilder(url);
		params.put("realName",name );
		params.put("token", userInfo.getToken());
		params.put("udid", userInfo.getUdid());
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(false,url,params);
//		volleyUtils.requestHttpPostNoLoading(builder, this,RevisePersonMsgInfo.class);
	}


	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		hideSoftInput(this,edName , true);
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onDataChanged(RevisePersonMsgInfo t) {
		if (t != null) {
			if (t.isData() && "0".equals(t.getCode())) {
				Intent intent = new Intent();
				if (name != null) {
					intent.putExtra("name", name);
				}
				Message msg = Message.obtain();
				msg.what = 4;
				msg.obj = name;
				setResult(7, intent);
				if(edName != null){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edName.getWindowToken(), 0);
					finish();
				}
			} else {
				Toast.makeText(this, "输入错误", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
