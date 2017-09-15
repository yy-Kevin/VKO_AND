package cn.vko.ring.circle.activity;

import butterknife.Bind;
import butterknife.OnClick;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.EditTextUtils;

public class CreateGroupOneActivity extends BaseActivity {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvNext;
	@Bind(R.id.et_group_name)
	public EditText etGroupName;
	@Bind(R.id.et_group_des)
	public EditText etGroupIntro;
	@Bind(R.id.tv_num_filter)
	public TextView mTextView;

	private MyBroadcastReceiver receiver;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_create_group_one;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText(R.string.circle_create_group_title);
		tvNext.setText(R.string.circle_next_text);
		tvNext.setVisibility(View.VISIBLE);
		new EditTextUtils(etGroupIntro, 50, mTextView);
		new EditTextUtils(etGroupName, 8, null);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (receiver == null) {
			receiver = new MyBroadcastReceiver();
		}
		registerReceiver(receiver, new IntentFilter("FINISH"));
	}

	@OnClick(R.id.iv_back)
	void sayGoBack() {
		finish();
	}

	@OnClick(R.id.tv_right)
	void sayNext() {
		if (VkoContext.getInstance(this).doLoginCheckToSkip(this))
			return;
		String name = etGroupName.getText().toString();
		String intro = etGroupIntro.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this,"群名称不能为空",Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(intro)) {
			Toast.makeText(this,"群简介不能为空",Toast.LENGTH_LONG).show();
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putString("NAME", name);
		bundle.putString("INTRO", intro);
		StartActivityUtil.startActivity(this, CreateGroupTwoActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);

	}

	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!isFinishing()) {
				finish();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hideSoftInput(this, etGroupIntro, true);
	}
}
